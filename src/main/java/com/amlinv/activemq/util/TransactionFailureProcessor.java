package com.amlinv.activemq.util;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;

import com.amlinv.activemq.util.templ.factory.DefaultConnectionFactory;
import com.amlinv.activemq.util.templ.factory.DefaultMessageConsumerFactory;
import com.amlinv.activemq.util.templ.factory.DefaultQueueFactory;
import com.amlinv.activemq.util.templ.factory.DefaultSessionFactory;
import com.amlinv.activemq.util.templ.factory.MessagingClient;
import com.amlinv.activemq.util.templ.factory.MessagingClientFactory;
import com.amlinv.activemq.util.templ.impl.ActiveMQProcessorTempl;

public class TransactionFailureProcessor extends ActiveMQProcessorTempl {

	public boolean	executeProcessorIteration (MessagingClient client) throws JMSException {
		Message	msg;

		msg = client.getConsumer().receive();
		throw	new JMSException("processor failure with" + ( ( msg == null ) ? "out" : "" ) + " message");
	}

	public static void	main (String[] args) {
		TransactionFailureProcessor	failureProc;

		failureProc = new TransactionFailureProcessor();
		failureProc.runCmdline(args);
	}

	public void	runCmdline (String[] args) {
		if ( args.length < 2 ) {
			System.out.println("Usage: TransactionFailureProcessor <broker-url> <dest-name>");
			throw	new Error("invalid command-line arguments");
		}

		this.setConnectionFactory(new DefaultConnectionFactory());
		this.setSessionFactory(new DefaultSessionFactory(true));
		this.setMessagingClientFactory(new DefaultMessageConsumerFactory());
		this.setDestinationFactory(new DefaultQueueFactory());		// TBD: support Topics too

		try {
			this.execute(args[0], args[1]);
		}
		catch ( JMSException jms_exc ) {
			jms_exc.printStackTrace();
		}
	}
}
