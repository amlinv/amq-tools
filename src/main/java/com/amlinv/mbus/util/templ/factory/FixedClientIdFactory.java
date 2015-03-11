package com.amlinv.mbus.util.templ.factory;

/**
 * ClientId factory that returns a fixed client ID.
 *
 * Created by art on 2/11/15.
 */
public class FixedClientIdFactory implements ClientIdFactory {
    private final String  clientId;

    public FixedClientIdFactory(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }
}
