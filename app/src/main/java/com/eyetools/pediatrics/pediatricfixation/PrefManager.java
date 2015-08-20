package com.eyetools.pediatrics.pediatricfixation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xu on 8/19/15.
 */
public class PrefManager {
    Activity c;
    int mMajorViewType;
    int mViewIndex;
    boolean mSoundSwitch;
    boolean mPro;
    public class LicenseInfo {
        boolean fromSiteLicense = false;

    }
    LicenseInfo mSiteLicense = null;

    public PrefManager (Activity c) {
        this.c = c;
    }

    public void loadSettings() {
        SharedPreferences s = c.getSharedPreferences("PediatricFixation", Context.MODE_PRIVATE);
        mMajorViewType = s.getInt("major_view_type", 0);
        mViewIndex = s.getInt("view_index", 0);
        mSoundSwitch = s.getBoolean("sound_switch", false);
        mPro = s.getBoolean("pro", false);
        // TODO: load site license info
    }

    public void saveSettings() {

    }

    public int getMajorViewType() {
        return mMajorViewType;
    }

    public int getViewIndex() {
        return mViewIndex;
    }

    public boolean getSoundSwitch() {
        return mSoundSwitch;
    }

    public boolean getPro() {
        return mPro;
    }

    public LicenseInfo getLicenseInfo() {
        return mSiteLicense;
    }
}

