/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class NodeClientFactoryBean extends AbstractFactoryBean<Client> {

	private Client		client;

	@Value("classpath:elasticsearch/mappings/advert.json")
	private Resource	mapping;

	protected abstract Client createClient();

	/**
	 * Template method that subclasses must override to construct the object
	 * returned by this factory.
	 * <p>
	 * Invoked on initialization of this FactoryBean in case of a singleton;
	 * else, on each {@link #getObject()} call.
	 * 
	 * @return the object returned by this factory
	 * @throws Exception
	 *             if an exception occured during object creation
	 * @see #getObject()
	 */
	@Override
	public Client createInstance() throws Exception {

		this.client = createClient();

		// Create index if not exist
		if (this.client.admin().indices().prepareExists(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet()
				.exists()) this.client.admin().indices().prepareDelete(PersistenceConstants.ADVERTS_INDEX_NAME)
				.execute().actionGet();
		this.client.admin().indices().prepareCreate(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet();

		// Configure mapping
		final Writer writer = new StringWriter();
		IOUtils.copy(this.mapping.getInputStream(), writer, "UTF-8");
		this.client.admin().indices().preparePutMapping(PersistenceConstants.ADVERTS_INDEX_NAME)
				.setSource(writer.toString()).setType(PersistenceConstants.ADVERTS_TYPE_NAME).execute().actionGet();

		return this.client;
	}

	/**
	 * Destroy the singleton instance, if any.
	 * 
	 * @see #destroyInstance(Object)
	 */
	@Override
	public final void destroy() throws Exception {
		this.client.close();
	}

	/**
	 * This abstract method declaration mirrors the method in the FactoryBean
	 * interface, for a consistent offering of abstract template methods.
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public final Class<?> getObjectType() {
		return Client.class;
	}
}
