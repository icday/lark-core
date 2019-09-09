package com.daiyc.lark.core.session;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daiyc
 */
@SuppressWarnings("unchecked")
public class Context {
    private static ThreadLocal<Map<String, Object>> map = ThreadLocal.withInitial(HashMap::new);

    public static <T> void setValue(String key, T value) {
        map.get().put(key, value);
    }

    public static <T> T getValue(String key) {
        return (T) map.get().get(key);
    }

    public static <T> T getValue(String key, T defaultValue) {
        return (T) map.get().getOrDefault(key, defaultValue);
    }

    public static void remove(String key) {
        map.get().remove(key);
    }

    public static void removeAll() {
        map.get().clear();
    }
}
