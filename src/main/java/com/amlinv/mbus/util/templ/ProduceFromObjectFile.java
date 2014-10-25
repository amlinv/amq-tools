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
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.wireformat.WireFormat;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.*;

public class ProduceFromObjectFile implements Processor {
	private ObjectInputStream   dataInput;
    private WireFormat          unmarshaller = new OpenWireFormat();

	public ProduceFromObjectFile (InputStream inStream) throws IOException {
        this.dataInput = new ObjectInputStream(inStream);
	}

	@Override
	public boolean	executeProcessorIteration (ActiveMQEngineImpl activeMQEngine, MessagingClient client)
            throws JMSException, IOException {

        Message msg;
		String	content;

		msg = this.getNextMessage();

		if ( msg == null )
			return	true;

		System.out.println("SENDING: " + msg);

        // TBD: link to header factory and add headers
        HeaderFactory hdrFactory = activeMQEngine.getHeaderFactory();
        if ( hdrFactory != null ) {
            for ( String hdr : hdrFactory.getHeaderNames() ) {
                msg.setObjectProperty(hdr, hdrFactory.getHeaderValue(hdr));
            }
        }

		client.getProducer().send(msg);

		return	false;
	}

	protected Message   getNextMessage () throws IOException {
        Object  cmd;

        try {
            cmd = this.readNextCommand();
            while ( ( cmd != null ) && ( ! ( cmd instanceof Message ) ) ) {
                cmd = this.readNextCommand();
            }

            return (Message) cmd;
        } catch ( EOFException eofExc ) {
            return  null;
        }
 	}

    protected Object    readNextCommand () throws IOException {
        try {
            Object  data;
            Object  cmd = null;

            data = this.dataInput.readObject();

            if ( data instanceof byte[] ) {
                cmd = unmarshaller.unmarshal(new ByteSequence((byte[]) data));
            } else if ( data != null ) {
                throw new RuntimeException("unexpected object read from file: " + data.getClass().getName());
            }

            return cmd;
        } catch ( ClassNotFoundException cnfExc ) {
            throw new RuntimeException("read unexpected object from file", cnfExc);
        }
    }
}
