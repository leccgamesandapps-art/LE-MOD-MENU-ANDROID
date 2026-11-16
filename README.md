# LE MOD MENU ANDROID V1.0

**LE MOD MENU** — A clean, modern Android + Native floating mod menu starter template.

Inspired by classic LGL-style menus but rebuilt for better structure, wider Android compatibility (API 21 → Android 15/16), and easier setup for different games.

---

## Features

- Floating overlay mod menu (SYSTEM_ALERT_WINDOW)
- Native C++ / JNI bridge
- Feature list system (Toggle, Slider, Button, Category, etc.)
- Ready for KittyMemory / Dobby / xDL style patching & hooking
- ARMv7 + ARM64 support
- Minimal permissions
- Educational / offline / single-player focused design
- Easy to inject into games or run as standalone overlay

---

## Project Structure

```
LE-MOD-MENU-ANDROID/
├── app/
│   └── src/main/
│       ├── java/com/lemodmenu/          # Java side (Service + UI)
│       ├── jni/                         # Native C++ code
│       │   ├── Main.cpp                 # Feature list + Changes + hack thread
│       │   ├── Menu/                    # Menu helpers
│       │   └── Includes/                # Utils, obfuscate, macros
│       ├── res/                         # Layouts, values
│       └── AndroidManifest.xml
├── docs/                                # Usage guides
├── build.gradle / settings.gradle
└── README.md
```

---

## Quick Start

1. Open the project in **Android Studio** (or AIDE / NDK).
2. Sync Gradle.
3. Build the APK (`app-debug.apk`).
4. For injection into a game:
   - Decompile your menu APK + target game.
   - Copy the `lib` folder (or the `.so`) and the Java service classes.
   - Add the required permission + service to the game's `AndroidManifest.xml`.
   - Load the library early (usually in the main activity or via JNI_OnLoad).

Detailed steps are in the `docs/` folder.

---

## Compatibility Goals

- Android 5.0 (API 21) → Android 15/16
- ARMv7 + ARM64
- Works with il2cpp, native, and many Unity games
- Overlay works with / without root (permission required)

---

## Important Notes

This is a **starter template** for learning and personal offline/single-player modding.

- Do **not** use this to cheat in online multiplayer games.
- Respect game ToS and local laws.
- The native side is intentionally left clean so you can add your own hooks/patches.

---

## Credits & Inspiration

Built as a clean LE-branded starter.  
Classic floating menu concepts come from the long line of community templates (LGL Team style and earlier works).

---

**LE MOD MENU V1.0**  
Made for the community. Keep it free.
