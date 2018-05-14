package com.linh.androidruntimepermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.linh.runtimepermission.RequestPermissionListener;
import com.linh.runtimepermission.RuntimePermissionHandler;
import com.linh.runtimepermission.model.RPermission;
import com.linh.runtimepermission.model.RequestPermissionResult;

public class IgnoreDontAskAgainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.text_status);

        findViewById(R.id.button_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RPermission[] permissions = new RPermission[] {
                        new RPermission(Manifest.permission.READ_CONTACTS, "a")
                        , new RPermission(Manifest.permission.READ_CALENDAR, "b")
                };
                new RuntimePermissionHandler.Builder(IgnoreDontAskAgainActivity.this,
                        permissions).setIgnoreNeverAskAgain(true)
                        .setListener(new RequestPermissionListener() {
                            @Override
                            public void onResult(RequestPermissionResult result) {
                                if (result.isAllPermissionGranted()) {
                                    tvStatus.setText(Constant.ALL_PERMISSION_GRANTED);
                                } else if (result.isAllPermissionDenied()) {
                                    tvStatus.setText(Constant.ALL_PERMISSION_DENIED);
                                } else {
                                    for (RPermission permission : result.getPermissions()) {
                                        if (permission.getResult()
                                                == PackageManager.PERMISSION_GRANTED) {
                                            Toast.makeText(IgnoreDontAskAgainActivity.this,
                                                    permission.getPermission() + " granted",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(IgnoreDontAskAgainActivity.this,
                                                    permission.getPermission() + " denied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        })
                        .build()
                        .request();
            }
        });
    }
}
