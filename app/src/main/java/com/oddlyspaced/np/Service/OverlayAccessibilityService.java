package com.oddlyspaced.np.Service;

import android.accessibilityservice.AccessibilityService;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.oddlyspaced.np.Interface.OnBatteryLevelChanged;
import com.oddlyspaced.np.Interface.OnConfigEdited;
import com.oddlyspaced.np.Interface.OnRotate;
import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Receiver.BatteryBroadcastReceiver;
import com.oddlyspaced.np.Receiver.FileBroadcastReceiver;
import com.oddlyspaced.np.Receiver.OrientationBroadcastReceiver;
import com.oddlyspaced.np.Utils.ColorLevel;
import com.oddlyspaced.np.Utils.DataManager;

// The Service class to show and handle the overlay
public class OverlayAccessibilityService extends AccessibilityService {

    private DataManager dataManager;
    private int statusbarHeight;
    private int batteryLevel;
    private WindowManager windowManager;
    private View overlayView;
    private FileObserver configObserver;
    private final String CUSTOM_BROADCAST_ACTION = "ConfigChangedBroadcast";
    private boolean isPortrait = true;

    private OrientationBroadcastReceiver receiverOrientation;
    private BatteryBroadcastReceiver receiverBattery;
    private FileBroadcastReceiver receiverConfig;

    // Executed when service started
    @Override
    protected void onServiceConnected() {
        Log.d("Overlay", "Starting Overlay");
        init();
        startReceivers();
    }

    // this method initialises every variable to be used
    private void init() {
        dataManager = new DataManager();
        dataManager.read();
        // getting the window manager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        statusbarHeight = getStatusBarHeight();
        // the parent image view in which the bitmap is set
        overlayView = LayoutInflater.from(this).inflate(R.layout.layout_float, null); // the view which will be overlayed
        overlayView.setRotationY(180); // mirroring
        // the observer which sends broadcast intents
        configObserver = new FileObserver(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/config") {
            @Override
            public void onEvent(int event, @Nullable String path) {
                if (event == MODIFY && isPortrait) {
                    Intent intent = new Intent(CUSTOM_BROADCAST_ACTION);
                    sendBroadcast(intent);
                }
            }
        };
        // start observer
        configObserver.startWatching();
    }

    // this method starts and initialises every receiver to be uses in service
    private void startReceivers() {
        receiverOrientation = new OrientationBroadcastReceiver(new OnRotate() {
            @Override
            public void onPortrait() {
                isPortrait = true;
                makeOverlay();
            }

            @Override
            public void onLandscape() {
                isPortrait = false;
                removeOverlay();
            }
        });
        IntentFilter intentFilterOrientation = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
        receiverBattery = new BatteryBroadcastReceiver(new OnBatteryLevelChanged() {
            @Override
            public void onChanged(int battery) {
                batteryLevel = battery;
                if (isPortrait) {
                    makeOverlay();
                }
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

    // this method acts as a main method and calls the sub methods to get the overlay done and applied it aswell
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
            } catch (Exception ee) {
                Log.e("why tho", ee.toString());
            }
        }
    }

    // this method removes the overlay from the view
    private void removeOverlay() {
        try {
            windowManager.removeView(overlayView); // this will error out if wm is already empty
        } catch (Exception e) {
            // such haxx much wow
        }
    }

    // this method draws the notch shape
    // please don't ask me how it works now
    // i kinda forgot myself
    // it just works so i am not commenting out anything in it.
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
                //screenWidth,
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
    }

    // this method generates the layout parameters for the overlay to rise above the status bar limits
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
        p.y = -statusbarHeight - 10; // -10 just in case
        return p;
    }

    // Array Generators //
    // this method generates the color palette depending upon the config
    private int[] getColorArray(int battery, boolean isFullStatus) {
        int[] c = new int[181];
        if (isFullStatus) { // fill the overlay with one color
            for (ColorLevel item : dataManager.getColorLevels()) {
                if (battery >= item.getStartLevel() && battery <= item.getEndLevel()) {
                    for (int i = 0; i < 181; i++) {
                        c[i] = Color.parseColor(item.getColor());
                    }
                }
            }
        } else { // need partially filler overlay
            for (int i = 0; i < 181; i++)
                c[i] = Color.TRANSPARENT;
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

    // this method generates an evenly spread position point array
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
        return dataManager.getWidth();
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
        configObserver.stopWatching();
    }

}
