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

import com.amlinv.mbus.util.templ.ConsumeToStdout;
import com.amlinv.mbus.util.templ.factory.*;
import com.amlinv.mbus.util.templ.impl.ActiveMQSessionCyclingEngineImpl;

/**
 * Queue Consumer loop that cycles sessions on every message.
 */
@BusUtil
public class QueueConsumerCyclingSession {
	protected ActiveMQSessionCyclingEngineImpl	engine;

	public static void	main (String[] args) {
		QueueConsumerCyclingSession consumerProc;

		consumerProc = new QueueConsumerCyclingSession();
		consumerProc.runCmdline(args);
	}

	public void	runCmdline (String[] args) {
		if ( args.length < 2 ) {
			System.out.println("Usage: QueueConsumerCyclingSession <broker-url> <dest-name>");
			throw	new Error("invalid command-line arguments");
		}

		this.engine = new ActiveMQSessionCyclingEngineImpl();

		this.engine.setConnectionFactory(new DefaultConnectionFactory());
		this.engine.setSessionFactory(new DefaultSessionFactory(true));
		this.engine.setMessagingClientFactory(new DefaultMessageConsumerFactory());
		this.engine.setDestinationFactory(new DefaultQueueFactory());
		this.engine.setProcessorFactory(
			new ProcessorFactory() {
				public Processor	createProcessor () {
					return	new ConsumeToStdout();
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
