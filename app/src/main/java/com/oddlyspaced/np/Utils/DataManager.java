package com.oddlyspaced.np.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DataManager {

    private final String TAG = "DataManager";

    private Context context;
    private String configPath;
    private Uri configUri;

    private int height, widht, notchSize, topRadius, bottomRadius;
    private ArrayList<ColorLevel> colorLevels;
    private boolean fullStatus;

    public DataManager(Context context) {
        this.context = context;
        configPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/config";
    }

    public DataManager(Context context, String configPath) {
        this.context = context;
        this.configPath = configPath;
    }

    public DataManager(Context context, Uri configUri) {
        this.context = context;
        this.configUri = configUri;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidht(int widht) {
        this.widht = widht;
    }

    public void setNotchSize(int notchSize) {
        this.notchSize = notchSize;
    }

    public void setTopRadius(int topRadius) {
        this.topRadius = topRadius;
    }

    public void setBottomRadius(int bottomRadius) {
        this.bottomRadius = bottomRadius;
    }
    
    public void setFullStatus(boolean fullStatus) {
        this.fullStatus = fullStatus;
    }

    public void setColorLevels(ArrayList<ColorLevel> colorLevels) {
        this.colorLevels = colorLevels;
    }

    public boolean save() {
        try {
            File dataFile = new File(configPath);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            writer.println(height + "");
            writer.println(widht + "");
            writer.println(notchSize + "");
            writer.println(topRadius + "");
            writer.println(bottomRadius + "");
            if (fullStatus)
                writer.println("T");
            else
                writer.println("F");
            for (ColorLevel item : colorLevels) {
                writer.println(item.getColor() + "," + item.getStartLevel() + "," + item.getEndLevel());
            }
            writer.close();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean save(String savePath) {
        try {
            File dataFile = new File(savePath);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            writer.println(height + "");
            writer.println(widht + "");
            writer.println(notchSize + "");
            writer.println(topRadius + "");
            writer.println(bottomRadius + "");
            if (fullStatus)
                writer.println("T");
            else
                writer.println("F");
            for (ColorLevel item : colorLevels) {
                writer.println(item.getColor() + "," + item.getStartLevel() + "," + item.getEndLevel());
            }
            writer.close();
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public String error = "";

    public boolean read() {
        try {
            File dataFile = new File(configPath);
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            height = Integer.parseInt(reader.readLine());
            widht = Integer.parseInt(reader.readLine());
            notchSize = Integer.parseInt(reader.readLine());
            topRadius = Integer.parseInt(reader.readLine());
            bottomRadius = Integer.parseInt(reader.readLine());
            fullStatus = reader.readLine().equals("T");
            colorLevels = new ArrayList<>();
            while (true) {
                String s = reader.readLine();
                if (s == null)
                    break;
                StringTokenizer stringTokenizer = new StringTokenizer(s, ",");
                ColorLevel cl = new ColorLevel();

                cl.setColor(stringTokenizer.nextToken());
                cl.setStartLevel(Integer.parseInt(stringTokenizer.nextToken()));
                cl.setEndLevel(Integer.parseInt(stringTokenizer.nextToken()));
                colorLevels.add(cl);
            }
            reader.close();
            return true;
        }
        catch (Exception e) {
            error = e.toString();
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean readUri() {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(configUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            height = Integer.parseInt(reader.readLine());
            widht = Integer.parseInt(reader.readLine());
            notchSize = Integer.parseInt(reader.readLine());
            topRadius = Integer.parseInt(reader.readLine());
            bottomRadius = Integer.parseInt(reader.readLine());
            fullStatus = reader.readLine().equals("T");
            colorLevels = new ArrayList<>();
            while (true) {
                String s = reader.readLine();
                if (s == null)
                    break;
                StringTokenizer stringTokenizer = new StringTokenizer(s, ",");
                ColorLevel cl = new ColorLevel();

                cl.setColor(stringTokenizer.nextToken());
                cl.setStartLevel(Integer.parseInt(stringTokenizer.nextToken()));
                cl.setEndLevel(Integer.parseInt(stringTokenizer.nextToken()));
                colorLevels.add(cl);
            }
            inputStream.close();
            reader.close();
            return true;
        }
        catch (Exception e) {
            error = e.toString();
            Log.e(TAG, e.toString());
            return false;
        }

    }

    public int getHeight() {
        return height;
    }

    public int getWidht() {
        return widht;
    }

    public int getNotchSize() {
        return notchSize;
    }

    public int getTopRadius() {
        return topRadius;
    }

    public int getBottomRadius() {
        return bottomRadius;
    }

    public ArrayList<ColorLevel> getColorLevels() {
        return colorLevels;
    }

    public boolean isFullStatus() {
        return fullStatus;
    }

    public boolean isSame (DataManager obj) {
        try {
            if (height != obj.getHeight())
                return false;
            if (widht != obj.getWidht())
                return false;
            if (notchSize != obj.getNotchSize())
                return false;
            if (topRadius != obj.getTopRadius())
                return false;
            if (bottomRadius != obj.getBottomRadius())
                return false;
            if (!obj.getColorLevels().equals(colorLevels))
                return false;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public DataManager get(){
        DataManager obj = new DataManager(context);
        obj.setHeight(height);
        obj.setWidht(widht);
        obj.setTopRadius(topRadius);
        obj.setBottomRadius(bottomRadius);
        obj.setNotchSize(notchSize);
        return obj;
    }
}
