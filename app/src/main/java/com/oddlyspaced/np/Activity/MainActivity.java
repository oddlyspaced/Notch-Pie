package com.oddlyspaced.np.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.oddlyspaced.np.R;

// Main Activity for app
// App starts from here
public class MainActivity extends AppCompatActivity {

    // permission request code for storage access
    private static final int CODE_STORAGE_PERMISSION = 1024;
    // Permission request code to draw over other apps
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start permission checking process
        checkPermissions();
    }

    // this method checks for all permissions
    private void checkPermissions() {
        if (!isStoragePermissionGranted()) {
            notifyStoragePermission();
        } else if (!canDrawOverlay()) {
            notifyOverlayPermission();
        } else {
            startActivity(new Intent(this, ConfigurationScreen.class));
            finish();
        }
    }

    // this method returns true is permission is granted
    private boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    // this method shows an alert dialog notifying the user about storage access
    private void notifyStoragePermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setPositiveButton(getString(R.string.popup_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestStoragePermission();
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

    // this method requests the storage runtime permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);
    }

    // this method check if the app is able to draw overlays
    private boolean canDrawOverlay() {
        return Settings.canDrawOverlays(this);
    }

    // this method shows an alert dialog asking for the permission to draw over other apps
    private void notifyOverlayPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setPositiveButton(getString(R.string.popup_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestOverlayPermission();
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

    // this method opens the activity to allow permission granting
    private void requestOverlayPermission() {
        Toast.makeText(getApplicationContext(), getString(R.string.alert_overlay_request), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
    }

    // this method is called when the app comes to foreground
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    // this method is called when a runtime permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // storage permission granted
        if (requestCode == CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
            } else {
                // close the app
                finish();
            }
        }
    }
}