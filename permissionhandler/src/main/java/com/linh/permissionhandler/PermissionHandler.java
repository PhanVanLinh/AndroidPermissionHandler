package com.linh.permissionhandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import com.linh.permissionhandler.model.RPermission;
import com.linh.permissionhandler.model.RequestPermissionResult;
import com.linh.permissionhandler.screen.RequestRuntimePermissionActivity;
import com.linh.permissionhandler.util.Constant;
import com.linh.permissionhandler.util.Extras;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PhanVanLinh on 11/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public class PermissionHandler {
    private Context context;
    private RPermission[] permissions;
    @Nullable
    private RequestPermissionListener listener;
    private boolean allowRequestDontAskAgainPermission;

    @SuppressWarnings("unused")
    private PermissionHandler() {
        // do nothing
    }

    private PermissionHandler(Builder builder) {
        this.context = builder.context;
        this.listener = builder.listener;
        this.permissions = builder.permissions;
        this.allowRequestDontAskAgainPermission = builder.allowRequestDontAskAgainPermission;
    }

    public void request() {
        if (!needRequestRuntimePermissions()) {
            notifyAllPermissionGranted();
            return;
        }
        RPermission[] unGrantedPermissions = findUnGrantedPermissions(permissions);
        if (unGrantedPermissions.length == 0) {
            notifyAllPermissionGranted();
            return;
        }
        requestUnGrantedPermissions(unGrantedPermissions, allowRequestDontAskAgainPermission);
    }

    private boolean needRequestRuntimePermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean isPermissionGranted(String permission) {
        return ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private RPermission[] findGrantedPermissions(RPermission[] permissions) {
        List<RPermission> grantedPermissions = new ArrayList<>();
        for (RPermission permission : permissions) {
            if (isPermissionGranted(permission.getPermission())) {
                grantedPermissions.add(permission);
            }
        }
        return grantedPermissions.toArray(new RPermission[0]);
    }

    private RPermission[] findUnGrantedPermissions(RPermission[] permissions) {
        List<RPermission> unGrantedPermissions = new ArrayList<>();
        for (RPermission permission : permissions) {
            if (!isPermissionGranted(permission.getPermission())) {
                unGrantedPermissions.add(permission);
            }
        }
        return unGrantedPermissions.toArray(new RPermission[0]);
    }

    private void updatePermissionResult(RPermission[] permissions, int result) {
        for (RPermission permission : permissions) {
            permission.setResult(result);
        }
    }

    private void requestUnGrantedPermissions(RPermission[] permissions,
            boolean ignoreNeverAskAgain) {
        Intent intent = new Intent(context, RequestRuntimePermissionActivity.class);
        intent.putExtra(Extras.EXTRAS_PERMISSIONS, permissions);
        intent.putExtra(Extras.EXTRAS_IGNORE_NEVER_ASK_AGAIN, ignoreNeverAskAgain);
        context.startActivity(intent);
        registerBroadcast();
    }

    private void registerBroadcast() {
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePermissionResult(findGrantedPermissions(permissions),
                        PackageManager.PERMISSION_GRANTED);
                notifyPermissionResult();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.REQUEST_RUNTIME_PERMISSION_ACTION);
        context.registerReceiver(mReceiver, filter);
    }

    private void notifyAllPermissionGranted() {
        updatePermissionResult(permissions, PackageManager.PERMISSION_GRANTED);
        notifyPermissionResult();
    }

    private void notifyPermissionResult() {
        if (listener != null) {
            listener.onResult(new RequestPermissionResult(permissions));
        }
    }

    public static class Builder {
        private Context context;
        private RPermission[] permissions;
        private RequestPermissionListener listener;
        private boolean allowRequestDontAskAgainPermission;

        public Builder(Context context, @NonNull RPermission[] permissions) {
            this.context = context;
            this.permissions = permissions;
        }

        public Builder setListener(RequestPermissionListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * When set allowRequestDontAskAgainPermission=true, we will open Settings to allow user enable permission
         */
        public Builder setAllowRequestDontAskAgainPermission(boolean allowRequestDontAskAgainPermission) {
            this.allowRequestDontAskAgainPermission = allowRequestDontAskAgainPermission;
            return this;
        }

        public PermissionHandler build() {
            return new PermissionHandler(this);
        }
    }
}
