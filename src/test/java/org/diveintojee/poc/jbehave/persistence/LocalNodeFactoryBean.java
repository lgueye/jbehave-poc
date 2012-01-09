package org.diveintojee.poc.jbehave.persistence;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class LocalNodeFactoryBean extends AbstractFactoryBean<Node> {
    private Node node;

    /**
     * Template method that subclasses must override to construct the object returned by this factory.
     * <p>
     * Invoked on initialization of this FactoryBean in case of a singleton; else, on each {@link #getObject()} call.
     * 
     * @return the object returned by this factory
     * @throws Exception if an exception occured during object creation
     * @see #getObject()
     */
    @Override
    protected Node createInstance() throws Exception {
        node = NodeBuilder.nodeBuilder() //
                .local(true).data(true) //
                .settings(ImmutableSettings.settingsBuilder() //
                        .loadFromClasspath("elasticsearch/elasticsearch.yml")) //
                .node();
        return node;
    }

    /**
     * Destroy the singleton instance, if any.
     * 
     * @see #destroyInstance(Object)
     */
    @Override
    public void destroy() throws Exception {
        node.close();
    }

    /**
     * This abstract method declaration mirrors the method in the FactoryBean interface, for a consistent offering of
     * abstract template methods.
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return Node.class;
    }
}
