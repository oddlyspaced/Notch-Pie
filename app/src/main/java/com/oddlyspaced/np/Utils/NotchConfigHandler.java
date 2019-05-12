package com.oddlyspaced.np.Utils;

import android.util.Log;

import com.oddlyspaced.np.Constants.Constants;

import java.util.ArrayList;

public class NotchConfigHandler {

    private static final String TAG = "NotchConfigHandler";
    private Constants constants;

    // constructor
    public NotchConfigHandler() {
        constants = new Constants();
    }

    // main method to call in order to perform fetching and saving
    // true : data saved successfully
    public boolean saveFetchNotchData() {
        try {
            GetNotchConfigTask task = new GetNotchConfigTask(constants.getConfigPreUrl(), constants.getAppCode(), new DeviceName().getDeviceName());
            task.execute();
            return dataToFile(task.get());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    // takes data arraylist and saves it to file
    private boolean dataToFile(ArrayList<String> data) {
        try {
            DataManager dataManager = new DataManager();
            dataManager.read();
            dataManager.setHeight(Integer.parseInt(data.get(0)));
            dataManager.setWidth(Integer.parseInt(data.get(1)));
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
