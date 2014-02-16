package com.amlinv.activemq.util.templ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class TopicProducerTempl extends ProducerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(TopicProducerTempl.class);

	public TopicProducerTempl (String url, String destName) {
		super(url, destName, ProducerTempl.DestType.TOPIC);
	}
}
