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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQMessageConsumer;

import com.amlinv.mbus.util.templ.QueueConsumerTempl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class QueueConsumer extends QueueConsumerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(QueueConsumerTempl.class);

	protected BufferedReader	reader;

	public QueueConsumer (String url, String destName) {
		super(url, destName);
	}

	public static void	main (String[] args) {
		QueueConsumer	mainObj;

		if ( args.length < 2 ) {
			System.err.println("Usage: QueueConsumer <broker-url> <queue-name>");
			System.exit(1);
		}

		try {
			mainObj = new QueueConsumer(args[0], args[1]);
			mainObj.run();
		}
		catch ( Throwable thrown ) {
			thrown.printStackTrace();
		}
	}

	protected void	executeConsumer (ActiveMQMessageConsumer cons) {
		Message	msg;

		try {
			msg = this.consumeMessage();

			while ( msg != null ) {
				System.out.println("RECEIVED MESSAGE: " + this.formatMessage(msg));
				msg = this.consumeMessage();
			}
		}
		catch ( Exception exc ) {
			throw	new RuntimeException(exc);
		}
	}

	protected String	formatMessage (Message msg) throws JMSException {
		if ( msg instanceof TextMessage ) {
			return	"TEXT [" + ((TextMessage) msg).getText() + "]";
		}

		return	msg.toString();
	}
}
