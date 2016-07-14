package com.heaven7.core.util;

import java.util.Properties;

/**
 * this class is help you to get basic data type easily.
 * Created by heaven7 on 2016/7/14.
 */
public class Properties2 extends Properties {

    public Properties2() {
    }
    public Properties2(Properties properties) {
        super(properties);
    }
//char / short int
    public boolean getBoolean(String key){
        return  Boolean.valueOf(getProperty(key));
    }
    public int getInt(String key) throws NumberFormatException{
        return  Integer.parseInt(getProperty(key));
    }
    public long getLong(String key) throws NumberFormatException{
        return  Long.parseLong(getProperty(key));
    }
    public byte getByte(String key) throws NumberFormatException{
        return  Byte.parseByte(getProperty(key));
    }
    public Double getDouble(String key) throws NumberFormatException{
        return  Double.valueOf(getProperty(key));
    }
    public float getFloat(String key)throws NumberFormatException{
        return  Float.valueOf(getProperty(key));
    }
    public String getString(String key){
        return  getProperty(key);
    }

}
