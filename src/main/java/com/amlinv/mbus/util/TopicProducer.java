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

import javax.jms.JMSException;

import com.amlinv.mbus.util.templ.ProduceFromStdin;
import com.amlinv.mbus.util.templ.factory.DefaultConnectionFactory;
import com.amlinv.mbus.util.templ.factory.DefaultMessageProducerFactory;
import com.amlinv.mbus.util.templ.factory.DefaultTopicFactory;
import com.amlinv.mbus.util.templ.factory.DefaultSessionFactory;
import com.amlinv.mbus.util.templ.factory.Processor;
import com.amlinv.mbus.util.templ.factory.ProcessorFactory;
import com.amlinv.mbus.util.templ.impl.ActiveMQEngineImpl;

@BusUtil
public class TopicProducer {
	protected ActiveMQEngineImpl	engine;

	public static void	main (String[] args) {
		TopicProducer	consumerProc;

		consumerProc = new TopicProducer();
		consumerProc.runCmdline(args);
	}

	public void	runCmdline (String[] args) {
		if ( args.length < 2 ) {
			System.out.println("Usage: TopicProducer <broker-url> <dest-name>");
			throw	new Error("invalid command-line arguments");
		}

		this.engine = new ActiveMQEngineImpl();

		this.engine.setConnectionFactory(new DefaultConnectionFactory());
		this.engine.setSessionFactory(new DefaultSessionFactory(true));
		this.engine.setMessagingClientFactory(new DefaultMessageProducerFactory());
		this.engine.setDestinationFactory(new DefaultTopicFactory());

		this.engine.setProcessorFactory(
			new ProcessorFactory() {
				public Processor	createProcessor () {
					return	new ProduceFromStdin();
				}
			});

		try {
			this.engine.execute(args[0], args[1]);
		}
		catch ( Exception exc ) {
			exc.printStackTrace();
		}
	}
}
