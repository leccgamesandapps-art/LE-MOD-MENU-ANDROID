#include <jni.h>
#include <string>
#include <vector>
#include <android/log.h>
#include <unistd.h>
#include <pthread.h>

#define LOG_TAG "LEModMenu"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Simple feature list (you expand this)
static const char* features[] = {
    "Category_Player",
    "Toggle_God Mode",
    "Toggle_Unlimited Health",
    "Toggle_No Damage",
    "Category_Currency",
    "Toggle_Unlimited Coins",
    "Toggle_Unlimited Gems",
    "Category_Misc",
    "Toggle_FPS Unlock",
    "Button_Teleport Home",
    NULL
};

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_lemodmenu_FloatingModMenuService_getFeatureList(JNIEnv *env, jobject thiz) {
    int count = 0;
    while (features[count] != NULL) count++;

    jobjectArray ret = env->NewObjectArray(count, env->FindClass("java/lang/String"), nullptr);
    for (int i = 0; i < count; i++) {
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));
    }
    return ret;
}

extern "C" JNIEXPORT void JNICALL
Java_com_lemodmenu_FloatingModMenuService_Changes(JNIEnv *env, jobject thiz,
                                                  jint featNum, jboolean booleanValue,
                                                  jint intValue, jstring strValue) {
    LOGI("Feature %d changed. Bool: %d  Int: %d", featNum, booleanValue, intValue);

    // Example handling
    switch (featNum) {
        case 1: // God Mode
            if (booleanValue) {
                LOGI("God Mode ON");
                // TODO: apply your patch / hook here
            } else {
                LOGI("God Mode OFF");
            }
            break;
        case 2: // Unlimited Health
            // TODO
            break;
        // Add more cases matching your feature list order
        default:
            break;
    }
}

// Optional background thread for continuous mods
void* hack_thread(void*) {
    LOGI("LE Mod Menu hack thread started");
    while (true) {
        // Your continuous patches / memory writes go here
        // Example: if (godMode) MemoryPatch(...);
        sleep(1);
    }
    return nullptr;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGI("LE MOD MENU V1.0 native library loaded");
    // pthread_t t;
    // pthread_create(&t, nullptr, hack_thread, nullptr);
    return JNI_VERSION_1_6;
}
