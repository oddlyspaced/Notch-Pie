package com.oddlyspaced.np.Utils;

import android.os.Environment;
import android.util.Log;

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

    private int height, widht, notchSize, topRadius, bottomRadius;
    private ArrayList<ColorLevel> colorLevels;

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

    public void setColorLevels(ArrayList<ColorLevel> colorLevels) {
        this.colorLevels = colorLevels;
    }

    public void save() {
        try {
            File dataFile = new File(Environment.getExternalStorageDirectory() + "/notchpie.data");
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            writer.println(height + "");
            writer.println(widht + "");
            writer.println(notchSize + "");
            writer.println(topRadius + "");
            writer.println(bottomRadius + "");
            for (ColorLevel item : colorLevels) {
                writer.println(item.getColor() + "," + item.getStartLevel() + "," + item.getEndLevel());
            }
            writer.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void read() {
        try {
            File dataFile = new File(Environment.getExternalStorageDirectory() + "/notchpie.data");
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            height = Integer.parseInt(reader.readLine());
            widht = Integer.parseInt(reader.readLine());
            notchSize = Integer.parseInt(reader.readLine());
            topRadius = Integer.parseInt(reader.readLine());
            bottomRadius = Integer.parseInt(reader.readLine());
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
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
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
        DataManager obj = new DataManager();
        obj.setHeight(height);
        obj.setWidht(widht);
        obj.setTopRadius(topRadius);
        obj.setBottomRadius(bottomRadius);
        obj.setNotchSize(notchSize);
        return obj;
    }
}
