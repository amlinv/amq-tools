package com.amlinv.activemq.util.templ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class TopicConsumerTempl extends ConsumerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(TopicConsumerTempl.class);

	public TopicConsumerTempl (String url, String destName) {
		super(url, destName, ConsumerTempl.DestType.TOPIC);
	}
}
