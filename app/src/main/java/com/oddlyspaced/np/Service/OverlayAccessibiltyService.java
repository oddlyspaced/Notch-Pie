package com.oddlyspaced.np.Service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.BatteryManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Utils.ColorLevel;
import com.oddlyspaced.np.Utils.DataManager;


public class OverlayAccessibiltyService extends AccessibilityService {

    private DataManager dataManager;
    private int currentRotation;
    private int screenWidht;
    private int statusbarHeight;
    private int batteryLevel1;
    private int batteryLevel2;
    private WindowManager windowManager;
    private View overlayView;
    private BatteryManager batteryManager;

    @Override
    protected void onServiceConnected() {
        Log.d("Overlay", "Starting Overlay");
        showOverlay();
    }

    // do not ask me why this method exists
    // i just like it that.
    private void showOverlay() {
        init();
        checkRotation();
    }


    private void init() {
        dataManager = new DataManager(this);
        dataManager.read();
        currentRotation = -1; // initial value
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        statusbarHeight = getStatusBarHeight();
        screenWidht = getScreenWidht();
        overlayView = LayoutInflater.from(this).inflate(R.layout.layout_float, null); // the view which will be overlayed
        overlayView.setRotationY(180); // mirroring
        batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
        batteryLevel1 = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        batteryLevel2 = -1;
    }

    // Rotation Check Thread //
    private void checkRotation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int rotation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
                batteryLevel1 = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                if (rotation == Surface.ROTATION_0 && currentRotation != 0) {
                    // do something
                    currentRotation = 0;
                    makeOverlayPortrait();
                }
                else if (rotation == Surface.ROTATION_90 && currentRotation != 90) {
                    currentRotation = 90;
                    makeOverlayLandscape();
                }
                else if (rotation == Surface.ROTATION_180 && currentRotation != 180) { // do nothing
                    currentRotation = 180;
                }
                else if (rotation == Surface.ROTATION_270 &&  currentRotation != 270) {
                    currentRotation = 270;
                    makeOverlayLandscapeReverse();
                }

                DataManager old = dataManager.get();
                dataManager.read();
                if (!old.isSame(dataManager.get())) {
                    if (currentRotation == 0) {
                        makeOverlayPortrait();
                    }
                }

                if (batteryLevel1 != batteryLevel2) {
                    if (currentRotation == 0)
                        makeOverlayPortrait();
                    else if (currentRotation == 90)
                        makeOverlayLandscape();
                    else if (currentRotation == 270)
                        makeOverlayLandscapeReverse();
                }

                batteryLevel2 = batteryLevel1;
                // check again
                checkRotation();
            }
        }, 10);
    }

    // Overlay Makers //
    private void makeOverlayPortrait() {
        Bitmap bitmap = drawNotch();
        ImageView img = overlayView.findViewById(R.id.imageView);
        img.setImageBitmap(bitmap);
        img.setRotation(0);
        overlayView.setRotation(0);
        try {
            windowManager.updateViewLayout(overlayView, generateParamsPortrait(bitmap.getHeight(), bitmap.getWidth()));
        }
        catch (Exception e) {
            // if this gives an error then its probably because wm is empty
            windowManager.addView(overlayView, generateParamsPortrait(bitmap.getHeight(), bitmap.getWidth()));
        }
    }

    private void makeOverlayLandscape() {
        try {
            windowManager.removeView(overlayView); // errrorrrororoorroorr
        }
        catch (Exception e) {
            // such haxx much wow
        }
    }

    private void makeOverlayLandscapeReverse() {
        try {
            windowManager.removeView(overlayView); // errrorrrororoorroorr
        }
        catch (Exception e) {
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
        //float p3 = (float) (screenWidht + 2);
        float p3 = (float) (((w + ns)*2) + 2);//////////////////////////
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
                (int)Math.abs(rectF.width()),
                (int)Math.abs(rectF.height())+10,// , // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);

        int[] colors = getColorArray(batteryLevel1);
        float[] positions = getPositionArray();
        SweepGradient sweepGradient = new SweepGradient((int)(Math.abs(rectF.width())/2) /*center*/, 0, colors, positions);
        paint.setShader(sweepGradient);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, paint);
        return bitmap;
        //windowManager.updateViewLayout(overlayView, generateParamsPo((int)Math.abs(rectF.height()), (int)Math.abs(rectF.width())));
    }

    // Layout Parameter Generators //

    private WindowManager.LayoutParams generateParamsPortrait(int h, int w) {
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

    private WindowManager.LayoutParams generateParamsLandscape(int h, int w) {
        int layoutParameters = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                w,
                h,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                layoutParameters,
                PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.LEFT | Gravity.CENTER;
        //setting boundary
        //p.x = -statusbarHeight;
        return p;
    }

    // Array Generators //

    private int[] getColorArray (int battery) {
        int c[] = new int[181];
        for (int i = 0; i < 181; i++)
            c[i] = Color.parseColor("#000000");
        for (ColorLevel item : dataManager.getColorLevels()) {
            if (battery >= item.getStartLevel() && battery <= item.getEndLevel()) {
                int val = (int)((battery/100.0)*180.0);
                for (int i = 0; i < val; i++) {
                    c[i] = Color.parseColor(item.getColor());
                }
            }
        }
        return c;
    }

    private float[] getPositionArray () {
        float a = 0.5F / 180;
        float c = 0;
        float p[] = new float[181];
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

    private int getScreenWidht() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
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

    }

}
