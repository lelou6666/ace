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

/**
 * Compile time constants for this package. Includes configuration keys and event topics.
 */
public interface AgentConstants {

    /**
     * Namespace for all agent configuration property
     */
    String CONFIG_KEY_NAMESPACE = "agent";

    /**
     * Agent configuration property key postfix. When set to true, the system property will not overwrite an existing
     * configuration value
     */
    String CONFIG_KEY_RETAIN = ".retain";

    /**
     * Configuration loglevel for default logger. Should be a valid options s specified by {@link LoggingHandler.Levels}
     * , default is <code>INFO</code>.
     */
    String CONFIG_LOGGING_LEVEL = CONFIG_KEY_NAMESPACE + ".logging.level";

    /**
     * Configuration option to disable the default identification handler. When set to true some other bundle must
     * provide it as a service. Should be <code>{true,false}</code>, default is <code>false</code>.
     */
    String CONFIG_IDENTIFICATION_DISABLED = CONFIG_KEY_NAMESPACE + ".identification.disabled";

    /**
     * Configuration option for the agentid of the default identification handler. Should be a filesystem safe string,
     * default is <code>defaultAgentID</code>/
     */
    String CONFIG_IDENTIFICATION_AGENTID = CONFIG_KEY_NAMESPACE + ".identification.agentid";

    /**
     * Configuration option to disable the default discovery handler. When set to true some other bundle must provide it
     * as a service. Should be <code>{true,false}</code>, default is <code>false</code>.
     */
    String CONFIG_DISCOVERY_DISABLED = CONFIG_KEY_NAMESPACE + ".discovery.disabled";

    /**
     * Configuration option for the serverurls of the default discovery handler. Should be a comma-seperated list of
     * valid urls, default is <code>http://localhost:8080</code>.
     */
    String CONFIG_DISCOVERY_SERVERURLS = CONFIG_KEY_NAMESPACE + ".discovery.serverurls";

    /**
     * Configuration option to enable checking for the default discovery handler. Should be e {true,false}, default is
     * false.
     */
    String CONFIG_DISCOVERY_CHECKING = CONFIG_KEY_NAMESPACE + ".discovery.checking";

    /**
     * Configuration option to disable the default controller. When set to true some other bundle control the agent's
     * behavior. Should be <code>{true,false}</code>, default is <code>false</code>.
     */
    String CONFIG_CONTROLLER_DISABLED = CONFIG_KEY_NAMESPACE + ".controller.disabled";

    /**
     * Configuration option to set streaming behavior of the default controller. Should be <code>{true,false}</code>,
     * default is <code>true</code>.
     */
    String CONFIG_CONTROLLER_STREAMING = CONFIG_KEY_NAMESPACE + ".controller.streaming";

    /**
     * Configuration option to set fixpackages behavior of the default controller. Should be <code>{true,false}</code>,
     * default is <code>true</code>.
     */
    String CONFIG_CONTROLLER_FIXPACKAGES = CONFIG_KEY_NAMESPACE + ".controller.fixpackages";

    /**
     * Configuration option to set retries behavior of the default controller. Should be an int, default is
     * <code>1</code>.
     */
    String CONFIG_CONTROLLER_RETRIES = CONFIG_KEY_NAMESPACE + ".controller.retries";

    /**
     * Configuration option to set initial sync delay seconds of the default controller. Should be an int, default is
     * <code>5</code>.
     */
    String CONFIG_CONTROLLER_SYNCDELAY = CONFIG_KEY_NAMESPACE + ".controller.syndelay";

    /**
     * Configuration option to set initial sync interval seconds of the default controller. Should be an int, default is
     * <code>30</code>.
     */
    String CONFIG_CONTROLLER_SYNCINTERVAL = CONFIG_KEY_NAMESPACE + ".controller.syncinterval";

    /**
     * Configuration option to disable the default {@link ConnectionHandler}. When set to true some other bundle must
     * provide it as a service. Should be <code>{true,false}</code>, default is <code>false</code>.
     */
    String CONFIG_CONNECTION_DISABLED = CONFIG_KEY_NAMESPACE + ".connection.disabled";

    /**
     * Configuration auth type for the default {@link ConnectionHandler}. Should be a valid type as specified by
     * {@link ConnectionHandler.Types}, default if <code>NONE</code>.
     */
    String CONFIG_CONNECTION_AUTHTYPE = CONFIG_KEY_NAMESPACE + ".connection.authtype";

    /**
     * Configuration option to set the basic authentication username for the default {@link ConnectionHandler}. Should
     * be an string, default is <code>""</code>.
     */
    String CONFIG_CONNECTION_USERNAME = CONFIG_KEY_NAMESPACE + ".connection.username";

    /**
     * Configuration option to set the basic authentication password for the default {@link ConnectionHandler}. Should
     * be an string, default is <code>""</code>.
     */
    String CONFIG_CONNECTION_PASSWORD = CONFIG_KEY_NAMESPACE + ".connection.password";

    /**
     * Configuration option to set the client-cert protocol for the default {@link ConnectionHandler}. Should be a
     * string, default is <code>TLS</code>.
     */
    String CONFIG_CONNECTION_SSL_PROTOCOL = CONFIG_KEY_NAMESPACE + ".connection.sslProtocol";

    /**
     * Configuration option to set the client-cert authentication keystore path for the default
     * {@link ConnectionHandler} . Should be a valid path, default is <code>""</code>.
     */
    String CONFIG_CONNECTION_KEYFILE = CONFIG_KEY_NAMESPACE + ".connection.keyfile";

    /**
     * Configuration option to set the client-cert authentication keystore password for the default
     * {@link ConnectionHandler}. Should be a string, default is <code>""</code>.
     */
    String CONFIG_CONNECTION_KEYPASS = CONFIG_KEY_NAMESPACE + ".connection.keypass";

    /**
     * Configuration option to set the client-cert authentication truststore path for the default
     * {@link ConnectionHandler}. Should be a valid path, default is <code>""</code>.
     */
    String CONFIG_CONNECTION_TRUSTFILE = CONFIG_KEY_NAMESPACE + ".connection.trustfile";

    /**
     * Configuration option to set the client-cert authentication truststore password for the default
     * {@link ConnectionHandler}. Should be a string, default is <code>""</code>.
     */
    String CONFIG_CONNECTION_TRUSTPASS = CONFIG_KEY_NAMESPACE + ".connection.trustpass";

    /**
     * Configuration option to set the feedback channels for the default {@link FeedbackHandler}. Should be a
     * comma-separated string, default is <code>auditlog</code>.
     */
    String CONFIG_FEEDBACK_CHANNELS = CONFIG_KEY_NAMESPACE + ".feedback.channels";

    /**
     * Event topic for deployment install events.
     */
    String EVENT_DEPLOYMENT_INSTALL = "org/osgi/service/deployment/INSTALL";

    /**
     * Event topic for deployment uninstall events.
     */
    String EVENT_DEPLOYMENT_UNINSTALL = "org/osgi/service/deployment/UNINSTALL";

    /**
     * Event topic for deployment install events.
     */
    String EVENT_DEPLOYMENT_COMPLETE = "org/osgi/service/deployment/COMPLETE";

    /**
     * HTTP headers name for Deployment Package size estimate.
     */
    String HEADER_DPSIZE = "X-ACE-DPSize";
}
