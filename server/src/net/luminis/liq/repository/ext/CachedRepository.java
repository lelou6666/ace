package net.luminis.liq.repository.ext;

import java.io.IOException;
import java.io.InputStream;

import net.luminis.liq.repository.Repository;


/**
 * Provides a cached repository representation, allowing the storing of local changes, without
 * committing them to the actual repository immediately.
 */
public interface CachedRepository extends Repository {

    /**
     * Checks our the most current version from the actual repository.
     * @param fail Indicates that this method should throw an IOException when no data
     * is available. Setting it to <code>false</code> will make it return an
     * empty stream in that case.
     * @return An input stream representing the checked out object.
     * @throws IOException Is thrown when the actual repository's commit does.
     */
    public InputStream checkout(boolean fail) throws IOException;

    /**
     * Commits the most current version to the actual repository.
     * @return true on success, false on failure (e.g. bad version number)
     * @throws IOException Is thrown when the actual repository's commit does.
     */
    public boolean commit() throws IOException;

    /**
     * Gets the most recent version of the object. If no current version is available,
     * and empty stream will be returned.
     * @param fail Indicates that this method should throw an IOException when no data
     * is available. Setting it to <code>false</code> will make it return an
     * empty stream in that case.
     * @return An input stream representing the most recently written object.
     * @throws IOException Thrown when there is a problem retrieving the data.
     */
    public InputStream getLocal(boolean fail) throws IOException;

    /**
     * Writes the most recent version of the object.
     * @throws IOException Thrown when there is a problem storing the data.
     */
    public void writeLocal(InputStream data) throws IOException;

    /**
     * Undoes all changes made using <code>writeLocal()</code> since the
     * last <code>commit</code> or <code>checkout</code>.
     * @throws IOException
     */
    public boolean revert() throws IOException;

    /**
     * Gets the most recent version of this repository, that is, the most recent version
     * number that is either committed (successfully) or checked out.
     * @return The most recent version of the underlying repository.
     */
    public long getMostRecentVersion();

    /**
     * Checks whether the version we have locally is current with respect to the version
     * on the server.
     * @return whether the version we have locally is current with respect to the version
     * on the server.
     * @throws IOException Thrown when an error occurs communicating with the server.
     */
    public boolean isCurrent() throws IOException;
}
