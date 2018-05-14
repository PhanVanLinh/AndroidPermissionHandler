package com.linh.runtimepermission.screen;

import android.content.DialogInterface;
import com.linh.runtimepermission.model.RPermission;
import com.linh.runtimepermission.util.SharedPreferenceApi;

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
    public void start(RPermission[] unGrantedPermissions) {
        handleRequest(unGrantedPermissions, ignoreDontAskAgain);
    }

    private void handleRequest(final RPermission[] permissions, boolean ignoreDontAskAgain) {
        if (ignoreDontAskAgain) {
            if (hasDontAskAgainPermission(permissions)) {
                handleRequestPermissionSetting(permissions);
                return;
            }
        }
        if (hasShouldShowRationalePermission(permissions)) {
            handleRequestPermissionDialog(permissions);
            return;
        }
        view.request(permissions);
    }

    private boolean hasShouldShowRationalePermission(RPermission[] permissions) {
        for (RPermission permission : permissions) {
            if (view.shouldShowPermissionRationale(permission.getPermission())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDontAskAgainPermission(RPermission[] permissions) {
        for (RPermission permission : permissions) {
            if (!view.shouldShowPermissionRationale(permission.getPermission())
                    && isPermissionRequestedBefore(permission)) {
                return true;
            }
        }
        return false;
    }

    private void handleRequestPermissionDialog(final RPermission[] permissions) {
        String rationale = buildRationaleMessage(permissions);
        if (rationale.isEmpty()) {
            view.request(permissions);
        } else {
            view.showRationale(rationale, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    view.request(permissions);
                }
            });
        }
    }

    private void handleRequestPermissionSetting(RPermission[] permissions) {
        String rationale = buildRationaleMessage(permissions);
        if (rationale.isEmpty()) {
            view.openSetting();
        } else {
            view.showRationale(rationale, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    view.openSetting();
                }
            });
        }
    }

    @Override
    public void markPermissionRequested(RPermission[] permissions) {
        for (RPermission permission : permissions) {
            sharedPreferenceApi.put(permission.getSharedPrefKey(), true);
        }
    }

    @Override
    public boolean isPermissionRequestedBefore(RPermission permission) {
        return sharedPreferenceApi.get(permission.getSharedPrefKey(), Boolean.class, false);
    }

    @Override
    public String buildRationaleMessage(RPermission[] permissions) {
        StringBuilder rationale = new StringBuilder();
        for (RPermission permission : permissions) {
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
