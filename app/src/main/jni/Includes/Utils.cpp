// Basic utils placeholder
// Add your KittyMemory wrappers, string obfuscation, offset helpers here later.

#include <string>
#include <android/log.h>

#define LOG_TAG "LEModMenu"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

namespace LEUtils {
    void log(const char* msg) {
        LOGI("%s", msg);
    }
}
