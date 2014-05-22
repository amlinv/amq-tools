package com.amlinv.mbus.util.templ.factory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by art on 5/20/14.
 */
public class DefaultHeaderFactory implements HeaderFactory {
    private Map<String, Object> headers = new HashMap<String, Object>();

    public void addHeaders (Map<String, Object> newHeaders) {
        this.headers.putAll(newHeaders);
    }

    public void addHeader (String hdrName, Object value) {
        this.headers.put(hdrName, value);
    }

    public void setHeaders (Map<String, Object> replacementHeaders) {
        this.headers = replacementHeaders;
    }

    public Map<String, Object>  getHeaders () {
        return  this.headers;
    }

    @Override
    public List<String> getHeaderNames () {
        return  new LinkedList<String>(this.headers.keySet());
    }

    @Override
    public Object getHeaderValue (String hdrName) {
        return this.headers.get(hdrName);
    }
}
