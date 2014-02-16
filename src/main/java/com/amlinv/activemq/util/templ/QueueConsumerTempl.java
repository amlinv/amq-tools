package com.amlinv.activemq.util.templ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class QueueConsumerTempl extends ConsumerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(QueueConsumerTempl.class);

	public QueueConsumerTempl (String url, String destName) {
		super(url, destName, ConsumerTempl.DestType.QUEUE);
	}
}
