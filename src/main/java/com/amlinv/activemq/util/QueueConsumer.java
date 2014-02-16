package com.amlinv.activemq.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQMessageConsumer;

import com.amlinv.activemq.util.templ.QueueConsumerTempl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class QueueConsumer extends QueueConsumerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(QueueConsumerTempl.class);

	protected BufferedReader	reader;

	public QueueConsumer (String url, String destName) {
		super(url, destName);
	}

	public static void	main (String[] args) {
		QueueConsumer	mainObj;

		if ( args.length < 2 ) {
			System.err.println("Usage: QueueConsumer <broker-url> <queue-name>");
			System.exit(1);
		}

		try {
			mainObj = new QueueConsumer(args[0], args[1]);
			mainObj.run();
		}
		catch ( Throwable thrown ) {
			thrown.printStackTrace();
		}
	}

	protected void	executeConsumer (ActiveMQMessageConsumer cons) {
		Message	msg;

		try {
			msg = this.consumeMessage();

			while ( msg != null ) {
				System.out.println("RECEIVED MESSAGE: " + this.formatMessage(msg));
				msg = this.consumeMessage();
			}
		}
		catch ( Exception exc ) {
			throw	new RuntimeException(exc);
		}
	}

	protected String	formatMessage (Message msg) throws JMSException {
		if ( msg instanceof TextMessage ) {
			return	"TEXT [" + ((TextMessage) msg).getText() + "]";
		}

		return	msg.toString();
	}
}
