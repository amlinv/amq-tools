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
import java.util.LinkedList;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

import com.amlinv.mbus.util.templ.ConsumeToStdout;
import com.amlinv.mbus.util.templ.factory.DefaultConnectionFactory;
import com.amlinv.mbus.util.templ.factory.DestinationFactory;
import com.amlinv.mbus.util.templ.factory.IdleProcessor;
import com.amlinv.mbus.util.templ.factory.MessagingClient;
import com.amlinv.mbus.util.templ.factory.MessagingClientFactory;
import com.amlinv.mbus.util.templ.factory.Processor;
import com.amlinv.mbus.util.templ.factory.ProcessorFactory;
import com.amlinv.mbus.util.templ.factory.SessionFactory;
import com.amlinv.mbus.util.templ.impl.ActiveMQEngineImpl;

@BusUtil
public class FloodConnections {
	protected ActiveMQEngineImpl		engine;
	protected List<ConnectionRunnerThread>	procThreads = new LinkedList<ConnectionRunnerThread>();

	public static void	main (String[] args) {
		FloodConnections	floodProc;

		floodProc = new FloodConnections();
		floodProc.runCmdline(args);
	}

	public void	runCmdline (String[] args) {
		if ( args.length < 2 ) {
			System.out.println("Usage: FloodConnections <broker-url> <connection-count>");
			throw	new Error("invalid command-line arguments");
		}

		try {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void	run () {
					FloodConnections.this.shutdown();
				}
			});

			int count = Integer.parseInt(args[1]);
			int iter = 0;
			while ( iter < count ) {
				System.out.println("Standing up connection " + iter);
				this.startOneConnection(args[0], "unused");
				iter++;
			}
		} catch ( Exception exc ) {
			exc.printStackTrace();
		}
	}

	protected void	shutdown () {
		for ( ConnectionRunnerThread oneProcThread : procThreads ) {
			oneProcThread.initiateShutdown();
		}

		for ( ConnectionRunnerThread oneProcThread : procThreads ) {
			oneProcThread.waitForShutdown();
		}
	}

	protected void	startOneConnection (final String url, final String destName) {
		final ConnectionRunnerThread	runnerThread = new ConnectionRunnerThread(url, destName);
		this.procThreads.add(runnerThread);
		runnerThread.start();
	}

	protected void	runOneConnection (String url, String destName, ConnectionRunnerThread runnerThread) {
		ActiveMQEngineImpl	engine;
		final IdleProcessor	processor = new IdleProcessor();

		runnerThread.setIdleProcessor(processor);

		engine = new ActiveMQEngineImpl();

		engine.setConnectionFactory(new DefaultConnectionFactory());
		engine.setSessionFactory(new SessionFactory() {
			public ActiveMQSession	createSession (ActiveMQConnection conn) throws JMSException {
				return	null;
			}
		});
		engine.setMessagingClientFactory(new MessagingClientFactory() {
			public MessagingClient	createMessagingClient (ActiveMQConnection conn, ActiveMQSession sess,
						                       ActiveMQDestination dest)
			throws JMSException {
				return	null;
			}
		});
		engine.setDestinationFactory(new DestinationFactory() {
			public ActiveMQDestination	createDestination (ActiveMQConnection conn,
							                   ActiveMQSession sess, String name)
			throws JMSException
			{
				return	null;
			}
		});

		engine.setProcessorFactory(
			new ProcessorFactory() {
				public Processor	createProcessor () {
					return	processor;
				}
			});

		try {
			engine.execute(url, destName);
		}
		catch ( Exception exc ) {
			exc.printStackTrace();
		}
	}

	protected class	ConnectionRunnerThread extends Thread {
		protected String	url;
		protected String	destName;
		protected IdleProcessor	idleProc;

		public ConnectionRunnerThread (String inUrl, String inDestName) {
			this.url      = inUrl;
			this.destName = inDestName;
		}

		public void	run () {
			try {
				FloodConnections.this.runOneConnection(this.url, this.destName, this);
			} catch ( Throwable thrown ) {
				System.out.println("Error on connection");
				thrown.printStackTrace();
			}
		}

		public void	setIdleProcessor (IdleProcessor proc) {
			this.idleProc = proc;
		}

		public void	initiateShutdown() {
			this.idleProc.shutdown();
		}

		public void	waitForShutdown () {
			try {
				this.join();
			} catch ( InterruptedException intExc ) {
				// LOG.debug("interrupted waiting for shutdown", intExc);
			}
		}
	}
}
