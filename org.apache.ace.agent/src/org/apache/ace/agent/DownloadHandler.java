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

import java.net.URL;

import aQute.bnd.annotation.ConsumerType;

/**
 * Service interface for downloading content from a particular {@link URL}.
 * <p>
 * Download handles should be used for all download-related tasks, allowing one to download content asynchronously, and
 * temporarily stop an ongoing download to resume it on a later moment.
 * </p>
 */
@ConsumerType
public interface DownloadHandler {

    /**
     * Returns a {@link DownloadHandle} for a given URL.
     * 
     * @param url
     *            The url to create a download handle for, cannot be <code>null</code>.
     * @return a new {@link DownloadHandle} instance, never <code>null</code>.
     */
    DownloadHandle getHandle(URL url);
}
