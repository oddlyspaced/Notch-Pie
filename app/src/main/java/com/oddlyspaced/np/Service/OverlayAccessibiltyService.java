package com.oddlyspaced.np.Service;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Receiver.BatteryBroadcastReceiver;
import com.oddlyspaced.np.Receiver.FileBroadcastReceiver;
import com.oddlyspaced.np.Receiver.OrientationBroadcastReceiver;
import com.oddlyspaced.np.Utils.ColorLevel;
import com.oddlyspaced.np.Utils.DataManager;


public class OverlayAccessibiltyService extends AccessibilityService {

    private DataManager dataManager;
    private int statusbarHeight;
    private int batteryLevel;
    private WindowManager windowManager;
    private View overlayView;
    private FileObserver configOberver;
    private final String CUSTOM_BROADCAST_ACTION = "ConfigChangedBroadcase";

    private OrientationBroadcastReceiver receiverOrientation;
    private BatteryBroadcastReceiver receiverBattery;
    private FileBroadcastReceiver receiverConfig;

    @Override
    protected void onServiceConnected() {
        Log.d("Overlay", "Starting Overlay");
        init();
        startReceivers();
    }


    private void init() {
        dataManager = new DataManager(this);
        dataManager.read();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        statusbarHeight = getStatusBarHeight();
        overlayView = LayoutInflater.from(this).inflate(R.layout.layout_float, null); // the view which will be overlayed
        overlayView.setRotationY(180); // mirroring
        configOberver = new FileObserver(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/config") {
            @Override
            public void onEvent(int event, @Nullable String path) {
                if (event == MODIFY) {
                    Intent intent = new Intent(CUSTOM_BROADCAST_ACTION);
                    sendBroadcast(intent);
                }
            }
        };
        configOberver.startWatching();
    }

    private void startReceivers() {
        receiverOrientation = new OrientationBroadcastReceiver(new OnRotate() {
            @Override
            public void onPortrait() {
                makeOverlay();
            }

            @Override
            public void onLandscape() {
                removeOverlay();
            }
        });
        IntentFilter intentFilterOrientation = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
        receiverBattery = new BatteryBroadcastReceiver(new OnBatteryChanged() {
            @Override
            public void onChanged(int battery) {
                batteryLevel = battery;
                makeOverlay();
            }
        });
        IntentFilter intentFilterBattery = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiverConfig = new FileBroadcastReceiver(new OnConfigEdited() {
            @Override
            public void onEdited() {
                dataManager.read();
                makeOverlay();
            }
        });
        IntentFilter intentFilterConfig = new IntentFilter(CUSTOM_BROADCAST_ACTION);
        registerReceiver(receiverOrientation, intentFilterOrientation);
        registerReceiver(receiverBattery, intentFilterBattery);
        registerReceiver(receiverConfig, intentFilterConfig);
    }

    // Overlay Makers //
    private void makeOverlay() {
        Bitmap bitmap = drawNotch();
        ImageView img = overlayView.findViewById(R.id.imageView);
        img.setImageBitmap(bitmap);
        img.setRotation(0);
        overlayView.setRotation(0);
        try {
            windowManager.updateViewLayout(overlayView, generateParams(bitmap.getHeight(), bitmap.getWidth()));
            Log.e("yee", "haww");
        } catch (Exception e) {
            Log.e("original", e.toString());
            // if this gives an error then its probably because wm is empty
            try {
                windowManager.addView(overlayView, generateParams(bitmap.getHeight(), bitmap.getWidth()));
            }
            catch (Exception ee) {
                Log.e("why tho", ee.toString());
            }
        }
    }

    private void removeOverlay() {
        try {
            windowManager.removeView(overlayView); // errrorrrororoorroorr
        } catch (Exception e) {
            // such haxx much wow
        }
    }

    private Bitmap drawNotch() {
        int w = getWidht();
        int h = getHeight();
        int ns = getNotchSize();
        int tr = getTopRadius();
        int br = getBottomRadius();

        float p1 = 0;
        float p2 = (float) (w * 2);
        float p3 = (float) (((w + ns) * 2) + 2);
        float p4 = p3 - (p1 + p1);

        Path path = new Path();
        path.moveTo(p3 - 1.0f, -1.0f + p1);
        float p5 = -p1;
        path.rMoveTo(p5, p5);
        float p6 = h;
        float p7 = ns;
        float p8 = ((tr / 100.0f) * p7);// top radius
        float p9 = 0.0f;
        float p10 = ((br / 100.0f) * p7);
        p4 = -((p4 - ((p7 * 2.0f) + p2)) / 2.0f);
        path.rMoveTo(p4, 0.0F);
        float p11 = -p9;
        p3 = -p7;
        path.rCubicTo(-p8, p9, p10 - p7, p11 + p6, p3, p6);
        path.rLineTo(-p2, 0.0f);
        path.rCubicTo(-p10, p11, p8 - p7, p9 - p6, p3, -p6);

        path.close();

        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        rectF.height();

        Bitmap bitmap = Bitmap.createBitmap(
                //screenWidht,
                (int) Math.abs(rectF.width()),
                (int) Math.abs(rectF.height()) + 10,// , // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);

        int[] colors = getColorArray(batteryLevel, dataManager.isFullStatus());
        float[] positions = getPositionArray();
        SweepGradient sweepGradient = new SweepGradient((int) (Math.abs(rectF.width()) / 2) /*center*/, 0, colors, positions);
        paint.setShader(sweepGradient);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, paint);
        return bitmap;
        //windowManager.updateViewLayout(overlayView, generateParamsPo((int)Math.abs(rectF.height()), (int)Math.abs(rectF.width())));
    }

    // Layout Parameter Generators //

    private WindowManager.LayoutParams generateParams(int h, int w) {
        int layoutParameters = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                w,
                h,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                layoutParameters,
                PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.CENTER;
        //setting boundary
        p.y = -statusbarHeight - 10;
        return p;
    }

    // Array Generators //

    private int[] getColorArray(int battery, boolean isFullStatus) {
        int[] c = new int[181];
        if (isFullStatus) {
            for (ColorLevel item : dataManager.getColorLevels()) {
                if (battery >= item.getStartLevel() && battery <= item.getEndLevel()) {
                    for (int i = 0; i < 181; i++) {
                        c[i] = Color.parseColor(item.getColor());
                    }
                }
            }
        } else {
            for (int i = 0; i < 181; i++)
                c[i] = Color.parseColor("#000000");
            for (ColorLevel item : dataManager.getColorLevels()) {
                if (battery >= item.getStartLevel() && battery <= item.getEndLevel()) {
                    int val = (int) ((battery / 100.0) * 180.0);
                    for (int i = 0; i < val; i++) {
                        c[i] = Color.parseColor(item.getColor());
                    }
                }
            }
        }
        return c;
    }

    private float[] getPositionArray() {
        float a = 0.5F / 180;
        float c = 0;
        float[] p = new float[181];
        for (int i = 0; i < 180; i++) {
            p[i] = c;
            c += a;
        }
        p[180] = 0.5F;
        return p;
    }


    // Value assigners/finders //

    private int getStatusBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getHeight() {
        return dataManager.getHeight();
    }

    private int getWidht() {
        return dataManager.getWidht();
    }

    private int getNotchSize() {
        return dataManager.getNotchSize();
    }

    private int getTopRadius() {
        return dataManager.getTopRadius();
    }

    private int getBottomRadius() {
        return dataManager.getBottomRadius();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
        unregisterReceiver(receiverOrientation);
        unregisterReceiver(receiverBattery);
        unregisterReceiver(receiverConfig);
        configOberver.stopWatching();
    }

    // Interface for rotation handling
    public interface OnRotate {
        void onPortrait();
        void onLandscape();
    }

    // Interface for handling battery level change
    public interface OnBatteryChanged {
        void onChanged(int battery);
    }

    public interface OnConfigEdited {
        void onEdited();
    }

}
