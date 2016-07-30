package com.heaven7.memory.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * optimize: get filed value( how to optimize reflect Method call? Map?  )
 * Unsafe:
 *  public native Object getObject(Object obj, long offset);
 *  public native void putObject(Object obj, long offset, Object newValue);
 *  public long objectFieldOffset(Field field)
 * Created by heaven7 on 2016/7/30.
 */
//TODO not done
public class UnsafeFactory {

    private static final Object sUnsafe;
    static{
        Object unsafe = null;
        try {
            Field f = Class.forName("sun.misc.Unsafe").getDeclaredField("THE_ONE");
            f.setAccessible(true);
            unsafe = f.get(null);
        }catch (Exception e){
            unsafe = getUnsafeObject2();
        }finally {
            sUnsafe = unsafe;
        }
    }

    private static Object getUnsafeObject2()  {
        Object unsafe = null;
        try {
            final Class<?> clazz = Class.forName("sun.misc.Unsafe");
            final Field[] fields = clazz.getDeclaredFields();
            if(fields!=null && fields.length > 0){
                int modifier;
                for(Field f : fields){
                    f.setAccessible(true);
                    modifier = f.getModifiers();
                    if(Modifier.isStatic(modifier) && Modifier.isFinal(modifier)){
                        unsafe = f.get(null);
                        if(unsafe.getClass() == clazz){
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return unsafe;
    }

    public interface IUnsafe{

    }

}

