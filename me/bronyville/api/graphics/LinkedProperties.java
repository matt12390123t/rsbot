package me.bronyville.api.graphics;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;

public class LinkedProperties extends Properties {
    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

    @Override
    public Enumeration<Object> keys() {
        return Collections.enumeration(keys);
    }

    @Override
    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }
}