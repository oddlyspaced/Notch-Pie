package com.oddlyspaced.np.Utils;

import android.content.Context;
import android.util.Log;

import com.oddlyspaced.np.R;

import java.util.ArrayList;

public class NotchConfig {

    private Context context;
    private static final String TAG = "NotchConfig";

    public NotchConfig(Context context) {
        this.context = context;
    }

    // true : data saved successfully
    public boolean saveNotchData() {
        try {
            GetNotchConfigTask task = new GetNotchConfigTask(context.getString(R.string.config_url), Integer.parseInt(context.getString(R.string.app_code)), new DeviceName().getDeviceName());
            task.execute();
            return dataToFile(task.get());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    private boolean dataToFile(ArrayList<String> data) {
        try {
            DataManager dataManager = new DataManager(context);
            dataManager.read();
            dataManager.setHeight(Integer.parseInt(data.get(0)));
            dataManager.setWidht(Integer.parseInt(data.get(1)));
            dataManager.setNotchSize(Integer.parseInt(data.get(2)));
            dataManager.setTopRadius(Integer.parseInt(data.get(3)));
            dataManager.setBottomRadius(Integer.parseInt(data.get(4)));
            return dataManager.save();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

}
