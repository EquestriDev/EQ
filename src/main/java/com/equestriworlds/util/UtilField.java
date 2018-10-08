/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.util;

import java.lang.reflect.Field;

public class UtilField {
    public static void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(packet, value);
        }
        catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        field.setAccessible(!field.isAccessible());
    }

    static void setFinalStatic(Field field, Object value) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.set(field, field.getModifiers() & -17);
        field.set(null, value);
        field.setAccessible(!field.isAccessible());
    }

    public static Field getField(Class<?> classs, String fieldname) {
        try {
            return classs.getDeclaredField(fieldname);
        }
        catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Object o = null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }
}
