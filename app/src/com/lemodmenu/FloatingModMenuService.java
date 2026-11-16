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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingModMenuService extends Service {

    static {
        try {
            System.loadLibrary("lemodmenu");
        } catch (UnsatisfiedLinkError e) {
            // Native not found - demo mode
        }
    }

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;

    public native String[] getFeatureList();
    public native void Changes(int featNum, boolean booleanValue, int intValue, String strValue);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundNotification();
        createFloatingMenu();
    }

    private void startForegroundNotification() {
        String channelId = "le_mod_menu_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
    }

    private void createFloatingMenu() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

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

        try {
            String[] features = getFeatureList();
            if (features != null && features.length > 0) {
                for (int i = 0; i < features.length; i++) {
                    final int featNum = i;
                    String feat = features[i];
                    if (feat == null) continue;

                    if (feat.startsWith("Category_")) {
                        TextView cat = new TextView(this);
                        cat.setText(feat.replace("Category_", ""));
                        cat.setTextColor(Color.CYAN);
                        cat.setTextSize(14);
                        cat.setPadding(8, 16, 8, 8);
                        featuresLayout.addView(cat);
                    } else if (feat.startsWith("Toggle_") || feat.startsWith("Switch_")) {
                        Switch sw = new Switch(this);
                        sw.setText(feat.replace("Toggle_", "").replace("Switch_", ""));
                        sw.setTextColor(Color.WHITE);
                        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Changes(featNum, isChecked, 0, "");
                            }
                        });
                        featuresLayout.addView(sw);
                    } else {
                        TextView tv = new TextView(this);
                        tv.setText(feat);
                        tv.setTextColor(Color.LTGRAY);
                        tv.setPadding(8, 8, 8, 8);
                        featuresLayout.addView(tv);
                    }
                }
            } else {
                addDemoFeatures(featuresLayout);
            }
        } catch (UnsatisfiedLinkError e) {
            addDemoFeatures(featuresLayout);
        } catch (Exception e) {
            addDemoFeatures(featuresLayout);
        }

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

        int layoutType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        }

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
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                }
                return false;
            }
        });

        try {
            windowManager.addView(floatingView, params);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to show overlay: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addDemoFeatures(LinearLayout layout) {
        TextView demo = new TextView(this);
        demo.setText("Demo Mode (Native not loaded)");
        demo.setTextColor(Color.YELLOW);
        demo.setPadding(8, 8, 8, 16);
        layout.addView(demo);

        Switch s1 = new Switch(this);
        s1.setText("God Mode");
        s1.setTextColor(Color.WHITE);
        layout.addView(s1);

        Switch s2 = new Switch(this);
        s2.setText("Unlimited Coins");
        s2.setTextColor(Color.WHITE);
        layout.addView(s2);

        Switch s3 = new Switch(this);
        s3.setText("No Damage");
        s3.setTextColor(Color.WHITE);
        layout.addView(s3);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null && windowManager != null) {
            try {
                windowManager.removeView(floatingView);
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
