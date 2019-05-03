package com.oddlyspaced.np.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class GetNotchConfigTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private String link;
    private int versionCode;
    private String deviceName;
    private ArrayList<String> data;
    private static final String TAG = "GetNotchConfigTask";

    public GetNotchConfigTask(int versionCode, String deviceName) {
        super();
        this.link = link;
        this.versionCode = versionCode;
        this.deviceName = deviceName;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
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
