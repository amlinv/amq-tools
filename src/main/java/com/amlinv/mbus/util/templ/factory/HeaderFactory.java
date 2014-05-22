package com.amlinv.mbus.util.templ.factory;

import java.util.List;

/**
 * Created by art on 5/20/14.
 */
public interface HeaderFactory {
    List<String>    getHeaderNames();
    Object          getHeaderValue(String hdrName);
}
