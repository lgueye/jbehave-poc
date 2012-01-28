/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class NodeClientFactoryBean extends AbstractFactoryBean<Client> {

    private Client client;

    @Value("classpath:elasticsearch/mappings/advert.json")
    private Resource mapping;

    protected abstract Client createClient();

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
    public Client createInstance() throws Exception {

        client = createClient();

        // Create index if not exist
        if (client.admin().indices().prepareExists(Utils.pluralize(Advert.class)).execute().actionGet().exists()) {
            client.admin().indices().prepareDelete(Utils.pluralize(Advert.class)).execute().actionGet();
        }
        client.admin().indices().prepareCreate(Utils.pluralize(Advert.class)).execute().actionGet();

        // Configure mapping
        final Writer writer = new StringWriter();
        IOUtils.copy(mapping.getInputStream(), writer, "UTF-8");
        client.admin().indices().preparePutMapping(Utils.pluralize(Advert.class)).setSource(writer.toString())
                .setType(Utils.minimize(Advert.class)).execute().actionGet();

        return client;
    }

    /**
     * Destroy the singleton instance, if any.
     *
     * @see #destroyInstance(Object)
     */
    @Override
    public final void destroy() throws Exception {
        client.close();
    }

    /**
     * This abstract method declaration mirrors the method in the FactoryBean interface, for a consistent offering of
     * abstract template methods.
     *
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public final Class<?> getObjectType() {
        return Client.class;
    }
}
