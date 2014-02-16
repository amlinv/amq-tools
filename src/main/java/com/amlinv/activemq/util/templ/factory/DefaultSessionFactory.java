package com.amlinv.activemq.util.templ.factory;

import javax.jms.Session;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;

public class DefaultSessionFactory implements SessionFactory {
	protected boolean	transactedInd = false;
	protected int		nonTransMode = Session.AUTO_ACKNOWLEDGE;

	public DefaultSessionFactory () {
	}

	public DefaultSessionFactory (boolean transacted) {
		this.transactedInd = transacted;
	}

	public DefaultSessionFactory (int mode) {
		this.nonTransMode = mode;
	}

	@Override
	public ActiveMQSession	createSession (ActiveMQConnection conn) throws JMSException {
		ActiveMQSession	result;

		result = (ActiveMQSession) conn.createSession(this.transactedInd, this.nonTransMode);

		return	result;
	}
}
