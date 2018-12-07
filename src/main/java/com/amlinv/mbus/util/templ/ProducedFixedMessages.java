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

import com.amlinv.mbus.util.templ.factory.HeaderFactory;
import com.amlinv.mbus.util.templ.factory.MessagingClient;
import com.amlinv.mbus.util.templ.factory.Processor;
import com.amlinv.mbus.util.templ.impl.ActiveMQEngineImpl;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.util.ByteSequence;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Arrays;

public class ProducedFixedMessages implements Processor {
    private int messageSize;
    private long maxMessageCount;
    private long messageCount;

	public ProducedFixedMessages(int messageSize, long maxMessageCount) {
        this.messageSize = messageSize;
        this.maxMessageCount = maxMessageCount;
	}

	@Override
	public boolean	executeProcessorIteration (ActiveMQEngine activeMQEngine, MessagingClient client)
            throws JMSException, IOException {

		ActiveMQBytesMessage msg;
		byte[]               content;

		content = this.getNextMessageContent();

		if ( content == null )
			return	true;

        this.messageCount++;

		System.out.println("SENDING FIXED BYTE MESSAGE #" + this.messageCount + " OF LENGTH " + content.length);
		msg = new ActiveMQBytesMessage();
		msg.setContent(new ByteSequence(content));

        HeaderFactory hdrFactory = activeMQEngine.getHeaderFactory();
        if ( hdrFactory != null ) {
            for ( String hdr : hdrFactory.getHeaderNames() ) {
                msg.setObjectProperty(hdr, hdrFactory.getHeaderValue(hdr));
            }
        }

		client.getProducer().send(msg);

		return	false;
	}

	protected byte[]	getNextMessageContent () throws IOException {
        byte[] result;

        if ( this.messageCount < this.maxMessageCount ) {
            result = new byte[this.messageSize];
            Arrays.fill(result, (byte) '-');
        } else {
            result = null;
        }

		return  result;
 	}
}
