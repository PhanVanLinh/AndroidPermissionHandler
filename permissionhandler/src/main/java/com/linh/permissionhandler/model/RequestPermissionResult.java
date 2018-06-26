package com.linh.permissionhandler.model;

import android.content.pm.PackageManager;

/**
 * Created by PhanVanLinh on 14/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public class RequestPermissionResult {
    private RuntimePermission[] permissions;

    public RequestPermissionResult(RuntimePermission[] permissions) {
        this.permissions = permissions;
    }

    public boolean isAllGranted() {
        for (RuntimePermission permission : permissions) {
            if (permission.getResult() != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllDenied() {
        for (RuntimePermission permission : permissions) {
            if (permission.getResult() == PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public RuntimePermission[] getPermissions() {
        return permissions;
    }
}
