package org.diveintojee.poc.jbehave.persistence;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component(LocalNodeClientFactoryBean.BEAN_ID)
public class LocalNodeClientFactoryBean extends NodeClientFactoryBean {

	public static final String	BEAN_ID	= "LocalNodeClient";

	@Autowired
	private Node				node;

	/**
	 * Eagerly create the singleton instance, if necessary.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(this.node);
	}

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
	protected Client createClient() {
		// Client client = new TransportClient();
		// ((TransportClient) client).addTransportAddress(new
		// InetSocketTransportAddress("localhost", 9300));
		// return client;
		return this.node.client();
	}
}
