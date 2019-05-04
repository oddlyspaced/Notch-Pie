package com.oddlyspaced.np.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.oddlyspaced.np.Adapter.BatteryColorAdapter;
import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Utils.ColorLevel;
import com.oddlyspaced.np.Utils.ColorPicker;
import com.oddlyspaced.np.Utils.DataManager;
import com.oddlyspaced.np.Utils.GetNotchConfigTask;
import com.oddlyspaced.np.Utils.NotchConfig;

import java.io.File;
import java.util.ArrayList;

public class ConfigurationScreen extends AppCompatActivity implements ColorPicker.ColorPickerListener, BatteryColorAdapter.onTouchColorLevel {

    private SeekBar height, widht, notchSize, notchRadiusB, notchRadiusT;
    private FloatingActionButton getConfig;
    private CheckBox fullStatus;
    private DataManager dataManager;

    private int[] heightLimit = {50, 500};
    private int[] widhtLimit = {1, 500};
    private int[] notchSizeLimit = {1, 500};
    private int[] notchRadiusBLimit = {0, 100};
    private int[] notchRadiusTLimit = {0, 100};

    private RecyclerView batteryColorView;
    private BatteryColorAdapter batteryColorAdapter;
    private ArrayList<ColorLevel> list;

    private int positionTouch = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_screen);
        checkFile();
        initViews();
        loadRecycler();
    }

    private void checkFile() {
        File dataFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/config");
        if (!dataFile.exists() || dataFile.exists() && dataFile.length() == 0) {
            DataManager dataManager = new DataManager(this);
            dataManager.setHeight(50);
            dataManager.setWidht(10);
            dataManager.setBottomRadius(0);
            dataManager.setTopRadius(0);
            dataManager.setNotchSize(0);
            ArrayList<ColorLevel> levels = new ArrayList<>();

            ColorLevel level = new ColorLevel();

            level.setColor("#FF5555");
            level.setStartLevel(0);
            level.setEndLevel(5);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(6);
            level.setEndLevel(10);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(11);
            level.setEndLevel(20);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(21);
            level.setEndLevel(30);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(31);
            level.setEndLevel(40);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(41);
            level.setEndLevel(50);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(51);
            level.setEndLevel(60);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(61);
            level.setEndLevel(70);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(71);
            level.setEndLevel(80);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#ffb86c");
            level.setStartLevel(81);
            level.setEndLevel(90);
            levels.add(level);

            level = new ColorLevel();
            level.setColor("#50fa7b");
            level.setStartLevel(91);
            level.setEndLevel(100);
            levels.add(level);

            dataManager.setColorLevels(levels);
            dataManager.save();
        }
    }

    private void initViews() {
        height = findViewById(R.id.sbHeight);
        widht = findViewById(R.id.sbWidht);
        notchSize = findViewById(R.id.sbNotchSize);
        notchRadiusB = findViewById(R.id.sbBottomRadius);
        notchRadiusT = findViewById(R.id.sbTopRadius);
        // setting limits
        height.setMin(heightLimit[0]);
        height.setMax(heightLimit[1]);
        widht.setMin(widhtLimit[0]);
        widht.setMax(widhtLimit[1]);
        notchSize.setMin(notchSizeLimit[0]);
        notchSize.setMax(notchSizeLimit[1]);
        notchRadiusT.setMin(notchRadiusTLimit[0]);
        notchRadiusT.setMax(notchRadiusTLimit[1]);
        notchRadiusB.setMin(notchRadiusBLimit[0]);
        notchRadiusB.setMax(notchRadiusBLimit[1]);

        // setting progress
        dataManager = new DataManager(this);
        dataManager.read();
        height.setProgress(dataManager.getHeight());
        Log.e("heightR", dataManager.getHeight() + "");
        widht.setProgress(dataManager.getWidht());
        Log.e("widthR", dataManager.getWidht() + "");
        notchSize.setProgress(dataManager.getNotchSize());
        notchRadiusB.setProgress(dataManager.getBottomRadius());
        notchRadiusT.setProgress(dataManager.getTopRadius());
        // setting listener
        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("heightC", progress + "");
                dataManager.setHeight(progress);
                dataManager.save();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        widht.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("widhtC", progress + "");
                dataManager.setWidht(progress);
                dataManager.save();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        notchSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataManager.setNotchSize(progress);
                dataManager.save();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        notchRadiusB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataManager.setBottomRadius(progress);
                dataManager.save();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        notchRadiusT.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataManager.setTopRadius(progress);
                dataManager.save();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        FloatingActionButton open = findViewById(R.id.fabOpen);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), getString(R.string.popup_accessibility_toast), Toast.LENGTH_LONG).show();
            }
        });
        getConfig = findViewById(R.id.fabConfig);
        final Context mContext = this;
        getConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
                builder.setTitle("Fetch Config?");
                builder.setMessage("Do you want to fetch a pre made config for your device? Please note that this action will overwrite your current config.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotchConfig notchConfig = new NotchConfig(getApplicationContext());
                        if (notchConfig.saveNotchData()) {
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ConfigurationScreen.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Device not supported!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setCancelable(false);
                builder.show();
            }
        });
        fullStatus = findViewById(R.id.cbFullProgress);

        batteryColorView = findViewById(R.id.rvBatteryColor);
    }

    private void loadRecycler() {
        batteryColorView.setHasFixedSize(true);
        batteryColorView.setLayoutManager(new LinearLayoutManager(this));
        list = dataManager.getColorLevels();
        batteryColorAdapter = new BatteryColorAdapter(getApplicationContext(), list);
        batteryColorView.setAdapter(batteryColorAdapter);
    }



    @Override
    public void onColorSet(String color) {
        ColorLevel prev = list.get(positionTouch);
        prev.setColor(color);
        list.set(positionTouch, prev);
        batteryColorAdapter.notifyItemChanged(positionTouch);
        dataManager.setColorLevels(list);
        dataManager.save();
    }

    @Override
    public void onTouchItem(int position) {
        positionTouch = position;
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.color = list.get(position).getColor();
        colorPicker.show(getSupportFragmentManager(), "color picker");
    }
}
