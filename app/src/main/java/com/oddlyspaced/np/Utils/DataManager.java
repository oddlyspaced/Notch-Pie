package com.oddlyspaced.np.Utils;

import android.util.Log;

import com.oddlyspaced.np.Constants.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DataManager {

    private final String TAG = "DataManager";

    private String configPath;

    private int height, width, notchSize, topRadius, bottomRadius;
    private ArrayList<ColorLevel> colorLevels;
    private boolean fullStatus;

    public String error = "";

    // this constructor takes the context to get the directory where file is being saved in Android>Data
    public DataManager() {
        configPath = new Constants().getConfigFilePath();
    }

    // this constructor takes a custom config path instead
    public DataManager(String configPath) {
        this.configPath = configPath;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
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

    // this method reads the config file to load up details.
    // return true if success and false if some error occurs
    public boolean read() {
        try {
            File dataFile = new File(configPath);
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            height = Integer.parseInt(reader.readLine());
            width = Integer.parseInt(reader.readLine());
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
        } catch (Exception e) {
            error = e.toString();
            Log.e(TAG, e.toString());
            return false;
        }
    }

    // this method saves the file
    public boolean save() {
        try {
            File dataFile = new File(configPath);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            writer.println(height + "");
            writer.println(width + "");
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
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean save(String configPath) {
        try {
            File dataFile = new File(configPath);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            writer.println(height + "");
            writer.println(width + "");
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
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

}
