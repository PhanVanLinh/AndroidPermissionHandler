package com.linh.androidpermissionhandler;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.linh.androidpermissionhandler.constant.Constant;
import com.linh.permissionhandler.PermissionHandler;
import com.linh.permissionhandler.RequestPermissionListener;
import com.linh.permissionhandler.model.RPermission;
import com.linh.permissionhandler.model.RequestPermissionResult;

public class ComplexSampleActivity extends AppCompatActivity {
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_sample);
        tvResult = findViewById(R.id.text_result);

        findViewById(R.id.button_test_granted_single_permission).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testGrantedSinglePermission();
                    }
                });

        findViewById(R.id.button_test_denied_single_permission).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testDeniedSinglePermission();
                    }
                });

        findViewById(R.id.button_test_granted_multiple_permission).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testGrantedMultiplePermission();
                    }
                });

        findViewById(R.id.button_test_denied_multiple_permission).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testDeniedMultiplePermission();
                    }
                });
    }

    private void testGrantedSinglePermission() {
        RPermission[] permissions = new RPermission[] {
                new RPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        "We need ACCESS_COARSE_LOCATION permission because ...")
        };
        new PermissionHandler.Builder(this, permissions).setAllowRequestDontAskAgainPermission(true)
                .setListener(new RequestPermissionListener() {
                    @Override
                    public void onResult(RequestPermissionResult result) {
                        tvResult.setText(
                                result.isAllGranted() ? Constant.ALL_PERMISSION_GRANTED
                                        : Constant.ALL_PERMISSION_DENIED);
                    }
                })
                .build()
                .request();
    }

    private void testDeniedSinglePermission() {
        RPermission[] permissions = new RPermission[] {
                new RPermission(Manifest.permission.CALL_PHONE,
                        "We need CALL_PHONE permission because ...")
        };
        new PermissionHandler.Builder(this, permissions).setAllowRequestDontAskAgainPermission(true)
                .setListener(new RequestPermissionListener() {
                    @Override
                    public void onResult(RequestPermissionResult result) {
                        tvResult.setText(
                                result.isAllGranted() ? Constant.ALL_PERMISSION_GRANTED
                                        : Constant.ALL_PERMISSION_DENIED);
                    }
                })
                .build()
                .request();
    }

    private void testGrantedMultiplePermission() {
        RPermission[] permissions = new RPermission[] {
                new RPermission(Manifest.permission.CAMERA,
                        "We need camera permission because ..."),
                new RPermission(Manifest.permission.GET_ACCOUNTS,
                        "We need account permission because ...")
        };
        new PermissionHandler.Builder(this, permissions).setAllowRequestDontAskAgainPermission(true)
                .setListener(new RequestPermissionListener() {
                    @Override
                    public void onResult(RequestPermissionResult result) {
                        tvResult.setText(
                                result.isAllGranted() ? Constant.ALL_PERMISSION_GRANTED
                                        : Constant.ALL_PERMISSION_DENIED);
                    }
                })
                .build()
                .request();
    }

    private void testDeniedMultiplePermission() {
        RPermission[] permissions = new RPermission[] {
                new RPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        "We need WRITE_EXTERNAL_STORAGE permission because ..."),
                new RPermission(Manifest.permission.SEND_SMS,
                        "We need SEND_SMS permission because ...")
        };
        new PermissionHandler.Builder(this, permissions).setAllowRequestDontAskAgainPermission(true)
                .setListener(new RequestPermissionListener() {
                    @Override
                    public void onResult(RequestPermissionResult result) {
                        tvResult.setText(
                                result.isAllGranted() ? Constant.ALL_PERMISSION_GRANTED
                                        : Constant.ALL_PERMISSION_DENIED);
                    }
                })
                .build()
                .request();
    }
}