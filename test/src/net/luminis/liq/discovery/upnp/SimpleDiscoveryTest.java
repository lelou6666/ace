package net.luminis.liq.discovery.upnp;

import static net.luminis.liq.test.utils.TestUtils.UNIT;

import java.net.URL;
import java.util.Properties;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SimpleDiscoveryTest {

    private static final String VALID_URL = "http://test.url.com:8080";
    private static final String INVALID_URL = "malformed url";

    private UPnPBasedDiscovery m_discovery;


    private IMocksControl m_control = EasyMock.createControl();


    @BeforeMethod(alwaysRun = true)
    protected void setUp() throws Exception {
        m_discovery = new UPnPBasedDiscovery();
        m_discovery.start();
    }



    /**
     * Test if discovering while there's no UPnPService
     * works as expected (returns null)
     *
     * @throws Exception
     */
    @Test(groups = { UNIT })
    public void simpleDiscoveryNoUPnPServices() throws ConfigurationException {
        URL url = m_discovery.discover();
        assert (url == null) : "Invalid url was returned (should have been null): " + url;
    }



    private UPnPDevice expectDeviceAdditionForURL(String url, String type) throws Exception {
        final String returnLocation = "returnLocation";
        final String returnType = "returnType";

        final Properties p = new Properties();
        p.put(returnLocation, url);
        p.put(returnType, type);

        m_control.checkOrder(false);

        final UPnPAction action1 = m_control.createMock(UPnPAction.class);
        EasyMock.expect(action1.getOutputArgumentNames()).andReturn(new String[]{returnLocation}).anyTimes();
        EasyMock.expect(action1.invoke(null)).andReturn(p).anyTimes();

        final UPnPAction action2 = m_control.createMock(UPnPAction.class);
        EasyMock.expect(action2.getOutputArgumentNames()).andReturn(new String[]{returnType}).anyTimes();
        EasyMock.expect(action2.invoke(null)).andReturn(p).anyTimes();

        final UPnPService service = m_control.createMock(UPnPService.class);
        EasyMock.expect(service.getAction("GetLocation")).andReturn(action1).anyTimes();
        EasyMock.expect(service.getAction("GetServerType")).andReturn(action2).anyTimes();

        UPnPDevice device = m_control.createMock(UPnPDevice.class);
        EasyMock.expect(device.getService(EasyMock.isA(String.class))).andReturn(service).anyTimes();

        return device;
    }


    /**
     * Test if the url as provided by a UPnPDevice
     * is returned as expected.
     * @throws Exception
     */
    @Test(groups = {UNIT})
    public void simpleDiscoverySingleUPnPDevice() throws Exception {

        m_control.reset();

        UPnPDevice device = expectDeviceAdditionForURL(VALID_URL, "RelayServer");
        ServiceReference ref = m_control.createMock(ServiceReference.class);

        m_control.replay();

        m_discovery.added(ref, device);
        m_discovery.discover();

        m_control.verify();


    }

    /**
     * Test if the url as provided by a UPnPDevice
     * is returned as expected.
     * @throws Exception
     */
    @Test(groups = {UNIT})
    public void simpleDiscoveryMultipleUPnPDevices() throws Exception {
        m_control.reset();

        UPnPDevice device1 = expectDeviceAdditionForURL(VALID_URL, "RelayServer");
        UPnPDevice device2 = expectDeviceAdditionForURL(INVALID_URL, "RelayServer");

        ServiceReference ref = m_control.createMock(ServiceReference.class);

        m_control.replay();

        m_discovery.added(ref, device1);
        m_discovery.added(ref, device2);

        URL url = m_discovery.discover();
        assert VALID_URL.equals(url.toString()) : "Valid url was not returned";

        m_control.verify();
    }
}
