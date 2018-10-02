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
import com.linh.permissionhandler.model.RequestPermissionResult;
import com.linh.permissionhandler.model.RuntimePermission;
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
    private RuntimePermission[] permissions;
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
        RuntimePermission[] unGrantedPermissions = findUnGrantedPermissions(permissions);
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

    private RuntimePermission[] findGrantedPermissions(RuntimePermission[] permissions) {
        List<RuntimePermission> grantedPermissions = new ArrayList<>();
        for (RuntimePermission permission : permissions) {
            if (isPermissionGranted(permission.getPermission())) {
                grantedPermissions.add(permission);
            }
        }
        return grantedPermissions.toArray(new RuntimePermission[0]);
    }

    private RuntimePermission[] findUnGrantedPermissions(RuntimePermission[] permissions) {
        List<RuntimePermission> unGrantedPermissions = new ArrayList<>();
        for (RuntimePermission permission : permissions) {
            if (!isPermissionGranted(permission.getPermission())) {
                unGrantedPermissions.add(permission);
            }
        }
        return unGrantedPermissions.toArray(new RuntimePermission[0]);
    }

    private void updatePermissionResult(RuntimePermission[] permissions, int result) {
        for (RuntimePermission permission : permissions) {
            permission.setResult(result);
        }
    }

    private void requestUnGrantedPermissions(RuntimePermission[] permissions,
            boolean ignoreNeverAskAgain) {
        Intent intent = new Intent(context, RequestRuntimePermissionActivity.class);
        intent.putExtra(Extras.EXTRAS_PERMISSIONS, permissions);
        intent.putExtra(Extras.EXTRAS_IGNORE_NEVER_ASK_AGAIN, ignoreNeverAskAgain);
        context.startActivity(intent);
        registerBroadcast();
    }

    private void registerBroadcast() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePermissionResult(findGrantedPermissions(permissions),
                        PackageManager.PERMISSION_GRANTED);
                notifyPermissionResult();
                context.unregisterReceiver(this);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.REQUEST_RUNTIME_PERMISSION_ACTION);
        context.registerReceiver(receiver, filter);
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
        private RuntimePermission[] permissions;
        private RequestPermissionListener listener;
        private boolean allowRequestDontAskAgainPermission;

        public Builder(Context context, @NonNull RuntimePermission[] permissions) {
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
        public Builder setAllowRequestDontAskAgainPermission(
                boolean allowRequestDontAskAgainPermission) {
            this.allowRequestDontAskAgainPermission = allowRequestDontAskAgainPermission;
            return this;
        }

        public PermissionHandler build() {
            return new PermissionHandler(this);
        }
    }
}
