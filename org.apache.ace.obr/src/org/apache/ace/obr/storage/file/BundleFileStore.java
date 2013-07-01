/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ace.obr.storage.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;
import java.util.Stack;

import org.apache.ace.obr.metadata.MetadataGenerator;
import org.apache.ace.obr.metadata.util.ResourceMetaData;
import org.apache.ace.obr.storage.BundleStore;
import org.apache.ace.obr.storage.file.constants.OBRFileStoreConstants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

/**
 * This BundleStore retrieves the files from the file system. Via the Configurator the relative path is set, and all
 * bundles and the repository.xml should be retrievable from that path (which will internally be converted to an
 * absolute path).
 */
public class BundleFileStore implements BundleStore, ManagedService {

    private static int BUFFER_SIZE = 8 * 1024;
    private static final String REPOSITORY_XML = "repository.xml";

    // injected by dependencymanager
    private volatile MetadataGenerator m_metadata;
    private volatile LogService m_log;

    private volatile String m_dirChecksum;
    private volatile File m_dir;

    /**
     * Checks if the the directory was modified since we last checked. If so, the meta-data generator is called.
     * 
     * @throws IOException
     *             If there is a problem synchronizing the meta-data.
     */
    public void synchronizeMetadata() throws IOException {
        File dir = m_dir;
        synchronized (REPOSITORY_XML) {
            if (m_dirChecksum == null || !m_dirChecksum.equals(getDirChecksum(dir))) {
                m_metadata.generateMetadata(dir);
                m_dirChecksum = getDirChecksum(dir);
            }
        }
    }

    public InputStream get(String fileName) throws IOException {
        if (REPOSITORY_XML.equals(fileName)) {
            synchronizeMetadata();
        }
        FileInputStream result = null;
        try {
            result = new FileInputStream(createFile(fileName));
        }
        catch (FileNotFoundException e) {
            // Resource does not exist; notify caller by returning null...
        }
        return result;
    }

    public String put(InputStream data, String fileName) throws IOException {

        File tempFile = downloadToTempFile(data);

        ResourceMetaData metaData = ResourceMetaData.getBundleMetaData(tempFile);
        if (metaData == null) {
            metaData = ResourceMetaData.getArtifactMetaData(fileName);
        }
        if (metaData == null) {
            throw new IOException("Not a valid bundle and no filename found (filename = " + fileName + ")");
        }

        File storeLocation = getResourceFile(metaData);
        if (storeLocation == null) {
            throw new IOException("Failed to store resource (filename = " + fileName + ")");
        }
        if (storeLocation.exists()) {
            m_log.log(LogService.LOG_ERROR, "Resource already existed in OBR (filename = " + fileName + ")");
            return null;
        }

        moveFile(tempFile, storeLocation);

        String filePath = storeLocation.toURI().toString().substring(getWorkingDir().toURI().toString().length());
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        return filePath;
    }

    public boolean remove(String fileName) throws IOException {
        File file = createFile(fileName);
        if (file.exists()) {
            if (file.delete()) {
                // deleting empty parent dirs
                while ((file = file.getParentFile()) != null && !file.equals(m_dir) && file.list().length == 0) {
                    file.delete();
                }
                return true;
            }
            else {
                throw new IOException("Unable to delete file (" + file.getAbsolutePath() + ")");
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void updated(Dictionary dict) throws ConfigurationException {
        if (dict != null) {
            String path = (String) dict.get(OBRFileStoreConstants.FILE_LOCATION_KEY);
            if (path == null) {
                throw new ConfigurationException(OBRFileStoreConstants.FILE_LOCATION_KEY, "Missing property");
            }

            File newDir = new File(path);
            File curDir = getWorkingDir();

            if (!newDir.equals(curDir)) {
                if (!newDir.exists()) {
                    newDir.mkdirs();
                }
                else if (!newDir.isDirectory()) {
                    throw new ConfigurationException(OBRFileStoreConstants.FILE_LOCATION_KEY, "Is not a directory: " + newDir);
                }

                m_dir = newDir;
                m_dirChecksum = "";
            }
        }
    }

    /**
     * Called by dependencymanager upon start of this component.
     */
    protected void start() {
        try {
            synchronizeMetadata();
        }
        catch (IOException e) {
            m_log.log(LogService.LOG_ERROR, "Could not generate initial meta data for bundle repository");
        }
    }

    /**
     * Computes a magic checksum used to determine whether there where changes in the directory without actually looking
     * into the files or using observation.
     * 
     * @param dir
     *            The directory
     * @return The checksum
     */
    private String getDirChecksum(File dir) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            // really should not happen
            m_log.log(LogService.LOG_WARNING, "Unable to get an MD5 digest. Metadata will refresh every ten minutes.", e);
            return "" + (System.currentTimeMillis() / 600000);
        }

        Stack<File> dirs = new Stack<File>();
        dirs.push(dir);
        while (!dirs.isEmpty()) {
            File pwd = dirs.pop();
            for (File file : pwd.listFiles()) {
                if (file.isDirectory()) {
                    dirs.push(file);
                    continue;
                }
                // basically we hash the filenames, but...
                // include last-modified to detect touched files
                // include length to work around last-modified rounding issues
                String magic = file.getName() + file.length() + file.lastModified();
                digest.update(magic.getBytes());
            }
        }
        String checksum = new BigInteger(digest.digest()).toString();
        return checksum;
    }

    /**
     * Downloads a given input stream to a temporary file.
     * 
     * @param source
     *            the input stream to download;
     * @throws IOException
     *             in case of I/O problems.
     */
    private File downloadToTempFile(InputStream source) throws IOException {
        File tempFile = File.createTempFile("obr", ".tmp");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            int read;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((read = source.read(buffer)) >= 0) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
            return tempFile;
        }
        finally {
            closeQuietly(fos);
        }
    }

    /**
     * Encapsulated the store layout strategy by creating the resource file based on the provided meta-data.
     * 
     * @param metaData
     *            the meta-data for the resource
     * @return the resource file
     * @throws IOException
     *             in case of I/O problems.
     */
    private File getResourceFile(ResourceMetaData metaData) throws IOException {

        File resourceFile = null;
        String[] dirs = metaData.getSymbolicName().split("\\.");
        for (String subDir : dirs) {
            if (resourceFile == null) {
                resourceFile = new File(getWorkingDir(), subDir);
            }
            else {
                resourceFile = new File(resourceFile, subDir);
            }
        }
        if (!resourceFile.exists() && !resourceFile.mkdirs()) {
            throw new IOException("Failed to create store directory");
        }

        if (metaData.getExtension() != null && !metaData.getExtension().equals("")) {
            resourceFile = new File(resourceFile, metaData.getSymbolicName() + "-" + metaData.getVersion() + "." + metaData.getExtension());
        }
        else {
            resourceFile = new File(resourceFile, metaData.getSymbolicName() + "-" + metaData.getVersion());
        }
        return resourceFile;
    }

    /**
     * @return the working directory of this file store.
     */
    private File getWorkingDir() {
        return m_dir;
    }

    /**
     * Moves a given source file to a destination location, effectively resulting in a rename.
     * 
     * @param source
     *            the source file to move;
     * @param dest
     *            the destination file to move the file to.
     * @return <code>true</code> if the move succeeded.
     * @throws IOException
     *             in case of I/O problems.
     */
    private boolean moveFile(File source, File dest) throws IOException {
        final int bufferSize = 1024 * 1024; // 1MB

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;

        try {
            fis = new FileInputStream(source);
            input = fis.getChannel();

            fos = new FileOutputStream(dest);
            output = fos.getChannel();

            long size = input.size();
            long pos = 0;
            while (pos < size) {
                pos += output.transferFrom(input, pos, Math.min(size - pos, bufferSize));
            }
        }
        finally {
            closeQuietly(fos);
            closeQuietly(fis);
            closeQuietly(output);
            closeQuietly(input);
        }

        if (source.length() != dest.length()) {
            throw new IOException("Failed to move file! Not all contents from '" + source + "' copied to '" + dest + "'!");
        }

        dest.setLastModified(source.lastModified());

        if (!source.delete()) {
            dest.delete();
            throw new IOException("Failed to move file! Source file (" + source + ") locked?");
        }

        return true;
    }

    /**
     * Safely closes a given resource, ignoring any I/O exceptions that might occur by this.
     * 
     * @param resource
     *            the resource to close, can be <code>null</code>.
     */
    private void closeQuietly(Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        }
        catch (IOException e) {
            // Ignored...
        }
    }

    /**
     * Creates a {@link File} object with the given file name in the current working directory.
     * 
     * @param fileName
     *            the name of the file.
     * @return a {@link File} object, never <code>null</code>.
     * @see #getWorkingDir()
     */
    private File createFile(String fileName) {
        return new File(getWorkingDir(), fileName);
    }
}
