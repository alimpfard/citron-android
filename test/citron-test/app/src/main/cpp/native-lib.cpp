#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_net_anothertest_citron_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject self) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
