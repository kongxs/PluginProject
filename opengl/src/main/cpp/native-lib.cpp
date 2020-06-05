#include <jni.h>
#include <string>

#include <EGL/egl.h>
#include <GLES2/gl2.h>


JNIEXPORT void JNICALL surfaceCreated(JNIEnv *env, jobject obj, jint color);
JNIEXPORT void JNICALL surfaceChanged(JNIEnv *env, jobject obj, jint width, jint height);
JNIEXPORT void JNICALL onDrawFrame(JNIEnv *env, jobject obj);


const char *CLASS_NAME = "fu/wanke/opengl/color/NativeColorRenderer";

/**
 * 动态注册
 */
JNINativeMethod methods[] = {
        {"surfaceCreated", "(I)V",  (void *) surfaceCreated},
        {"surfaceChanged", "(II)V", (void *) surfaceChanged},
        {"onDrawFrame",    "()V",   (void *) onDrawFrame}
};


int registerNativeMethod(JNIEnv *pEnv);

void JNI_OnUnload(JavaVM *vm, void *unused) {

}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    //注册方法
    if (registerNativeMethod(env) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}

int registerNativeMethod(JNIEnv *env) {

    jclass cl = env->FindClass(CLASS_NAME);
    if ((env->RegisterNatives(cl, methods, sizeof(methods) / sizeof(methods[0]))) < 0) {
        return -1;
    }

    return 0;
}

JNIEXPORT void JNICALL surfaceCreated(JNIEnv *env, jobject obj, jint color) {
    GLfloat redF = ((color >> 16) & 0xFF) * 1.0f / 255;
    GLfloat greenF = ((color >> 8) & 0xFF) * 1.0f / 255;
    GLfloat blueF = (color & 0xFF) * 1.0f / 255;
    GLfloat alphaF = ((color >> 24) & 0xFF) * 1.0f / 255;
    glClearColor(redF, greenF, blueF, alphaF);
}

JNIEXPORT void JNICALL surfaceChanged(JNIEnv *env, jobject obj, jint width, jint height) {
    glViewport(0, 0, width, height);
}

JNIEXPORT void JNICALL onDrawFrame(JNIEnv *env, jobject obj) {
    //把颜色缓冲区设置为我们预设的颜色
    glClear(GL_COLOR_BUFFER_BIT);
}