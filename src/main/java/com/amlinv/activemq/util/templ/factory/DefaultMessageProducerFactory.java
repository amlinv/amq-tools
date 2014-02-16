package com.amlinv.activemq.util.templ.factory;

import javax.jms.Session;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

public class DefaultMessageProducerFactory implements MessagingClientFactory {
	public MessagingClient	createMessagingClient (ActiveMQConnection conn, ActiveMQSession sess,
				                       ActiveMQDestination dest) throws JMSException {
		final ActiveMQMessageProducer	prod;
		MessagingClient			result;

		prod = (ActiveMQMessageProducer) sess.createProducer(dest);

		result = new MessagingClient () {
			public boolean			isConsumer ()	{ return false; }
			public boolean			isProducer ()	{ return true; }
			public ActiveMQMessageConsumer	getConsumer ()	{ return null; }
			public ActiveMQMessageProducer	getProducer ()	{ return prod; }
		} ;

		return	result;
	}
}
