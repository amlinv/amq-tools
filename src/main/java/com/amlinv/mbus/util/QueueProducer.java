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
package com.amlinv.mbus.util;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.amlinv.mbus.util.templ.factory.DefaultConnectionFactory;
import com.amlinv.mbus.util.templ.factory.DefaultMessageProducerFactory;
import com.amlinv.mbus.util.templ.factory.DefaultQueueFactory;
import com.amlinv.mbus.util.templ.factory.DefaultSessionFactory;
import com.amlinv.mbus.util.templ.factory.MessagingClient;
import com.amlinv.mbus.util.templ.factory.MessagingClientFactory;
import com.amlinv.mbus.util.templ.impl.ActiveMQProcessorTempl;

public class QueueProducer extends ActiveMQProcessorTempl {
	protected BufferedReader	reader;

	public static void	main (String[] args) {
		QueueProducer	consumerProc;

		consumerProc = new QueueProducer();
		consumerProc.runCmdline(args);
	}

	public void	runCmdline (String[] args) {
		if ( args.length < 2 ) {
			System.out.println("Usage: QueueProducer <broker-url> <dest-name>");
			throw	new Error("invalid command-line arguments");
		}

		this.reader = new BufferedReader(new InputStreamReader(System.in));

		this.setConnectionFactory(new DefaultConnectionFactory());
		this.setSessionFactory(new DefaultSessionFactory(true));
		this.setMessagingClientFactory(new DefaultMessageProducerFactory());
		this.setDestinationFactory(new DefaultQueueFactory());		// TBD: support Topics too

		try {
			this.execute(args[0], args[1]);
		}
		catch ( JMSException jms_exc ) {
			jms_exc.printStackTrace();
		}
		catch ( IOException io_exc ) {
			io_exc.printStackTrace();
		}
	}

	@Override
	public boolean	executeProcessorIteration (MessagingClient client) throws JMSException, IOException {
		ActiveMQTextMessage	msg;
		String			content;

		content = this.getNextMessageContent();

		if ( content == null )
			return	true;

		System.out.println("SENDING MESSAGE: " + content);
		msg = new ActiveMQTextMessage();
		msg.setText(content);
		client.getProducer().send(msg);

		return	false;
	}

	protected String	formatMessage (Message msg) throws JMSException {
		if ( msg instanceof TextMessage ) {
			return	"TEXT [" + ((TextMessage) msg).getText() + "]";
		}

		return	msg.toString();
	}

	protected String	getNextMessageContent () throws IOException {
		return	this.reader.readLine();
 	}
}
