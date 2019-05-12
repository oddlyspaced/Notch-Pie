package com.oddlyspaced.np.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Class to get device name in a properly formatted form
public class DeviceName {

    private static final String TAG = "DeviceName";

    // main method
    public String getDeviceName() {
        String dn = exec("getprop ro.product.model");
        // using string tokenizer to remove spaces
        StringTokenizer tokenizer = new StringTokenizer(dn);
        dn = "";
        while (tokenizer.hasMoreTokens())
            dn = dn.concat(tokenizer.nextToken());
        Log.d(TAG, "Device Name : " + dn);
        return dn.toLowerCase();
    }

    // executes shell command
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
