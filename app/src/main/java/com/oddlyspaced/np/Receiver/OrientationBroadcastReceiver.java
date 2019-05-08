package com.oddlyspaced.np.Receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.oddlyspaced.np.Service.OverlayAccessibiltyService;

// Copied from : https://stackoverflow.com/questions/36086320/android-get-the-screen-rotation-from-a-broadcastreceiver

public class OrientationBroadcastReceiver extends BroadcastReceiver {

    // Interface
    private OverlayAccessibiltyService.OnRotate onRotateInterface;

    public OrientationBroadcastReceiver(OverlayAccessibiltyService.OnRotate onRotateInterface) {
        this.onRotateInterface = onRotateInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the orientation from the current configuration object
        //An integer that holds the value of the orientation given by the current configuration
        int configOrientation = context.getResources().getConfiguration().orientation;

        //Display the current orientation using a Toast notification
        switch (configOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE: {
                onRotateInterface.onLandscape();
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT: {
                onRotateInterface.onPortrait();
                break;
            }
        }
    }
}