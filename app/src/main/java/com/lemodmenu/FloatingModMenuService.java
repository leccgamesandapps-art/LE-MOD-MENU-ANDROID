package com.lemodmenu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingModMenuService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            startForegroundNotification();
            createFloatingMenu();
        } catch (Throwable t) {
            Toast.makeText(this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }

    private void startForegroundNotification() {
        try {
            String channelId = "le_mod_menu_channel";

            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(
                        channelId, "LE Mod Menu", NotificationManager.IMPORTANCE_LOW);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }

                Notification notification = new Notification.Builder(this, channelId)
                        .setContentTitle("LE MOD MENU V1.0")
                        .setContentText("Floating menu is active")
                        .setSmallIcon(android.R.drawable.ic_menu_manage)
                        .build();

                startForeground(1, notification);
            } else {
                Notification notification = new Notification.Builder(this)
                        .setContentTitle("LE MOD MENU V1.0")
                        .setContentText("Floating menu is active")
                        .setSmallIcon(android.R.drawable.ic_menu_manage)
                        .build();

                startForeground(1, notification);
            }
        } catch (Throwable t) {
            // ignore
        }
    }

    private void createFloatingMenu() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager == null) {
            stopSelf();
            return;
        }

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#DD111111"));
        root.setPadding(24, 24, 24, 24);

        TextView title = new TextView(this);
        title.setText("LE MOD MENU V1.0");
        title.setTextColor(Color.WHITE);
        title.setTextSize(16);
        title.setPadding(8, 8, 8, 16);
        root.addView(title);

        ScrollView scroll = new ScrollView(this);
        LinearLayout featuresLayout = new LinearLayout(this);
        featuresLayout.setOrientation(LinearLayout.VERTICAL);

        // Demo features
        TextView demo = new TextView(this);
        demo.setText("Demo Mode");
        demo.setTextColor(Color.YELLOW);
        demo.setPadding(8, 8, 8, 16);
        featuresLayout.addView(demo);

        Switch s1 = new Switch(this);
        s1.setText("God Mode");
        s1.setTextColor(Color.WHITE);
        featuresLayout.addView(s1);

        Switch s2 = new Switch(this);
        s2.setText("Unlimited Coins");
        s2.setTextColor(Color.WHITE);
        featuresLayout.addView(s2);

        Switch s3 = new Switch(this);
        s3.setText("No Damage");
        s3.setTextColor(Color.WHITE);
        featuresLayout.addView(s3);

        Switch s4 = new Switch(this);
        s4.setText("FPS Unlock");
        s4.setTextColor(Color.WHITE);
        featuresLayout.addView(s4);

        scroll.addView(featuresLayout);
        root.addView(scroll);

        Button closeBtn = new Button(this);
        closeBtn.setText("CLOSE");
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        root.addView(closeBtn);

        floatingView = root;

        int layoutType = Build.VERSION.SDK_INT >= 26 ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 200;

        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        try {
                            windowManager.updateViewLayout(floatingView, params);
                        } catch (Exception e) {}
                        return true;
                }
                return false;
            }
        });

        try {
            windowManager.addView(floatingView, params);
            Toast.makeText(this, "LE Mod Menu started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Please grant Overlay permission", Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null && windowManager != null) {
            try {
                windowManager.removeView(floatingView);
            } catch (Exception e) {}
        }
    }
}
