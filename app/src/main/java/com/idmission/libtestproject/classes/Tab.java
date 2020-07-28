package com.idmission.libtestproject.classes;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class Tab implements Serializable, Parcelable {

    private String tabName;
    private Fragment fragment;

    public Tab(String tabName, Fragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    protected Tab(Parcel in) {
        tabName = in.readString();
    }

    public static final Creator<Tab> CREATOR = new Creator<Tab>() {
        @Override
        public Tab createFromParcel(Parcel in) {
            return new Tab(in);
        }

        @Override
        public Tab[] newArray(int size) {
            return new Tab[size];
        }
    };

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Tab)) return false;
        Tab o = (Tab) obj;
        return o.tabName == this.tabName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tabName);
    }
}
