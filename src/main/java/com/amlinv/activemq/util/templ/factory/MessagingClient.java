package com.amlinv.activemq.util.templ.factory;

import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQMessageProducer;

public interface MessagingClient {
	boolean	isConsumer();
	boolean	isProducer();
	ActiveMQMessageConsumer	getConsumer();
	ActiveMQMessageProducer	getProducer();
}
