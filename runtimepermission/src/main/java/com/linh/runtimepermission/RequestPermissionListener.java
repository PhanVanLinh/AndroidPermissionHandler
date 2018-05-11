package com.linh.runtimepermission;

import com.linh.runtimepermission.model.RequestPermissionResult;

public interface RequestPermissionListener {
    void onResult(RequestPermissionResult result);
}