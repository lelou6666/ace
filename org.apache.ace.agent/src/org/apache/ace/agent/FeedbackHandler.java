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

import java.io.IOException;
import java.util.Set;

<<<<<<< HEAD:org.apache.ace.location.upnp/src/org/apache/ace/location/upnp/util/HostUtil.java
import aQute.bnd.annotation.ProviderType;

@ProviderType
public class HostUtil
{
	private HostUtil() {}
=======
import aQute.bnd.annotation.ConsumerType;
>>>>>>> refs/remotes/apache/trunk:org.apache.ace.agent/src/org/apache/ace/agent/FeedbackHandler.java

/**
 * Agent context delegate interface that is responsible for handling feedback channels.
 */
@ConsumerType
public interface FeedbackHandler {

    /** Returns the feedback channels names */
    Set<String> getChannelNames() throws IOException;

    /** Returns the feedback channel for a name */
    FeedbackChannel getChannel(String name) throws IOException;
}
