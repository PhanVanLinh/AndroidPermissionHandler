package com.linh.permissionhandler.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.linh.permissionhandler.util.Constant;

public class RPermission implements Parcelable {
    private String permission;
    @Nullable
    private String rationale;
    private int result = Constant.PERMISSION_UNKNOWN;

    public RPermission(String permission, String rationale) {
        this.permission = permission;
        this.rationale = rationale;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Nullable
    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public String getSharedPrefKey() {
        return permission + "_runtime";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.permission);
        dest.writeString(this.rationale);
        dest.writeInt(this.result);
    }

    protected RPermission(Parcel in) {
        this.permission = in.readString();
        this.rationale = in.readString();
        this.result = in.readInt();
    }

    public static final Creator<RPermission> CREATOR = new Creator<RPermission>() {
        @Override
        public RPermission createFromParcel(Parcel source) {
            return new RPermission(source);
        }

        @Override
        public RPermission[] newArray(int size) {
            return new RPermission[size];
        }
    };
}