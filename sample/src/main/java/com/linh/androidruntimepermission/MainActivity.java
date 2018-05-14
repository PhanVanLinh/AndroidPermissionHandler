package com.linh.androidruntimepermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.linh.runtimepermission.RequestPermissionListener;
import com.linh.runtimepermission.RuntimePermissionHandler;
import com.linh.runtimepermission.model.RPermission;
import com.linh.runtimepermission.model.RequestPermissionResult;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

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
                                    Toast.makeText(MainActivity.this, Constant.ALL_PERMISSION_GRANTED,
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
}
