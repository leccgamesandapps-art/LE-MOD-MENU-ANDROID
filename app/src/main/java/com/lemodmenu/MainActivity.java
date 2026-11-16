package com.lemodmenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int OVERLAY_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = new TextView(this);
        title.setText("LE MOD MENU V1.0");
        title.setTextSize(22);
        title.setPadding(40, 80, 40, 20);

        TextView info = new TextView(this);
        info.setText("Floating Native Mod Menu Starter\n\nGrant overlay permission then start the menu.");
        info.setPadding(40, 20, 40, 40);

        Button startBtn = new Button(this);
        startBtn.setText("START MOD MENU");
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOverlayPermission()) {
                    startService(new Intent(MainActivity.this, FloatingModMenuService.class));
                    Toast.makeText(MainActivity.this, "LE Mod Menu started", Toast.LENGTH_SHORT).show();
                } else {
                    requestOverlayPermission();
                }
            }
        });

        Button stopBtn = new Button(this);
        stopBtn.setText("STOP MENU");
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, FloatingModMenuService.class));
                Toast.makeText(MainActivity.this, "Menu stopped", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);
        layout.addView(title);
        layout.addView(info);
        layout.addView(startBtn);
        layout.addView(stopBtn);

        setContentView(layout);
    }

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (checkOverlayPermission()) {
                startService(new Intent(this, FloatingModMenuService.class));
            } else {
                Toast.makeText(this, "Overlay permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }
}
