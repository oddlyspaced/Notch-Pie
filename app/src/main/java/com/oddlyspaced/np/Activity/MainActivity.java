package com.oddlyspaced.np.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.oddlyspaced.np.R;
import com.oddlyspaced.np.Utils.ColorLevel;
import com.oddlyspaced.np.Utils.DataManager;
import com.oddlyspaced.np.Utils.NotchConfig;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final int CODE_STORAGE_PERMISSION = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            showStorageAlert();
        } else {
            checkOverlay();
        }
    }

    private void checkOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            showOverlayAlert();
            finish();
        }
        else {
            startActivity(new Intent(this, ConfigurationScreen.class));
            finish();
        }
    }

    private void showStorageAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setPositiveButton(getString(R.string.popup_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showStoragePopup();
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.popup_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setMessage(getString(R.string.alert_storage_description));
        builder.setTitle(R.string.alert_storage_title);
        builder.show();
    }

    private void showStoragePopup() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);

    }

    private void showOverlayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setPositiveButton(getString(R.string.popup_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showOverlayPopup();
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton(getString(R.string.popup_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setMessage(getString(R.string.alert_overlay_description));
        builder.setTitle(getString(R.string.alert_overlay_title));
        builder.show();
    }

    private void showOverlayPopup() {
        Toast.makeText(getApplicationContext(), getString(R.string.alert_overlay_request), Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, ConfigurationScreen.class));
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CODE_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showOverlayAlert();
                } else {
                    finish();
                }
            }
        }
    }
}