package com.lemodmenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int OVERLAY_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check overlay permission first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // Go directly to Display over other apps settings
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                Toast.makeText(this, "Please allow Display over other apps", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Permission already granted → start menu automatically
        startModMenu();
    }

    private void startModMenu() {
        try {
            startService(new Intent(this, FloatingModMenuService.class));
            Toast.makeText(this, "LE Mod Menu started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to start menu", Toast.LENGTH_SHORT).show();
        }
        // Close the activity so only floating menu remains
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // User granted permission → start menu
                    startModMenu();
                } else {
                    // Still not granted
                    Toast.makeText(this, "Overlay permission is required", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}
