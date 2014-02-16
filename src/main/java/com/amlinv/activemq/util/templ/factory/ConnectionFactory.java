package com.amlinv.activemq.util.templ.factory;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;

public interface ConnectionFactory {
	ActiveMQConnection	createConnection (String brokerUrl) throws JMSException;
}
