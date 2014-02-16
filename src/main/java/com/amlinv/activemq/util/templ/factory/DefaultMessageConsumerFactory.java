package com.amlinv.activemq.util.templ.factory;

import javax.jms.Session;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

public class DefaultMessageConsumerFactory implements MessagingClientFactory {
	public MessagingClient	createMessagingClient (ActiveMQConnection conn, ActiveMQSession sess,
				                       ActiveMQDestination dest) throws JMSException {
		final ActiveMQMessageConsumer	cons;
		MessagingClient			result;

		cons = (ActiveMQMessageConsumer) sess.createConsumer(dest);

		result = new MessagingClient () {
			public boolean			isConsumer ()	{ return true; }
			public boolean			isProducer ()	{ return false; }
			public ActiveMQMessageConsumer	getConsumer ()	{ return cons; }
			public ActiveMQMessageProducer	getProducer ()	{ return null; }
		} ;

		return	result;
	}
}
