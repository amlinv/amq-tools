package com.amlinv.activemq.util.templ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class QueueProducerTempl extends ProducerTempl
{
	private static final Logger	LOG = LoggerFactory.getLogger(QueueProducerTempl.class);

	public QueueProducerTempl (String url, String destName) {
		super(url, destName, ProducerTempl.DestType.QUEUE);
	}
}
