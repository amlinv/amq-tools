package com.amlinv.activemq.util.templ;

import javax.jms.JMSException;

import com.amlinv.activemq.util.templ.factory.ConnectionFactory;
import com.amlinv.activemq.util.templ.factory.DestinationFactory;
import com.amlinv.activemq.util.templ.factory.MessagingClientFactory;
import com.amlinv.activemq.util.templ.factory.SessionFactory;

public interface ActiveMQProcessor {
	void	setConnectionFactory(ConnectionFactory connFactory);
	void	setDestinationFactory(DestinationFactory destFactory);
	void	setMessagingClientFactory(MessagingClientFactory clientFactory);
	void	setSessionFactory(SessionFactory sessFactory);
	void	execute(String brokerUrl, String destName) throws JMSException;
}
