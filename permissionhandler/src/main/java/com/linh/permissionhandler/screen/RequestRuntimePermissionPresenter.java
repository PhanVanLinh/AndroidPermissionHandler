package com.linh.permissionhandler.screen;

import android.content.DialogInterface;
import com.linh.permissionhandler.model.RuntimePermission;
import com.linh.permissionhandler.util.SharedPreferenceApi;

/**
 * Created by PhanVanLinh on 14/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public class RequestRuntimePermissionPresenter
        implements RequestRuntimePermissionContract.Presenter {
    private SharedPreferenceApi sharedPreferenceApi;
    private RequestRuntimePermissionContract.View view;
    private boolean ignoreDontAskAgain;

    RequestRuntimePermissionPresenter(RequestRuntimePermissionContract.View view,
            boolean ignoreDontAskAgain, SharedPreferenceApi sharedPreferenceApi) {
        this.sharedPreferenceApi = sharedPreferenceApi;
        this.view = view;
        this.ignoreDontAskAgain = ignoreDontAskAgain;
    }

    @Override
    public void start(RuntimePermission[] unGrantedPermissions) {
        handleRequest(unGrantedPermissions, ignoreDontAskAgain);
    }

    private void handleRequest(final RuntimePermission[] permissions, boolean ignoreDontAskAgain) {
        if (hasDontAskAgainPermission(permissions)) {
            if (ignoreDontAskAgain) {
                requestPermissionInSetting(permissions);
                return;
            }
            if (countDontAskAgainPermission(permissions) == permissions.length) {
                view.sendResultAndFinish();
                return;
            }
        }
        requestPermissionInApp(permissions);
    }

    private boolean hasShouldShowRationalePermission(RuntimePermission[] permissions) {
        for (RuntimePermission permission : permissions) {
            if (view.shouldShowPermissionRationale(permission.getPermission())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDontAskAgainPermission(RuntimePermission[] permissions) {
        return countDontAskAgainPermission(permissions) > 0;
    }

    private int countDontAskAgainPermission(RuntimePermission[] permissions) {
        int count = 0;
        for (RuntimePermission permission : permissions) {
            if (!view.shouldShowPermissionRationale(permission.getPermission())
                    && isPermissionRequestedBefore(permission)) {
                count++;
            }
        }
        return count;
    }

    private void requestPermissionInSetting(RuntimePermission[] permissions) {
        String rationale = buildRationaleMessage(permissions);
        if (rationale.isEmpty()) {
            view.requestPermissionInSetting();
        } else {
            view.showRationale(rationale, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    view.requestPermissionInSetting();
                }
            });
        }
    }

    private void requestPermissionInApp(final RuntimePermission[] permissions) {
        if (hasShouldShowRationalePermission(permissions)) {
            String rationale = buildRationaleMessage(permissions);
            if (rationale.isEmpty()) {
                view.requestPermissionsInApp(permissions);
            } else {
                view.showRationale(rationale, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.requestPermissionsInApp(permissions);
                    }
                });
            }
        } else {
            view.requestPermissionsInApp(permissions);
        }
    }

    @Override
    public void markPermissionRequested(RuntimePermission[] permissions) {
        for (RuntimePermission permission : permissions) {
            sharedPreferenceApi.put(permission.getSharedPrefKey(), true);
        }
    }

    @Override
    public boolean isPermissionRequestedBefore(RuntimePermission permission) {
        return sharedPreferenceApi.get(permission.getSharedPrefKey(), Boolean.class, false);
    }

    @Override
    public String buildRationaleMessage(RuntimePermission[] permissions) {
        StringBuilder rationale = new StringBuilder();
        for (RuntimePermission permission : permissions) {
            if (permission.getRationale() != null) {
                if (rationale.length() > 0) {
                    rationale.append("\n");
                }
                rationale.append(permission.getRationale());
            }
        }
        return rationale.toString();
    }
}
