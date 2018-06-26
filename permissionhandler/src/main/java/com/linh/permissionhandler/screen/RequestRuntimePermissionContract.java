package com.linh.permissionhandler.screen;

import android.content.DialogInterface;
import com.linh.permissionhandler.model.RuntimePermission;

/**
 * Created by PhanVanLinh on 14/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public interface RequestRuntimePermissionContract {
    interface View {
        void showRationale(String rationale, final DialogInterface.OnClickListener onClickListener);

        void requestPermissionInSetting();

        void requestPermissionsInApp(RuntimePermission[] permissions);

        boolean shouldShowPermissionRationale(String permission);

        void sendResultAndFinish();
    }

    interface Presenter {

        void start(RuntimePermission[] unGrantedPermissions);

        boolean isPermissionRequestedBefore(RuntimePermission permission);

        void markPermissionRequested(RuntimePermission[] permissions);

        String buildRationaleMessage(RuntimePermission[] permissions);
    }
}
