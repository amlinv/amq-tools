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
package com.amlinv.mbus.util.templ.factory;

import java.io.IOException;
import javax.jms.JMSException;

import com.amlinv.mbus.util.templ.impl.ActiveMQEngineImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor that simply sits idle in the processing loop until shutdown.
 */
public class IdleProcessor implements Processor {
	private static final Logger	LOG = LoggerFactory.getLogger(IdleProcessor.class);
	protected final Object		shutdownSync = new Object();
	protected boolean		shutdownInd = false;
	protected boolean		shutdownComplete = false;

	/**
	 * Execute one iteration; just loop until shutdown, waiting for notification of shutdown.
 	 */
	public boolean	executeProcessorIteration (ActiveMQEngineImpl activeMQEngine, MessagingClient client) throws JMSException, IOException {
		synchronized ( this.shutdownSync ) {
			while ( ! shutdownInd ) {
				try {
					this.shutdownSync.wait();
				} catch ( InterruptedException intExc ) {
						//
						// This is safe to ignore since the shutdownInd is the key to shutting
						//  down, and it's fine to be interrupted.  With that said, log it at
						//  debug level just in case there's a desire to get more information
						//  on occurrences.
						//

					LOG.debug("idle processor interrupted during wait", intExc);
				}
			}
		}

		return	true;
	}

	/**
	 * Initiate shutdown now.
 	 */
	public void	shutdown () {
		synchronized ( this.shutdownSync ) {
			this.shutdownInd = true;
			this.shutdownSync.notifyAll();
		}
	}
}
