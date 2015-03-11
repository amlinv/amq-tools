package com.amlinv.mbus.util.templ.factory;

/**
 * Source of client IDs, in particular for the creation of JMS connections.
 *
 * Created by art on 2/11/15.
 */
public interface ClientIdFactory {
    /**
     * Return the client ID to use, or null if no client ID should be assigned.
     *
     * @return
     */
    String  getClientId();
}
