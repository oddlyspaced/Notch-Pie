package com.oddlyspaced.np.Constants;

import android.os.Environment;

// This class defines constants for the non contextual classes,
public class Constants {

    private final String appPackageName = "com.oddlyspaced.np";
    private final int appCode = 7;
    private final String configFolderPath = Environment.getExternalStorageDirectory() + "/Android/data/" + appPackageName;
    private final String configFilePath = configFolderPath + "/config";
    private final String configPreUrl = "https://raw.githubusercontent.com/oddlyspaced/NotchPie-Data/master/";

    public String getAppPackageName() {
        return appPackageName;
    }

    public int getAppCode() {
        return appCode;
    }

    public String getConfigFolderPath() {
        return configFolderPath;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }

    public String getConfigPreUrl() {
        return configPreUrl;
    }
}
