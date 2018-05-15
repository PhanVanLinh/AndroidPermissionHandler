package com.linh.permissionhandler.screen;

import android.content.DialogInterface;
import com.linh.permissionhandler.model.RPermission;

/**
 * Created by PhanVanLinh on 14/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public interface RequestRuntimePermissionContract {
    interface View {
        void showRationale(String rationale, final DialogInterface.OnClickListener onClickListener);

        void requestPermissionInSetting();

        void requestPermissionsInApp(RPermission[] permissions);

        boolean shouldShowPermissionRationale(String permission);

        void sendResultAndFinish();
    }

    interface Presenter {

        void start(RPermission[] unGrantedPermissions);

        boolean isPermissionRequestedBefore(RPermission permission);

        void markPermissionRequested(RPermission[] permissions);

        String buildRationaleMessage(RPermission[] permissions);
    }
}
