package com.linh.permissionhandler;

import com.linh.permissionhandler.model.RequestPermissionResult;

public interface RequestPermissionListener {
    void onResult(RequestPermissionResult result);
}