package com.amlinv.mbus.util.plugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.BrokerPlugin;

/**
 * Created by art on 9/6/14.
 */
public class DelaySendBrokerPlugin implements BrokerPlugin {
    public static final long    DEFAULT_SEND_DELAY = 1000;

    private long                sendDelay = DEFAULT_SEND_DELAY;

    public long getSendDelay () {
        return sendDelay;
    }

    public void setSendDelay (long newSendDelay) {
        sendDelay = newSendDelay;
    }

    @Override
    public Broker installPlugin (Broker broker) throws Exception {
        DelaySendBrokerFilter    filter;

        filter = new DelaySendBrokerFilter(broker);
        filter.setSendDelay(this.sendDelay);

        return filter;
    }
}
