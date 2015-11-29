package com.eyetools.pediatrics.pediatricfixation;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.eyetools.pediatrics.pediatricfixation.util.IabHelper;
import com.eyetools.pediatrics.pediatricfixation.util.IabResult;
import com.eyetools.pediatrics.pediatricfixation.util.Inventory;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */

public class MainFixationFragment extends Fragment implements Switch.OnCheckedChangeListener {
    static String TAG = "PediatricFixation";

    // UI elements
    Switch soundSwitch;
    WebView gifView;
    RelativeLayout gesturePanel;
    private GestureDetector mGestureDetector;
    MediaPlayer soundPlayer;
    ToggleButton nearButton;
    IabHelper billingHelper;

    // Application variables
    MainFixation mainAct;
    int mMajorViewType;
    int mViewIndex;
    int mAltViewIndex;
    boolean mSound;
    boolean mPro;
    PrefManager.LicenseInfo mLicenseInfo;
    PrefManager prefManager;

    // Major view types
    public static int FAR = 0;
    public static int NEAR = 1;

    private static int MODE_COUNT = 2;
    ArrayList<ArrayList> mViewTypes;
    class Anim {
        public String viewType;
        public String filename;
        public int resId;

        public Anim(String viewType, String filename, int resId) {
            this.viewType = viewType;
            this.filename = filename;
            this.resId = resId;
        }
    }


    public MainFixationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_fixation, container, false);

        mainAct = (MainFixation)this.getActivity();
        /* load savedinstancestate */
        if (savedInstanceState != null) {
            // NTD
        }

        /* load preferences */
        prefManager = new PrefManager(getActivity());
        prefManager.loadSettings();
        mMajorViewType = prefManager.getMajorViewType();
        mViewIndex = prefManager.getViewIndex();
        mAltViewIndex = prefManager.getAltViewIndex();
        mSound = prefManager.getSoundSwitch();
        mPro = prefManager.getPro();
        mLicenseInfo = prefManager.getLicenseInfo();

        /* get UI elements */
        soundSwitch = (Switch)v.findViewById(R.id.sound_switch);
        soundSwitch.setChecked(mSound);
        soundSwitch.setOnCheckedChangeListener(this);
        nearButton = (ToggleButton)v.findViewById(R.id.near_button);
        nearButton.setChecked(mMajorViewType == NEAR);
        nearButton.setOnCheckedChangeListener(this);
        gifView = (WebView)v.findViewById(R.id.gifwebview);
        gesturePanel = (RelativeLayout)v.findViewById(R.id.touchpanel);

        mGestureDetector = new GestureDetector(mainAct, new SwipeListener());
        gesturePanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        /* define animation modes */
        mViewTypes = new ArrayList<ArrayList>(MODE_COUNT);
        // add the large GIFs
        ArrayList<Anim> viewtype0 = new ArrayList<Anim>();
        viewtype0.add(new Anim("gif", "tunnel.gif", R.raw.sfx1));
        viewtype0.add(new Anim("gif", "color2.gif", R.raw.sfx2));
        viewtype0.add(new Anim("gif", "colors.gif", R.raw.sfx6));
        viewtype0.add(new Anim("gif", "colorsquares.gif", R.raw.sfx4));
        viewtype0.add(new Anim("gif", "pinkballs.gif", R.raw.sfx5));
        viewtype0.add(new Anim("gif", "swirls.gif", R.raw.sfx6));
        viewtype0.add(new Anim("gif", "weird.gif", R.raw.sfx7));
        mViewTypes.add(viewtype0);
        // add the small GIFs
        ArrayList<Anim> viewtype1 = new ArrayList<Anim>();
        viewtype1.add(new Anim("gif", "dinosaur.gif", R.raw.sfx6));
        viewtype1.add(new Anim("gif", "smallcartoon.gif", R.raw.sfx5));
        viewtype1.add(new Anim("gif", "smallkaleidoscope.gif", R.raw.sfx4));
        viewtype1.add(new Anim("gif", "smallredanimal.gif", R.raw.sfx2));
        mViewTypes.add(viewtype1);

        // show the last used animation
        ArrayList<Anim> lastViewType = mViewTypes.get(mMajorViewType);
        showAnim(lastViewType.get(mViewIndex).filename, mMajorViewType == NEAR);

        // show first time window
        if (prefManager.getFirstTime()) {
            mainAct.showAbout();
        }

        // initialize billing system
        String base64EncodedPublicKey;
        base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl6i3vQfVZpj/bqtl23AkUHQkatMEKkuHqKUqeaybnGPxYsdrKYtcCV0jzFyQoBPFXk4kqGIFg+3+jXnd3V05Z2f6Dt2ZTmKZKrzIhgFoyvR9yHp6CwekZmBr1aZIm1yPBIxNk4uPEz5wJjtqUW8J/fqm2L539ZFsDhizWptD7rNFUfTJ3Vtu00lNk1db2a6d6FzON1l90rZ1574tjP0vSWgK9u7SYiAagukfj0UGJ5zpXUvUHKTWFXWp/5okKlSGEjf52tVnH+Wsx/HjlwyK6H87K0/UqpPctTraSJesZBgY5uB+Qe5rKZsjq+6K48yAqZTev9UmyoxUOsiCxf5rKQIDAQAB";
        billingHelper = new IabHelper(mainAct, base64EncodedPublicKey);
        billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "failed to set up InApp Billing");
                } else {
                    Log.d(TAG, "successfully st up in app billing");
                }
            }
        });

        ArrayList<String> billingQueryList = new ArrayList<String>();
        billingQueryList.add("1001");

        billingHelper.queryInventoryAsync(true, billingQueryList, new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {

            }
        });

        return v;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == soundSwitch) {
            mSound = isChecked;
            if (mSound) {
                soundOn();
            } else {
                soundOff();
            }
        } else if (buttonView == nearButton) {
            if (isChecked) {
                mMajorViewType = NEAR;
            } else {
                mMajorViewType = FAR;
            }
            int tempViewIndex = mViewIndex;
            mViewIndex = mAltViewIndex;
            mAltViewIndex = tempViewIndex;


            showAnim(((Anim)mViewTypes.get(mMajorViewType).get(mViewIndex)).filename, mMajorViewType == NEAR);
            reloadSound();
        }
    }

    private class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 75;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    return false;
                }
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                    // show next image
                    nextImage();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // show prev image
                    prevImage();
                }
            } catch (Exception e) {
                Log.d(TAG, "error onFling");
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mSound = !mSound;
            soundSwitch.setChecked(mSound);
            if (mSound) {
                soundOn();
            } else {
                soundOff();
            }

            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        /* start or restart the sound player */
        soundPlayer = MediaPlayer.create(mainAct, ((Anim)mViewTypes.get(mMajorViewType).get(mViewIndex)).resId);
        soundPlayer.setLooping(true);

        if (mSound) {
            Log.d(TAG, "onResume, plan to start sound");
            try {
                soundPlayer.start();
            } catch (Exception e) {
                Log.d(TAG, "error onResume starting sound player");
            }
        } else {
            Log.d(TAG, "onResume, no plan to start sound");
        }
    }

    private void soundOn() {
        if (soundPlayer == null) {
                    /* don't try to reinstantiate soundPlayer, likely reason why its been destroyed
                    is because application getting unloaded before the tap handler has been unloaded
                     */
            return;
        }
        try {
            soundPlayer.start();
        } catch (Exception e) {
            mSound = false;
            soundSwitch.setChecked(false);
            Log.d(TAG, "error toggleSound false");
        }
    }

    private void soundOff() {
        try {
            soundPlayer.pause();
        } catch (Exception e) {
            Log.d(TAG, "error toggleSound true");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        try {
            soundPlayer.stop();
            soundPlayer.release();
            soundPlayer = null;
        } catch (Exception e) {
            Log.d(TAG, "error onPause");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        savePreferences();
        if (billingHelper != null) {
            billingHelper.dispose();
        }
        billingHelper = null;
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        // Note onSaveInstanceState usually gets called after onPause

        /* Save application variables*/
        Log.d(TAG, "Fragment onSaveInstanceState");

        /* Save to preference manager, use that to load/save rather than Bundle */
        savePreferences();
    }

    private void savePreferences() {
        prefManager.setMajorViewType(mMajorViewType);
        prefManager.setViewIndex(mViewIndex);
        prefManager.setAltViewIndex(mAltViewIndex);
        prefManager.setSoundSwitch(mSound);
        prefManager.setPro(mPro);
        prefManager.setLicenseInfo(mLicenseInfo);
        prefManager.saveSettings();
    }

    void nextImage() {
        mViewIndex++;
        if (mViewIndex >= mViewTypes.get(mMajorViewType).size()) {
            mViewIndex = 0;
        }
        showAnim(((Anim)mViewTypes.get(mMajorViewType).get(mViewIndex)).filename, mMajorViewType == NEAR);
        reloadSound();
    }

    private void reloadSound() {
        if (soundPlayer != null) {
            try {
                if (soundPlayer.isPlaying()) {
                    soundPlayer.stop();
                }
                soundPlayer.release();
                soundPlayer = MediaPlayer.create(mainAct, ((Anim)mViewTypes.get(mMajorViewType).get(mViewIndex)).resId);
                soundPlayer.setLooping(true);
                if (mSound) {
                    soundPlayer.start();
                }
            } catch (Exception e) {
                Log.d(TAG, "error nextImage");
            }
        }
    }

    void prevImage() {
        mViewIndex--;
        if (mViewIndex < 0 ) {
            mViewIndex = mViewTypes.get(mMajorViewType).size() - 1;
        }
        showAnim(((Anim)mViewTypes.get(mMajorViewType).get(mViewIndex)).filename, mMajorViewType == NEAR);
        reloadSound();
    }

    private void showAnim(String filename, boolean near) {

        String htmlString;
        if (!near) {
            htmlString = "<!DOCTYPE html><html><body style = \"text-align:center;vertical-align:middle\"><img src=\"file:///android_res/drawable/" + filename + "\" width=\"100%\" style=\"vertical-align:middle;\"></body></html>";
        } else {
            htmlString = "<!DOCTYPE html><html><body style = \"text-align:center;vertical-align:middle\"><img src=\"file:///android_res/drawable/" + filename + "\" width=\"25%\" style=\"vertical-align:middle;\"></body></html>";
        }
        gifView.loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null);
    }
}
