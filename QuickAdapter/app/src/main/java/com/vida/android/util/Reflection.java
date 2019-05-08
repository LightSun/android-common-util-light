package com.vida.android.util;

/**
 * Created by heaven7 on 2019/5/8.
 */
public class Reflection {
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("music_annotator");
    }

    //jni impl is build by qtcreator. see src of qt/music_annotator
    public native String hello();
}
