package com.linh.runtimepermission.model;

import android.content.pm.PackageManager;

/**
 * Created by PhanVanLinh on 14/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public class RequestPermissionResult {
    private RPermission[] permissions;

    public RequestPermissionResult(RPermission[] permissions) {
        this.permissions = permissions;
    }

    public boolean isAllPermissionGranted() {
        for (RPermission permission : permissions) {
            if (permission.getResult() != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllPermissionDenied() {
        for (RPermission permission : permissions) {
            if (permission.getResult() == PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public RPermission[] getPermissions() {
        return permissions;
    }
}
