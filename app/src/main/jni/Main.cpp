#include <jni.h>
#include <string>
#include <android/log.h>
#include <unistd.h>
#include <pthread.h>

#define LOG_TAG "LEModMenu"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// Feature list - same style as LGL
// Category_  = not counted as feature
// Toggle_    = switch
// Button_    = button
// SeekBar_   = slider (name_min_max)

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

    jobjectArray ret = (jobjectArray) env->NewObjectArray(count, env->FindClass("java/lang/String"), NULL);
    for (int i = 0; i < count; i++) {
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));
    }
    return ret;
}

extern "C" JNIEXPORT void JNICALL
Java_com_lemodmenu_FloatingModMenuService_Changes(JNIEnv *env, jobject thiz,
                                                  jint featNum, jboolean booleanValue,
                                                  jint intValue, jstring strValue) {
    LOGI("Feature changed -> num: %d | bool: %d | int: %d", featNum, booleanValue, intValue);

    switch (featNum) {
        case 1: // God Mode
            if (booleanValue) {
                LOGI("God Mode ENABLED");
                // TODO: Your patch / hook here
            } else {
                LOGI("God Mode DISABLED");
            }
            break;

        case 2: // Unlimited Health
            // TODO
            break;

        case 3: // No Damage
            // TODO
            break;

        case 5: // Unlimited Coins
            // TODO
            break;

        default:
            break;
    }
}

// Background thread example
void* hack_thread(void*) {
    LOGI("LE Mod Menu hack thread started");
    while (true) {
        // Continuous mods here
        sleep(2);
    }
    return NULL;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGI("===== LE MOD MENU V1.0 Native Loaded =====");
    // Uncomment to start background thread
    // pthread_t t;
    // pthread_create(&t, NULL, hack_thread, NULL);
    return JNI_VERSION_1_6;
}
