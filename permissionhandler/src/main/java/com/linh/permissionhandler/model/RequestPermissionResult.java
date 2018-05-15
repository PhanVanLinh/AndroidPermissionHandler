package com.linh.permissionhandler.model;

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

    public boolean isAllGranted() {
        for (RPermission permission : permissions) {
            if (permission.getResult() != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllDenied() {
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
