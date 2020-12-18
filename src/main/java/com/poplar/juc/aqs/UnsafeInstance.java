package com.poplar.juc.aqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Create BY poplar ON 2020/12/8
 * 通过反射获取unsafe
 */
public class UnsafeInstance {

    public static Unsafe reflectGetUnsafe() {
        try {
            Class<?> clazz = Unsafe.class;
            Field f = clazz.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(clazz);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
