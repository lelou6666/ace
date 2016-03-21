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

<<<<<<< HEAD:org.apache.ace.http/src/org/apache/ace/http/listener/constants/HttpConstants.java
import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface HttpConstants
{
    /**
     * Endpoint constant to be used by several Servlet bundles.
     */
    public static final String ENDPOINT = "org.apache.ace.server.servlet.endpoint";
}
=======
package org.apache.ace.useradmin.repository;

import java.util.concurrent.atomic.AtomicLong;

import org.osgi.service.useradmin.Role;

/**
 * 
 */
public interface RepoCurrentChecker {
    
    void checkRepoUpToDate(Role context, AtomicLong expectedVersion) throws IllegalStateException;
}
>>>>>>> refs/remotes/apache/trunk:org.apache.ace.useradmin/src/org/apache/ace/useradmin/repository/RepoCurrentChecker.java
