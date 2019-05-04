package com.oddlyspaced.np.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import com.oddlyspaced.np.Service.OverlayAccessibiltyService;

public class FileBroadcastReceiver extends BroadcastReceiver {

    // Interface
    private OverlayAccessibiltyService.OnConfigEdited onConfigEditedInterface;

    public FileBroadcastReceiver(OverlayAccessibiltyService.OnConfigEdited onConfigEditedInterface) {
        this.onConfigEditedInterface = onConfigEditedInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onConfigEditedInterface.onEdited();
    }
}


