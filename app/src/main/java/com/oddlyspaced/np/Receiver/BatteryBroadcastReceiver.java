package com.oddlyspaced.np.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.BatteryManager;

import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Service.OverlayAccessibiltyService;

public class BatteryBroadcastReceiver extends BroadcastReceiver{

    // Interface
    private OverlayAccessibiltyService.OnBatteryChanged onBatteryChangedInterface;

    public BatteryBroadcastReceiver(OverlayAccessibiltyService.OnBatteryChanged onBatteryChangedInterface) {
        this.onBatteryChangedInterface = onBatteryChangedInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        onBatteryChangedInterface.onChanged(level);
    }

}
