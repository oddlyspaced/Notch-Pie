package com.oddlyspaced.np.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oddlyspaced.np.Interface.OnConfigEdited;

// Broadcast Receiver for receiving FileUpdates
public class FileBroadcastReceiver extends BroadcastReceiver {

    // Interface
    private OnConfigEdited onConfigEditedInterface;

    // Constructor
    public FileBroadcastReceiver(OnConfigEdited onConfigEditedInterface) {
        this.onConfigEditedInterface = onConfigEditedInterface;
    }

    // here onReceive is called when file is modified
    @Override
    public void onReceive(Context context, Intent intent) {
        onConfigEditedInterface.onEdited();
    }
}


