// Utils placeholder
// Add KittyMemory wrappers, obfuscation, etc. here later

#include <android/log.h>

#define LOG_TAG "LEModMenu"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

void LELog(const char* msg) {
    LOGI("%s", msg);
}
