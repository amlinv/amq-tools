package com.amlinv.activemq.util.templ.factory;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;

public class DefaultQueueFactory implements DestinationFactory {
	public ActiveMQDestination	createDestination (ActiveMQConnection conn, ActiveMQSession sess, String name)
	throws JMSException {
		return	new ActiveMQQueue(name);
	}
}
