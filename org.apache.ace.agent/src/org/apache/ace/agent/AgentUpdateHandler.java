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

<<<<<<< HEAD:org.apache.ace.agent/src/org/apache/ace/agent/ManagementAgent.java
import aQute.bnd.annotation.ProviderType;
=======
import aQute.bnd.annotation.ConsumerType;
>>>>>>> refs/remotes/apache/trunk:org.apache.ace.agent/src/org/apache/ace/agent/AgentUpdateHandler.java

/**
 * Agent context delegate interface that is responsible for managing agent updates.
 */
<<<<<<< HEAD:org.apache.ace.agent/src/org/apache/ace/agent/ManagementAgent.java
@ProviderType
public interface ManagementAgent {

=======
@ConsumerType
public interface AgentUpdateHandler extends UpdateHandler {
    // No additional methods
>>>>>>> refs/remotes/apache/trunk:org.apache.ace.agent/src/org/apache/ace/agent/AgentUpdateHandler.java
}
