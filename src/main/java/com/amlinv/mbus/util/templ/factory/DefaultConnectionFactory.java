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

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class DefaultConnectionFactory implements ConnectionFactory {
	public ActiveMQConnection	createConnection (String brokerUrl) throws JMSException {
		ActiveMQConnectionFactory	factory;

		factory = new ActiveMQConnectionFactory(brokerUrl);

		this.configureConnectionFactory(factory);

		return	(ActiveMQConnection) factory.createConnection();
	}

	/**
	 * Hook to allow subclasses to configure the connection factory with settings such as connection listener,
	 * user name, and password.
	 */
	protected void	configureConnectionFactory (ActiveMQConnectionFactory connFactory) {
	}
}
