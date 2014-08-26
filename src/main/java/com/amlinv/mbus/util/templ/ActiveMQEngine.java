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
import javax.jms.JMSException;

import com.amlinv.mbus.util.templ.factory.*;

public interface ActiveMQEngine {
	void	setConnectionFactory(ConnectionFactory connFactory);
	void	setDestinationFactory(DestinationFactory destFactory);
	void	setMessagingClientFactory(MessagingClientFactory clientFactory);
	void	setSessionFactory(SessionFactory sessFactory);
	void	setProcessorFactory(ProcessorFactory procFactory);
    void    setHeaderFactory(HeaderFactory hdrFactory);

    ConnectionFactory       getConnectionFactory();
    DestinationFactory      getDestinationFactory();

    MessagingClientFactory  getMessagingClientFactory();
    ProcessorFactory        getProcessorFactory();
    SessionFactory          getSessionFactory();
    HeaderFactory           getHeaderFactory();

	void	execute(String brokerUrl, String destName) throws JMSException, IOException, InterruptedException;


}
