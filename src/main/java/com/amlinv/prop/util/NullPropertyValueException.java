package com.amlinv.prop.util;

/**
 * Created by art on 8/26/14.
 */
public class NullPropertyValueException extends RuntimeException {
    public NullPropertyValueException (String name) {
        super("property value is null; property=" + name);
    }
}
