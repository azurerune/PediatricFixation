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
    int mAltViewIndex;
    boolean mSoundSwitch;
    boolean mPro;
    boolean mFirstTime;
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
        mAltViewIndex = s.getInt("alt_view_index", 0);
        mSoundSwitch = s.getBoolean("sound_switch", false);
        mPro = s.getBoolean("pro", false);
        mFirstTime = s.getBoolean("first_time", true);
        // TODO: load site license info
        mSiteLicense = new LicenseInfo();
        mSiteLicense.fromSiteLicense = s.getBoolean("from_site_license", false);
    }

    public void saveSettings() {
        SharedPreferences s = c.getSharedPreferences("PediatricFixation", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putInt("major_view_type", mMajorViewType);
        e.putInt("view_index", mViewIndex);
        e.putInt("alt_view_index", mAltViewIndex);
        e.putBoolean("sound_switch", mSoundSwitch);
        e.putBoolean("pro", mPro);
        e.putBoolean("from_site_license", mSiteLicense.fromSiteLicense);
        e.putBoolean("first_time", false);
        e.commit();

    }

    public int getMajorViewType() {
        return mMajorViewType;
    }
    public void setMajorViewType(int majorviewtype) {
        mMajorViewType = majorviewtype;
    }

    public int getViewIndex() {
        return mViewIndex;
    }
    public void setViewIndex(int viewindex) {
        mViewIndex = viewindex;
    }

    public int getAltViewIndex() {
        return mAltViewIndex;
    }
    public void setAltViewIndex(int nearviewindex) {
        mAltViewIndex = nearviewindex;
    }

    public boolean getSoundSwitch() {
        return mSoundSwitch;
    }
    public void setSoundSwitch(boolean soundswitch) {
        mSoundSwitch = soundswitch;
    }

    public boolean getPro() {
        return mPro;
    }
    public void setPro(boolean pro) {
        mPro = pro;
    }

    public LicenseInfo getLicenseInfo() {
        return mSiteLicense;
    }
    public void setLicenseInfo(LicenseInfo l) {
        mSiteLicense = l;
    }

    public boolean getFirstTime() {
        return mFirstTime;
    }
}

