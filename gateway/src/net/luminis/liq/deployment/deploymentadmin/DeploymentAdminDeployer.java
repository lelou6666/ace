package net.luminis.liq.deployment.deploymentadmin;

import java.io.InputStream;

import net.luminis.liq.deployment.Deployment;

import org.osgi.framework.Version;
import org.osgi.service.deploymentadmin.DeploymentAdmin;
import org.osgi.service.deploymentadmin.DeploymentPackage;
import org.osgi.service.log.LogService;

/**
 * Implementation of the <code>DeploymentService</code> interface that uses the <code>DeploymentAdmin</code>
 * to deploy components.
 */
public class DeploymentAdminDeployer implements Deployment {
    private volatile LogService m_log; /* will be injected by dependencymanager */
    private volatile DeploymentAdmin m_admin; /* will be injected by dependencymanager */

    public String getName(Object object) throws IllegalArgumentException {
        if (!(object instanceof DeploymentPackage)) {
            throw new IllegalArgumentException("Argument is not a DeploymentPackage");
        }
        return ((DeploymentPackage) object).getName();
    }

    public Version getVersion(Object object) throws IllegalArgumentException {
        if (!(object instanceof DeploymentPackage)) {
            throw new IllegalArgumentException("Argument is not a DeploymentPackage");
        }
        return ((DeploymentPackage) object).getVersion();
    }

    public Object install(InputStream inputStream) throws Exception {
        DeploymentPackage deploymentPackage = m_admin.installDeploymentPackage(inputStream);
        m_log.log(LogService.LOG_INFO, "Deployment Package installed: name=" + deploymentPackage.getName() + " version=" + deploymentPackage.getVersion());
        return deploymentPackage;
    }

    public Object[] list() {
        // DeploymentAdmin spec says this call should never return null
        return m_admin.listDeploymentPackages();
    }
}
