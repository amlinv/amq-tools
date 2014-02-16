package com.amlinv.activemq.util.templ.impl;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

import com.amlinv.activemq.util.templ.ActiveMQProcessor;

import com.amlinv.activemq.util.templ.factory.ConnectionFactory;
import com.amlinv.activemq.util.templ.factory.DestinationFactory;
import com.amlinv.activemq.util.templ.factory.MessagingClient;
import com.amlinv.activemq.util.templ.factory.MessagingClientFactory;
import com.amlinv.activemq.util.templ.factory.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ActiveMQProcessorTempl implements ActiveMQProcessor {
	private static final Logger		LOG = LoggerFactory.getLogger(ActiveMQProcessorTempl.class);

	protected ConnectionFactory		connectionFactory;
	protected DestinationFactory		destinationFactory;
	protected MessagingClientFactory	messagingClientFactory;
	protected SessionFactory		sessionFactory;

	protected ActiveMQConnection		amqConnection;
	protected ActiveMQSession		amqSession;
	protected ActiveMQDestination		amqDestination;
	protected MessagingClient		msgClient;

	/**
	 * Perform one iteration of the processor which equates to a complete transaction or an iteration of a
	 *  processing loop in which consumed messages are acknowledged on completion of the loop iteration.
	 */
	protected abstract boolean	executeProcessorIteration (MessagingClient client) throws JMSException;

	@Override
	public void	setConnectionFactory (ConnectionFactory connFactory) {
		this.connectionFactory = connFactory;
	}

	@Override
	public void	setDestinationFactory (DestinationFactory destFactory) {
		this.destinationFactory = destFactory;
	}

	@Override
	public void	setMessagingClientFactory (MessagingClientFactory clientFactory) {
		this.messagingClientFactory = clientFactory;
	}

	@Override
	public void	setSessionFactory (SessionFactory sessFactory) {
		this.sessionFactory = sessFactory;
	}

	/**
	 * TBD: JMSException handler strategy, processor factory instead of / in-addition-to subclassing 
	 */
	@Override
	public void	execute (String brokerUrl, String destName) throws JMSException {
		boolean	doneInd;
		boolean	excFreeInd;

		doneInd = false;
		excFreeInd = false;

		try {
			this.connect(brokerUrl, destName);

			while ( ! doneInd ) {
				doneInd = this.executeProcessorIteration(this.msgClient);

				if ( this.amqSession.isTransacted() ) {
					this.amqSession.commit();
				}
				else if ( ! this.amqSession.isAutoAcknowledge() ) {
					this.amqSession.acknowledge();
				}
			}

			excFreeInd = true;
		}
		finally {
				//
				// Cleanup.  Log any exceptions on cleanup.  On cleanup exception without a processing
				//  exception, re-throw the cleanup exception.
				//

			try {
				this.disconnect();
			}
			catch ( JMSException exc ) {
				LOG.error("JMS exception on cleanup of processor", exc);

				if ( excFreeInd )
					throw exc;
			}
			catch ( RuntimeException rtExc ) {
				LOG.error("Runtime exception on cleanup of processor", rtExc);

				if ( excFreeInd )
					throw rtExc;
			}
			catch ( Error err ) {
				LOG.error("Error on cleanup of processor", err);

				if ( excFreeInd )
					throw err;
			}
		}
	}

	protected void	connect (String brokerUrl, String destName) throws JMSException {
		this.amqConnection  = this.connectionFactory.createConnection(brokerUrl);
		this.amqSession     = this.sessionFactory.createSession(this.amqConnection);
		this.amqDestination = this.destinationFactory.createDestination(this.amqConnection, this.amqSession,
		                                                                destName);
		this.msgClient      = this.messagingClientFactory.createMessagingClient(this.amqConnection,
		                                                                        this.amqSession,
		                                                                        this.amqDestination);
		this.amqConnection.start();
	}

	protected void	disconnect() throws JMSException {
		if ( this.amqConnection != null ) {
			this.amqConnection.close();
		}

		this.amqConnection  = null;
		this.amqSession     = null;
		this.amqDestination = null;
		this.msgClient      = null;
	}
}
