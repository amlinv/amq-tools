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
package com.amlinv.mbus.util.templ;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class ConsumerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(ConsumerTempl.class);

	protected abstract void			executeConsumer(ActiveMQMessageConsumer cons);

	protected String			brokerUrl;
	protected String			destName;
	protected ActiveMQConnection		conn;
	protected ActiveMQSession		sess;
	protected ActiveMQDestination		dest;
	protected ActiveMQMessageConsumer	cons;

	public enum DestType {
		QUEUE,
		TOPIC
	} ;

	/**
	 *
	 */
	public ConsumerTempl (String url, String dName, DestType type) {
		this.brokerUrl = url;
		this.destName = dName;

		if ( type == DestType.QUEUE )
			this.dest = new ActiveMQQueue(this.destName);
		else
			this.dest = new ActiveMQTopic(this.destName);
	}

	public void run () throws JMSException, URISyntaxException, IOException {
		this.connect();
		this.executeConsumer(this.cons);
		this.disconnect();
	}

	protected Message	consumeMessage () throws JMSException {
		return	this.cons.receive();
	}

	protected Message	consumeMessage (long timeout) throws JMSException {
		return	this.cons.receive(timeout);
	}

	/**
	 * Create a JMS connection for Queue operations.
	 */
	protected void	connect () throws JMSException, URISyntaxException {
		this.createConnection();
		this.createSession();
		this.createConsumer();
	}

	/**
	 * Create the JMS Connection for Queue operations.
	 */
	protected void	createConnection () throws JMSException, URISyntaxException {
		this.conn = ActiveMQConnection.makeConnection(brokerUrl);
	}

	/**
	 * Create the JMS Session for Queue operations.
	 */
	protected void	createSession () throws JMSException {
		this.sess = (ActiveMQSession) this.conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	}

	/**
	 * Create the JMS Consumer for Queue operations.
	 */
	protected void	createConsumer () throws JMSException {
		this.cons = (ActiveMQMessageConsumer) this.sess.createConsumer(this.dest);
	}

	/**
	 * Close the JMS connection used for Queue operations.
	 */
	protected void	disconnect () throws JMSException {
		this.conn.close();
		this.conn = null;
		this.sess = null;
		this.cons = null;
	}
}
