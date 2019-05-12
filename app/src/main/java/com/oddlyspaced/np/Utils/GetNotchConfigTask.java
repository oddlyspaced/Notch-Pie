package com.oddlyspaced.np.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

// AsyncTask to fetch notch data from raw webpage
public class GetNotchConfigTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private String link;
    private int versionCode;
    private String deviceName;
    private static final String TAG = "GetNotchConfigTask";

    // constructor
    public GetNotchConfigTask(String link, int versionCode, String deviceName) {
        this.link = link;
        this.versionCode = versionCode;
        this.deviceName = deviceName;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> data = new ArrayList<>();
        try {
            URL url = new URL(link + deviceName + versionCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                data.add(line);
            }
            reader.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return data;
    }
}
