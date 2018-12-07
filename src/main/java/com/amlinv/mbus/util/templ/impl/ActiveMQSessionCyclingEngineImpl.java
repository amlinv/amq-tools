/**
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *   
 *    http://www.apache.org/licenses/LICENSE-2.0
 *   
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.amlinv.mbus.util.templ.impl;

import com.amlinv.mbus.util.templ.ActiveMQEngine;
import com.amlinv.mbus.util.templ.factory.*;
import com.amlinv.prop.util.NamedProperties;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import java.io.IOException;

/**
 * Note that all of the factories must be defined.  However, only the ActiveMQConnection and Processor must be returned
 * non-null from the factories; the rest may be null as long as the processor and other components do not expect
 * otherwise.
 */
public class ActiveMQSessionCyclingEngineImpl implements ActiveMQEngine {
	private static final Logger		LOG = LoggerFactory.getLogger(ActiveMQSessionCyclingEngineImpl.class);

	protected ConnectionFactory		connectionFactory;
	protected DestinationFactory		destinationFactory;
	protected MessagingClientFactory	messagingClientFactory;
	protected ProcessorFactory		processorFactory;
	protected SessionFactory		sessionFactory;
    protected HeaderFactory         headerFactory;

	protected ActiveMQConnection		amqConnection;
	protected ActiveMQSession		amqSession;
	protected ActiveMQDestination		amqDestination;
	protected MessagingClient		msgClient;
	protected Processor			processor;
    private NamedProperties properties = new NamedProperties();

    private ClientIdFactory clientIdFactory;

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
	public void	setProcessorFactory (ProcessorFactory procFactory) {
		this.processorFactory = procFactory;
	}

    @Override
    public void setHeaderFactory (HeaderFactory hdrFactory) {
        this.headerFactory = hdrFactory;
    }

    @Override
	public void	setSessionFactory (SessionFactory sessFactory) {
		this.sessionFactory = sessFactory;
	}

    @Override
    public ConnectionFactory getConnectionFactory () {
        return connectionFactory;
    }

    @Override
    public DestinationFactory getDestinationFactory () {
        return destinationFactory;
    }

    @Override
    public MessagingClientFactory getMessagingClientFactory () {
        return messagingClientFactory;
    }

    @Override
    public ProcessorFactory getProcessorFactory () {
        return processorFactory;
    }

    @Override
    public SessionFactory getSessionFactory () {
        return sessionFactory;
    }

    @Override
    public HeaderFactory getHeaderFactory () {
        return headerFactory;
    }

    public ClientIdFactory getClientIdFactory() {
        return clientIdFactory;
    }

    public void setClientIdFactory(ClientIdFactory clientIdFactory) {
        this.clientIdFactory = clientIdFactory;
    }

    /**
	 * TBD: JMSException handler strategy, processor factory instead of / in-addition-to subclassing 
	 */
	@Override
	public void	execute (String brokerUrl, String destName) throws JMSException, IOException, InterruptedException {
		boolean	doneInd;
		boolean	excFreeInd;
        long    iterationDelay;

        iterationDelay = this.properties.getLongProperty("iterationDelay", 0);

		doneInd = false;
		excFreeInd = false;

		try {
			this.connect(brokerUrl, destName);

			while ( ! doneInd ) {
				doneInd = this.processor.executeProcessorIteration(this, this.msgClient);

				if ( amqSession != null ) {
					if ( this.amqSession.isTransacted() ) {
						this.amqSession.commit();
					}
					else if ( ! this.amqSession.isAutoAcknowledge() ) {
						this.amqSession.acknowledge();
					}
				}

                if ( iterationDelay > 0 ) {
                    Thread.sleep(iterationDelay);
                }

				this.cycleSession(destName);
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
		this.amqConnection.setExceptionListener(new ConnectionEventHandler());

        this.setConnectionClientId(this.amqConnection);

		this.createSession(destName);

		this.amqConnection.start();
	}

	protected void cycleSession(String destName) throws JMSException {
		this.amqSession.close();

		this.createSession(destName);
	}

	protected void createSession(String destName) throws JMSException {
		this.amqSession     = this.sessionFactory.createSession(this.amqConnection);
		this.amqDestination = this.destinationFactory.createDestination(this.amqConnection, this.amqSession,
		                                                                destName);
		this.msgClient      = this.messagingClientFactory.createMessagingClient(this.amqConnection,
				this.amqSession,
				this.amqDestination);
		this.processor      = this.processorFactory.createProcessor();
	}

    protected void  setConnectionClientId (ActiveMQConnection connection) throws JMSException {
        String clientId = null;
        if ( this.clientIdFactory != null ) {
            clientId = this.clientIdFactory.getClientId();
        }

        if ( clientId != null ) {
            connection.setClientID(clientId);
        }
    }

	protected synchronized void	disconnect() throws JMSException {
		if ( this.amqConnection != null ) {
			this.amqConnection.close();
		}

		this.amqConnection  = null;
		this.amqSession     = null;
		this.amqDestination = null;
		this.msgClient      = null;
	}

    public void addProperties (NamedProperties newProperties) {
        this.properties.putAll(newProperties);
    }

    public NamedProperties getProperties () {
        return properties;
    }

    protected class	ConnectionEventHandler implements ExceptionListener {
		public void	onException (JMSException jmsExc) {
			LOG.error("Exception received on connection", jmsExc);

			synchronized ( ActiveMQSessionCyclingEngineImpl.this ) {
				if ( ActiveMQSessionCyclingEngineImpl.this.amqConnection != null ) {
					try {
						ActiveMQSessionCyclingEngineImpl.this.amqConnection.close();
					} catch ( JMSException jmsExc2 ) {
						LOG.info("close AMQ connection on exception failed (this is normal)",
						         jmsExc2);
					}
				}
			}
		}
	}
}
