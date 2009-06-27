package net.luminis.liq.client.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.luminis.liq.client.repository.object.DeploymentArtifact;
import net.luminis.liq.client.repository.object.DeploymentVersionObject;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Implementation class for the DeploymentVersionObject. For 'what it does', see DeploymentVersionObject,
 * for 'how it works', see RepositoryObjectImpl.
 */
public class DeploymentVersionObjectImpl extends RepositoryObjectImpl<DeploymentVersionObject> implements DeploymentVersionObject {
    private final static String XML_NODE = "deploymentversion";
    private final static String ARTIFACTS_XML_NODE = "artifacts";
    private DeploymentArtifact[] m_deploymentArtifacts;

    /**
     * Creates a new <code>DeploymentVersionObjectImpl</code>.
     * @param attributes A map of attributes; must include <code>KEY_GATEWAYID</code>, <code>KEY_VERSION</code>.
     * @param deploymentArtifacts A (possibly empty) array of DeploymentArtifacts.
     * @param notifier A change notifier to be used by this object.
     */
    DeploymentVersionObjectImpl(Map<String, String> attributes, ChangeNotifier notifier) {
        super(checkAttributes(attributes, new String[] {KEY_GATEWAYID, KEY_VERSION}, new boolean[] {false, false}), notifier, XML_NODE);
    }

    DeploymentVersionObjectImpl(Map<String, String> attributes, Map<String, String> tags, ChangeNotifier notifier) {
        super(checkAttributes(attributes, new String[] {KEY_GATEWAYID, KEY_VERSION}, new boolean[] {false, false}), tags, notifier, XML_NODE);
    }

    DeploymentVersionObjectImpl(HierarchicalStreamReader reader, ChangeNotifier notifier) {
        super(reader, notifier, XML_NODE);
    }

    synchronized void setDeploymentArtifacts(DeploymentArtifact[] deploymentArtifacts) {
        if (m_deploymentArtifacts != null) {
            throw new IllegalStateException("Deployment artifacts are already set; this can only be done once.");
        }
        if (deploymentArtifacts == null) {
            throw new IllegalArgumentException("The argument should not be null.");
        }
        else {
            m_deploymentArtifacts = deploymentArtifacts;
        }
    }

    @Override
    protected void readCustom(HierarchicalStreamReader reader) {
        List<DeploymentArtifact> result = new ArrayList<DeploymentArtifact>();
        reader.moveDown();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            DeploymentArtifactImpl deploymentArtifactImpl = new DeploymentArtifactImpl(reader);
            result.add(deploymentArtifactImpl);
            reader.moveUp();
        }
        setDeploymentArtifacts(result.toArray(new DeploymentArtifact[result.size()]));
        reader.moveUp();
    }

    @Override
    protected synchronized void writeCustom(HierarchicalStreamWriter writer) {
        if (m_deploymentArtifacts == null) {
            throw new IllegalStateException("This object is not fully initialized, so it cannot be serialized.");
        }
        writer.startNode(ARTIFACTS_XML_NODE);
        for (DeploymentArtifact da : m_deploymentArtifacts) {
            ((DeploymentArtifactImpl) da).marshal(writer);
        }
        writer.endNode();
    }

    private static String[] DEFINING_KEYS = new String[] {KEY_GATEWAYID, KEY_VERSION};
    @Override
    String[] getDefiningKeys() {
        return DEFINING_KEYS;
    }

    public String getGatewayID() {
        return getAttribute(KEY_GATEWAYID);
    }

    public String getVersion() {
        return getAttribute(KEY_VERSION);
    }

    public synchronized DeploymentArtifact[] getDeploymentArtifacts() {
        if (m_deploymentArtifacts == null) {
            throw new IllegalStateException("This object is not fully initialized yet.");
        }
        return m_deploymentArtifacts.clone();
    }
}

