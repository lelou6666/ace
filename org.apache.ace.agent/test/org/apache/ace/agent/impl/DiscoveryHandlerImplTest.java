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
package org.apache.ace.agent.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.net.URL;

import org.apache.ace.agent.AgentConstants;
import org.apache.ace.agent.AgentContext;
import org.apache.ace.agent.ConfigurationHandler;
import org.apache.ace.agent.ConnectionHandler;
import org.apache.ace.agent.DiscoveryHandler;
import org.apache.ace.agent.EventsHandler;
import org.apache.ace.agent.testutil.BaseAgentTest;
import org.apache.ace.agent.testutil.TestWebServer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Testing {@link DiscoveryHandlerImpl}.
 */
public class DiscoveryHandlerImplTest extends BaseAgentTest {

    private static final int PORT = 8882;

    private TestWebServer m_webServer;
    private URL m_availableURL;
    private URL m_unavailableURL;

    private AgentContext m_agentContext;
    private AgentContextImpl m_agentContextImpl;

    @BeforeTest
    public void setUpOnceAgain() throws Exception {
        m_webServer = new TestWebServer(PORT, "/", "generated");
        m_webServer.start();
        m_availableURL = new URL("http://localhost:" + PORT);
        m_unavailableURL = new URL("http://localhost:9999");

        m_agentContextImpl = mockAgentContext();
        m_agentContext = m_agentContextImpl;
        m_agentContextImpl.setHandler(DiscoveryHandler.class, new DiscoveryHandlerImpl());
        m_agentContextImpl.setHandler(EventsHandler.class, new EventsHandlerImpl(mockBundleContext()));
        m_agentContextImpl.setHandler(ConfigurationHandler.class, new ConfigurationHandlerImpl());
        m_agentContextImpl.setHandler(ConnectionHandler.class, new ConnectionHandlerImpl());
        replayTestMocks();
        m_agentContextImpl.start();
    }

    @AfterTest
    public void tearDownOnceAgain() throws Exception {
        m_webServer.stop();

        m_agentContextImpl.stop();
        verifyTestMocks();
        clearTestMocks();
    }

    @Test
    public void testAvailableURL() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_availableURL.toExternalForm());
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertEquals(discoveryHandler.getServerUrl(), m_availableURL);
    }

    @Test
    public void testUnavailableURL_unavailable() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_unavailableURL.toExternalForm());
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertNull(discoveryHandler.getServerUrl());
    }

    @Test
    public void testUnavailableAfterConfigUpdate() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_availableURL.toExternalForm());
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertEquals(discoveryHandler.getServerUrl(), m_availableURL);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_unavailableURL.toExternalForm());

        assertNull(discoveryHandler.getServerUrl());
    }

    @Test
    public void testAvailableAfterConfigUpdate() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_unavailableURL.toExternalForm());
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertNull(discoveryHandler.getServerUrl());

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_availableURL.toExternalForm());

        assertEquals(discoveryHandler.getServerUrl(), m_availableURL);
    }

    @Test
    public void testAvailableAfterUnavailableURL() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, m_unavailableURL.toExternalForm() + "," + m_availableURL.toExternalForm());
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertEquals(discoveryHandler.getServerUrl(), m_availableURL);
    }

    @Test
    public void testEmptyURLConfig() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, "");
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertNull(discoveryHandler.getServerUrl());
    }

    @Test
    public void testBadURLConfig() throws Exception {
        ConfigurationHandler configurationHandler = m_agentContext.getHandler(ConfigurationHandler.class);

        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_SERVERURLS, "invalidURL");
        configurationHandler.put(AgentConstants.CONFIG_DISCOVERY_CHECKING, "true");

        DiscoveryHandler discoveryHandler = m_agentContext.getHandler(DiscoveryHandler.class);
        assertNull(discoveryHandler.getServerUrl());
    }
}
