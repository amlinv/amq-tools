package com.amlinv.activemq.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.apache.activemq.ActiveMQMessageProducer;

import com.amlinv.activemq.util.templ.QueueProducerTempl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class QueueProducer extends QueueProducerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(QueueProducerTempl.class);

	protected BufferedReader	reader;

	public QueueProducer (String url, String destName) {
		super(url, destName);
	}

	public static void	main (String[] args) {
		QueueProducer	mainObj;

		if ( args.length < 2 ) {
			System.err.println("Usage: QueueProducer <broker-url> <queue-name>");
			System.exit(1);
		}

		try {
			mainObj = new QueueProducer(args[0], args[1]);
			mainObj.run();
		}
		catch ( Throwable thrown ) {
			thrown.printStackTrace();
		}
	}

	protected void	executeProducer (ActiveMQMessageProducer prod) {
		String	content;

		try {
			this.reader = new BufferedReader(new InputStreamReader(System.in));

			content = this.getNextMessageContent();

			while ( content != null ) {
				System.out.println("SENDING MESSAGE: " + content);
				this.produceMessage(content);
				content = this.getNextMessageContent();
			}
		}
		catch ( Exception exc ) {
			throw	new RuntimeException(exc);
		}
	}

	protected String	getNextMessageContent () throws IOException {
		return	this.reader.readLine();
	}
}