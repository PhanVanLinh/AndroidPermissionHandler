package com.linh.permissionhandler.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;
import com.linh.permissionhandler.util.Constant;
import com.linh.permissionhandler.model.RuntimePermission;
import com.linh.permissionhandler.util.Extras;
import com.linh.permissionhandler.util.SharedPreferenceApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestRuntimePermissionActivity extends AppCompatActivity
        implements RequestRuntimePermissionContract.View {
    private int REQUEST_PERMISSION_IN_SETTING = 101;
    private int REQUEST_PERMISSION_IN_APP = 100;
    private RequestRuntimePermissionContract.Presenter presenter;
    private boolean hasShowedRationale;
    private RuntimePermission[] unGrantedPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        Parcelable[] parcelableArray = getIntent().getParcelableArrayExtra(Extras.EXTRAS_PERMISSIONS);
        unGrantedPermissions = Arrays.copyOf(parcelableArray, parcelableArray.length, RuntimePermission[].class);
        boolean ignoreDontAskAgain =
                getIntent().getBooleanExtra(Extras.EXTRAS_IGNORE_NEVER_ASK_AGAIN, false);

        presenter = new RequestRuntimePermissionPresenter(this, ignoreDontAskAgain,
                new SharedPreferenceApi(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start(unGrantedPermissions);
    }

    @Override
    public boolean shouldShowPermissionRationale(@NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    public void requestPermissionsInApp(RuntimePermission[] permissions) {
        String[] permissionStr = new String[permissions.length];
        for (int i = 0, len = permissions.length; i < len; i++) {
            permissionStr[i] = permissions[i].getPermission();
        }
        presenter.markPermissionRequested(permissions);
        ActivityCompat.requestPermissions(this, permissionStr, REQUEST_PERMISSION_IN_APP);
    }

    public void requestPermissionInSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_IN_SETTING);
        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showRationale(String rationale,
            final DialogInterface.OnClickListener onClickListener) {
        hasShowedRationale = true;
        new AlertDialog.Builder(this).setMessage(rationale)
                .setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                onClickListener.onClick(dialog, which);
                            }
                        })
                .setNegativeButton(getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sendResultAndFinish();
                            }
                        })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_IN_APP) {
            boolean isAllPermissionGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isAllPermissionGranted = false;
                    break;
                }
            }
            if (!isAllPermissionGranted && !hasShowedRationale) {
                RuntimePermission[] newUnGrantedPermissions = getNewUnGrantedPermissions();
                if (!presenter.buildRationaleMessage(newUnGrantedPermissions).isEmpty()) {
                    presenter.start(newUnGrantedPermissions);
                    return;
                }
            }
            sendResultAndFinish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_IN_SETTING) {
            sendResultAndFinish();
        }
    }

    private RuntimePermission[] getNewUnGrantedPermissions() {
        List<RuntimePermission> newUnGrantedPermissions = new ArrayList<>();
        for (RuntimePermission permission : unGrantedPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission.getPermission())
                    != PackageManager.PERMISSION_GRANTED) {
                newUnGrantedPermissions.add(permission);
            }
        }
        return newUnGrantedPermissions.toArray(new RuntimePermission[0]);
    }

    @Override
    public void sendResultAndFinish() {
        Intent intent = new Intent();
        intent.setAction(Constant.REQUEST_RUNTIME_PERMISSION_ACTION);
        sendBroadcast(intent);
        finish();
    }
}
