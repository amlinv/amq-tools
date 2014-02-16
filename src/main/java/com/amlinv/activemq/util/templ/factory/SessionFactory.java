package com.amlinv.activemq.util.templ.factory;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;

public interface SessionFactory {
	ActiveMQSession	createSession (ActiveMQConnection conn) throws JMSException;
}
