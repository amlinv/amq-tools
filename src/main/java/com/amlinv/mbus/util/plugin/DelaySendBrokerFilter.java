package com.amlinv.mbus.util.plugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.command.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by art on 9/6/14.
 */
public class DelaySendBrokerFilter extends BrokerFilter {
    private static final Logger LOG = LoggerFactory.getLogger(DelaySendBrokerFilter.class);

    private long                sendDelay = DelaySendBrokerPlugin.DEFAULT_SEND_DELAY;

    public DelaySendBrokerFilter (Broker next) {
        super(next);
    }

    public long getSendDelay () {
        return sendDelay;
    }

    public void setSendDelay (long newSendDelay) {
        sendDelay = newSendDelay;
    }

    @Override
    public void send (ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
        if ( sendDelay > 0 ) {
            try {
                Thread.sleep(sendDelay);
            } catch ( InterruptedException intExc ) {
                LOG.info("send delay interrupted; delay aborted", intExc);
            }
        }

        super.send(producerExchange, messageSend);
    }
}
