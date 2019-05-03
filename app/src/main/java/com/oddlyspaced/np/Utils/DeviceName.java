package com.oddlyspaced.np.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class DeviceName {

    private static final String TAG = "DeviceName";

    public String getDeviceName() {
        String dn = exec("getprop ro.product.model");
        StringTokenizer tokenizer = new StringTokenizer(dn);
        dn = "";
        while (tokenizer.hasMoreTokens())
            dn.concat(tokenizer.nextToken());
        Log.d(TAG, "Device Name : " + dn);
        return dn;
    }

    private String exec (String command) {
        StringBuilder output = new StringBuilder();
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while (true) {
                line = reader.readLine();
                if (line == null)
                    break;
                output.append("\n").append(line);
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return output.toString();
    }


}
