#include "music_annotator.h"

Music_annotator::Music_annotator()
{


}

extern "C" JNIEXPORT jstring JNICALL Java_com_vida_android_util_Reflection_hello(JNIEnv *env, jobject obj) {

    std::string hello = "Hello from QT++";
    return env->NewStringUTF(hello.c_str());
}
