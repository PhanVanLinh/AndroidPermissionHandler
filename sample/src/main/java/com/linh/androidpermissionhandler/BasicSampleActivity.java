package com.linh.androidpermissionhandler;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.linh.androidpermissionhandler.constant.Constant;
import com.linh.permissionhandler.PermissionHandler;
import com.linh.permissionhandler.RequestPermissionListener;
import com.linh.permissionhandler.model.RPermission;
import com.linh.permissionhandler.model.RequestPermissionResult;

public class BasicSampleActivity extends AppCompatActivity {
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_sample);
        tvResult = findViewById(R.id.text_result);
        findViewById(R.id.button_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestContactAndCalendarPermission();
            }
        });
    }

    private void requestContactAndCalendarPermission() {
        RPermission[] permissions = new RPermission[] {
                new RPermission(Manifest.permission.READ_CONTACTS,
                        "We need READ_CONTACTS because ..."),
                new RPermission(Manifest.permission.READ_CALENDAR,
                        "We need READ_CALENDAR because ...")
        };
        new PermissionHandler.Builder(BasicSampleActivity.this, permissions).setAllowRequestDontAskAgainPermission(
                true).setListener(new RequestPermissionListener() {
            @Override
            public void onResult(RequestPermissionResult result) {
                if (result.isAllGranted()) {
                    tvResult.setText(Constant.ALL_PERMISSION_GRANTED);
                } else if (result.isAllDenied()) {
                    tvResult.setText(Constant.ALL_PERMISSION_DENIED);
                } else {
                    StringBuilder tmpResult = new StringBuilder();
                    for (RPermission permission : result.getPermissions()) {
                        if (permission.getResult() == PackageManager.PERMISSION_GRANTED) {
                            tmpResult.append(permission.getPermission()).append("granted\n");
                        } else {
                            tmpResult.append(permission.getPermission()).append("denied\n");
                        }
                        tvResult.setText(tmpResult);
                    }
                }
            }
        }).build().request();
    }
}
