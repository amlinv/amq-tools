package com.amlinv.activemq.util.templ.factory;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class DefaultConnectionFactory implements ConnectionFactory {
	public ActiveMQConnection	createConnection (String brokerUrl) throws JMSException {
		ActiveMQConnectionFactory	factory;

		factory = new ActiveMQConnectionFactory(brokerUrl);

		this.configureConnectionFactory(factory);

		return	(ActiveMQConnection) factory.createConnection();
	}

	/**
	 * Hook to allow subclasses to configure the connection factory with settings such as connection listener,
	 * user name, and password.
	 */
	protected void	configureConnectionFactory (ActiveMQConnectionFactory connFactory) {
	}
}
