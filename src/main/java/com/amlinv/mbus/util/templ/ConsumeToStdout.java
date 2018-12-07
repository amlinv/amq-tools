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

import javax.jms.JMSException;
import javax.jms.Message;

import com.amlinv.mbus.util.MessageUtil;
import com.amlinv.mbus.util.templ.factory.MessagingClient;
import com.amlinv.mbus.util.templ.factory.Processor;
import com.amlinv.mbus.util.templ.impl.NoTimeoutStrategy;

public class ConsumeToStdout implements Processor {
	protected TimeoutStrategy	timeoutStrategy = new NoTimeoutStrategy();

	public void	setTimeoutStrategy (TimeoutStrategy strategy) {
		this.timeoutStrategy = strategy;
	}

	@Override
	public boolean	executeProcessorIteration (ActiveMQEngine activeMQEngine, MessagingClient client) throws JMSException {
		Message	msg;
		boolean	done = false;

		// TBD: abstract this -- perhaps using a timeout helper that takes a runnable for the actual operation
		// Like done = TimeoutStrategyUtil.execWithTimeoutStrategy(this.timeoutStrategy, new Runnable() {
		//	public void	run () { ... } });
		if ( this.timeoutStrategy.isTimeoutEnabled() ) {
			msg = client.getConsumer().receive(this.timeoutStrategy.getTimeout());
			if ( msg == null ) {
				done = this.timeoutStrategy.shouldTerminate();
			}
		} else {
			msg = client.getConsumer().receive();
		}

		if ( msg != null ) {
			System.out.println(MessageUtil.formatMessage(msg));
		}

		return	done;
	}
}
