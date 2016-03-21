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
package org.apache.ace.agent;

<<<<<<< HEAD:org.apache.ace.agent/src/org/apache/ace/agent/Constants.java
import aQute.bnd.annotation.ProviderType;

=======
import java.io.IOException;
import java.io.InputStream;

import aQute.bnd.annotation.ConsumerType;
>>>>>>> refs/remotes/apache/trunk:org.apache.ace.agent/src/org/apache/ace/agent/DownloadResult.java

/**
 * Represents the result of a download task.
 * 
 */
<<<<<<< HEAD:org.apache.ace.agent/src/org/apache/ace/agent/Constants.java
@ProviderType
public interface Constants {

    /**
     * Configuration key for the list of agents.
     */
    String CONFIG_AGENTS_KEY = "agents";
    
    /**
     * Configuration key for the agent.
     */
    String CONFIG_AGENT_KEY = "agent";
    
=======
@ConsumerType
public interface DownloadResult {
>>>>>>> refs/remotes/apache/trunk:org.apache.ace.agent/src/org/apache/ace/agent/DownloadResult.java
    /**
     * Returns an input stream to the downloaded result.
     * 
     * @return an input stream, can be <code>null</code> if the download was unsuccessful.
     */
    InputStream getInputStream() throws IOException;

    /**
     * @return <code>true</code> if the download is complete, <code>false</code> if not.
     */
    boolean isComplete();
}
