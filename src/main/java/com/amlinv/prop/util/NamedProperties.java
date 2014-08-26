package com.amlinv.prop.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Property store with String-only key values and automatic conversion between types, and safe reading of typed values
 * with default values.
 *
 * Created by art on 8/26/14.
 */
public class NamedProperties {
    private Map<String, Object> properties = new TreeMap<String, Object>();

    public void setProperty (String name, Object value) {
        this.properties.put(name, value);
    }

    public Object   put (String name, Object value) {
        return  this.properties.put(name, value);
    }

    public Object   get (String name) {
        return  this.properties.get(name);
    }

    public void putAll (NamedProperties other) {
        for ( Map.Entry<String, Object> oneEntry : other.entrySet() ) {
            this.put(oneEntry.getKey(), oneEntry.getValue());
        }
    }

    public int size () {
        return this.properties.size();
    }

    public boolean isEmpty () {
        return this.properties.isEmpty();
    }

    public boolean containsProperty (String name) {
        return this.properties.containsKey(name);
    }

    public void clear () {
        this.properties.clear();
    }

    public Set<String> keySet () {
        return  this.properties.keySet();
    }

    public Collection<Object> values () {
        return this.properties.values();
    }

    public Set<Map.Entry<String, Object>> entrySet () {
        return this.properties.entrySet();
    }

    public String   getStringProperty (String name) {
        Object val = this.properties.get(name);
        String result = null;

        if ( val instanceof String ) {
            result = (String) val;
        } else if ( val != null ) {
            result = val.toString();
        }

        return  result;
    }

    public int  getIntegerProperty (String name, int defaultValue) {
        Object val = this.properties.get(name);
        int result;

        try {
            result = this.getIntegerProperty(name);
        } catch ( Exception exc ) {
            result = defaultValue;
        }

        return  result;
    }

    public long getLongProperty (String name, long defaultValue) {
        Object val = this.properties.get(name);
        long result;

        try {
            result = this.getIntegerProperty(name);
        } catch ( Exception exc ) {
            result = defaultValue;
        }

        return  result;
    }

    public short    getShortProperty (String name, short defaultValue) {
        Object val = this.properties.get(name);
        short result;

        try {
            result = this.getShortProperty(name);
        } catch ( Exception exc ) {
            result = defaultValue;
        }

        return  result;
    }

    public int  getIntegerProperty (String name) {
        Object val = this.properties.get(name);
        int result;

        if ( val != null ) {
            if ( val instanceof Number ) {
                result = ( (Number) val ).intValue();
            } else {
                result = Integer.valueOf(val.toString());
            }
        } else {
            throw new NullPropertyValueException(name);
        }

        return  result;
    }

    public long  getLongProperty (String name) {
        Object val = this.properties.get(name);
        long result;

        if ( val != null ) {
            if ( val instanceof Number ) {
                result = ( (Number) val ).longValue();
            } else {
                result = Long.valueOf(val.toString());
            }
        } else {
            throw new NullPropertyValueException(name);
        }

        return  result;
    }

    public short    getShortProperty (String name) {
        Object val = this.properties.get(name);
        short result;

        if ( val != null ) {
            if ( val instanceof Number ) {
                result = ( (Number) val ).shortValue();
            } else {
                result = Short.valueOf(val.toString());
            }
        } else {
            throw new NullPropertyValueException(name);
        }

        return  result;
    }
}
