package com.socialbehavior.socialbehaviormod.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Serializer {
    static public Map<String, Object> object2Map(Object object) {
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        Map<String, Object> ret = new HashMap<>();
        for (Field field : fields) {
            String attributeName = field.getName();
            try {
                Object valObject = field.get(object);
                ret.put(attributeName, valObject);
            } catch (Exception ignored) {
            }
        }
        return ret;
    }
}
