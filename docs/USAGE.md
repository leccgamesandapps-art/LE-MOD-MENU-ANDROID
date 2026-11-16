# LE MOD MENU V1.0 – Usage Guide

## 1. Building the Starter

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Connect a device or use emulator (API 21+).
4. Click Run → the launcher activity appears.
5. Grant **Display over other apps** permission.
6. Press **START MOD MENU**.

A floating window should appear.

---

## 2. Adding Real Features

Edit `app/src/main/jni/Main.cpp`:

### Feature List
```cpp
static const char* features[] = {
    "Category_Player",
    "Toggle_God Mode",
    "Toggle_Speed Hack",
    "Category_Money",
    "Toggle_Unlimited Coins",
    NULL
};
```

### Handling Changes
```cpp
case 1: // God Mode (index starts from 0 after categories)
    if (booleanValue) {
        // Apply patch / set flag
    }
    break;
```

---

## 3. Making it work inside a game (Injection style)

Classic LGL-style flow:

1. Build this project → get `app-debug.apk`
2. Decompile the APK (apktool)
3. Decompile the target game
4. Copy:
   - The native library (`liblemodmenu.so` from `lib/armeabi-v7a` and/or `arm64-v8a`)
   - The Java classes under `com/lemodmenu/` (or change package)
5. In the game's `AndroidManifest.xml` add:
   ```xml
   <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
   ```
   and the service declaration.
6. Load the library early (usually from the main Activity `onCreate` or via a small Java stub that calls `System.loadLibrary("lemodmenu")`).
7. Rebuild + sign the game.

---

## 4. Adding Memory Patching / Hooking

This starter is intentionally clean.

Recommended libraries to integrate (same as classic templates):

- **KittyMemory** → easy memory patches
- **Dobby** or **And64InlineHook** → function hooking
- **xDL** → better library finding

Place them under `jni/` and update `CMakeLists.txt` / `Android.mk` accordingly.

---

## 5. Tips for better compatibility

- Prefer **ARM64** primary, keep ARMv7 for older devices.
- Always request overlay permission properly on Android 6+.
- On Android 10+ use `TYPE_APPLICATION_OVERLAY`.
- Test on real devices; emulators sometimes block overlays.
- For maximum stealth, change package name, lib name, class names, and strings.

---

## Next Steps for V1.1+

- Better feature types (Slider, Input, Spinner, ButtonOnOff)
- Theme system
- Save/load feature states
- Optional ImGui backend
- Auto-detection of common game engines

Enjoy building. Keep it educational and fair.
