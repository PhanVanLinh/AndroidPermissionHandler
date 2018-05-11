package com.linh.androidruntimepermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.linh.runtimepermission.RequestPermissionListener;
import com.linh.runtimepermission.RuntimePermissionHandler;
import com.linh.runtimepermission.model.RPermission;
import com.linh.runtimepermission.model.RequestPermissionResult;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RPermission[] permissions = new RPermission[] {
                        new RPermission(Manifest.permission.READ_CONTACTS, "a"),
                        new RPermission(Manifest.permission.READ_CALENDAR, "b")
                };
                new RuntimePermissionHandler.Builder(MainActivity.this, permissions).setListener(
                        new RequestPermissionListener() {
                            @Override
                            public void onResult(RequestPermissionResult result) {
                                if (result.isAllPermissionGranted()) {
                                    Toast.makeText(MainActivity.this, "All permission granted",
                                            Toast.LENGTH_SHORT).show();
                                } else if (result.isAllPermissionDenied()) {
                                    Toast.makeText(MainActivity.this, "All permission denied",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    for (RPermission permission : result.getPermissions()) {
                                        if (permission.getResult()
                                                == PackageManager.PERMISSION_GRANTED) {
                                            Toast.makeText(MainActivity.this,
                                                    permission.getPermission() + " granted",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this,
                                                    permission.getPermission() + " denied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }).build().request();
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            Log.i(TAG, "requestPermission: " + ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_CONTACTS));
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(MainActivity.this).setMessage("S")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] { Manifest.permission.READ_CONTACTS },
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.READ_CONTACTS },
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Toast.makeText(MainActivity.this, "Permission has already been granted",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: " + requestCode);
    }
}
