package com.eyetools.pediatrics.pediatricfixation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */

public class MainFixationFragment extends Fragment {
    static String TAG = "PediatricFixation";

    // UI elements
    Switch mSoundSwitch;
    MainViewPager mViewPager;
    MainViewPagerAdapter mPagerAdapter;
    MainSurfaceView mSurfaceView;


    // Application variables

    int mMajorViewType;
    int mViewIndex;
    boolean mSound;
    boolean mSoundPlaying = false;
    boolean mPro;
    PrefManager.LicenseInfo mLicenseInfo;
    PrefManager prefManager;


    public MainFixationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_fixation, container, false);

        /* load preferences */
        prefManager = new PrefManager(getActivity());
        mMajorViewType = prefManager.getMajorViewType();
        mViewIndex = prefManager.getViewIndex();
        mSound = prefManager.getSoundSwitch();
        mPro = prefManager.getPro();
        mLicenseInfo = prefManager.getLicenseInfo();


        /* get UI elements */
        mSoundSwitch = (Switch)v.findViewById(R.id.sound_switch);
        mViewPager = (MainViewPager)v.findViewById(R.id.viewpager);
        mPagerAdapter = new MainViewPagerAdapter(this, mViewPager);
        mViewPager.setAdapter(mPagerAdapter);


        return v;

    }

    int getViewCount() {
        if (mMajorViewType == 0) {
            return 3;
        } else {
            return 0;
        }
    }

    class MainViewPagerAdapter extends PagerAdapter {

        MainFixationFragment p;
        Activity a;
        MainViewPager mViewPager;


        public MainViewPagerAdapter(Fragment p, MainViewPager pager) {
            this.p = (MainFixationFragment)p;
            this.a = p.getActivity();
            mViewPager = pager;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MainSurfaceView newSurfaceView = new MainSurfaceView(a);
            newSurfaceView.initSurface(position);
            Log.i(TAG, "instantitate Surface " + String.valueOf(position));
            return newSurfaceView;
        }

        @Override
        public int getCount() {
            return p.getViewCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (view == object) {
                return true;
            }
            return false;
        }

        public void clearAllViews() {
            //TODO: mViewPager.removeAllViews();
        }
    }
}
