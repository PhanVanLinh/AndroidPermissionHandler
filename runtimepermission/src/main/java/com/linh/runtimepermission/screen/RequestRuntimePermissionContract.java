package com.linh.runtimepermission.screen;

import android.content.DialogInterface;
import com.linh.runtimepermission.model.RPermission;

/**
 * Created by PhanVanLinh on 14/05/2018.
 * phanvanlinh.94vn@gmail.com
 */

public interface RequestRuntimePermissionContract {
    interface View {
        void showRationale(String rationale, final DialogInterface.OnClickListener onClickListener);

        void openSetting();

        void request(RPermission[] permissions);

        boolean shouldShowPermissionRationale(String permission);
    }

    interface Presenter {

        void start(RPermission[] unGrantedPermissions);

        boolean isPermissionRequestedBefore(RPermission permission);

        void markPermissionRequested(RPermission[] permissions);

        String buildRationaleMessage(RPermission[] permissions);
    }
}
