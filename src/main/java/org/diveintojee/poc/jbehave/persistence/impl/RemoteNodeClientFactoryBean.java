package org.diveintojee.poc.jbehave.persistence.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.diveintojee.poc.jbehave.persistence.NodeClientFactoryBean;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.util.Assert;

public class RemoteNodeClientFactoryBean extends NodeClientFactoryBean {

    public static final String BEAN_ID = "RemoteNodeClient";

    private static Map<String, Integer> transportAddresses;

    static {

        transportAddresses = new HashMap<String, Integer>();

        transportAddresses.put("localhost", 9300);

    }

    /**
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#afterPropertiesSet()
     */

    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notEmpty(transportAddresses, "At least one hots/port pair required");

        for (final Entry<String, Integer> entry : transportAddresses.entrySet()) {
            Assert.hasLength(entry.getKey(), "host is required");
            Assert.notNull(entry.getValue(), "port is required");
        }

        super.afterPropertiesSet();

    }

    @Override
    protected Client createClient() {

        final Client client = new TransportClient();

        for (final Entry<String, Integer> entry : transportAddresses.entrySet()) {

            final String host = entry.getKey();

            final Integer port = entry.getValue();

            ((TransportClient) client).addTransportAddress(new InetSocketTransportAddress(host, port));

        }

        return client;
    }

}
