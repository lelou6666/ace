package net.luminis.liq.server.log;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import net.luminis.liq.http.listener.constants.HttpConstants;
import net.luminis.liq.server.log.store.LogStore;

import org.apache.felix.dependencymanager.DependencyActivatorBase;
import org.apache.felix.dependencymanager.DependencyManager;
import org.apache.felix.dependencymanager.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase implements ManagedServiceFactory {
    private static final String LOG_NAME = "name";

    private final Map<String, Service> m_instances = new HashMap<String, Service>(); // String -> Service
    private DependencyManager m_manager;
    private volatile LogService m_log;

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
        m_manager = manager;
        Properties props = new Properties();
        props.put(Constants.SERVICE_PID, "net.luminis.liq.server.log.servlet.factory");
        manager.add(createService()
            .setInterface(ManagedServiceFactory.class.getName(), props)
            .setImplementation(this)
            .add(createServiceDependency().setService(LogService.class).setRequired(false)));    }

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }

    public void deleted(String pid) {
        Service log = m_instances.remove(pid);
        if (log != null) {
            m_manager.remove(log);
        }
    }

    public String getName() {
        return "Log Servlet Factory";
    }

    @SuppressWarnings("unchecked")
    public void updated(String pid, Dictionary dict) throws ConfigurationException {
        String name = (String) dict.get(LOG_NAME);
        if ((name == null) || "".equals(name)) {
            throw new ConfigurationException(LOG_NAME, "Log name has to be specified.");
        }
        String endpoint = (String) dict.get(HttpConstants.ENDPOINT);
        if ((endpoint == null) || "".equals(endpoint)) {
            throw new ConfigurationException(HttpConstants.ENDPOINT, "Servlet endpoint has to be specified.");
        }

        Service service = m_instances.get(pid);
        if (service == null) {
            Properties props = new Properties();
            props.put(HttpConstants.ENDPOINT, endpoint);
            service = m_manager.createService()
                .setInterface(HttpServlet.class.getName(), props)
                .setImplementation(new LogServlet(name))
                .add(createServiceDependency().setService(LogService.class).setRequired(false))
                .add(createServiceDependency().setService(LogStore.class, "(&("+Constants.OBJECTCLASS+"="+LogStore.class.getName()+")(name=" + name + "))").setRequired(true));

            m_instances.put(pid, service);
            m_manager.add(service);
        } else {
            m_log.log(LogService.LOG_INFO, "Ignoring configuration update because factory instance was already configured: " + name);
        }
    }
}
