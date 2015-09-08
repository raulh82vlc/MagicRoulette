/*
 * Copyright (C) 2015 Raul Hernandez Lopez
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.raulh82vlc.magicroulette.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raulh82vlc.magicroulette.R;
import com.raulh82vlc.magicroulette.adapters.RecyclerViewHistoryPrizesAdapter;
import com.raulh82vlc.magicroulette.adapters.RouletteAdapter;
import com.raulh82vlc.magicroulette.callbacks.DialogCallback;
import com.raulh82vlc.magicroulette.common.Constants;
import com.raulh82vlc.magicroulette.listeners.OnWheelChangedListener;
import com.raulh82vlc.magicroulette.listeners.OnWheelScrollListener;
import com.raulh82vlc.magicroulette.models.Sign;
import com.raulh82vlc.magicroulette.util.DialogUtils;
import com.raulh82vlc.magicroulette.util.SystemUiHider;
import com.raulh82vlc.magicroulette.widgets.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MachineRouletteMainActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * How long this spins
     **/
    private static final int TIME = 2000;
    /**
     * Views injection
     **/
    @InjectView(R.id.start_game_button)
    Button startGameButton;
    @InjectView(R.id.roulette_1)
    WheelView roulette1;
    @InjectView(R.id.roulette_2)
    WheelView roulette2;
    @InjectView(R.id.roulette_3)
    WheelView roulette3;
    @InjectView(R.id.status_game)
    TextView statusGame;
    @InjectView(R.id.history_rv)
    RecyclerView recyclerView;
    Handler mHideHandler = new Handler();
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
    // Wheel scrolled flag
    private boolean rouletteScrolled = false;
    /**
     * Variables
     **/
    private RecyclerViewHistoryPrizesAdapter recyclerHistoryPrizesAdapter;
    private AlertDialog materialDialog;
    private List<List<Sign>> listOfStatusSigns = new ArrayList<>();
    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            rouletteScrolled = true;
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            rouletteScrolled = false;
            updateResultFromRoulette();
        }
    };
    /**
     * Previous status flags
     */
    private int prevStatus1 = -1;
    private int prevStatus2 = -1;
    private int prevStatus3 = -1;
    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!rouletteScrolled) {
                updateResultFromRoulette();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_machine_roulette_main);

        ButterKnife.inject(this);

        hideShowControlsInFullScreenMode();

        initializeRoulette(roulette1);
        initializeRoulette(roulette2);
        initializeRoulette(roulette3);

        setupRecyclerView();

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActualResult(roulette1);
                changeActualResult(roulette2);
                changeActualResult(roulette3);
            }
        });
    }

    /**
     * setupRecyclerView
     * sets the initial configuration of the recycler view for the history
     **/
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerHistoryPrizesAdapter == null) {
            recyclerHistoryPrizesAdapter = new RecyclerViewHistoryPrizesAdapter(this, listOfStatusSigns);
            recyclerView.setAdapter(recyclerHistoryPrizesAdapter);
        }
    }

    /**
     * updateStatusCurrentDataStructures
     * sets each iteration of the 3 status, to change the history
     **/
    private void updateStatusCurrentDataStructures() {
        if (avoidRepetitions()) {
            setPrevStatus();
            List<Sign> myStatusList = new ArrayList<>();
            myStatusList.add(new Sign(roulette1.getCurrentItem()));
            myStatusList.add(new Sign(roulette2.getCurrentItem()));
            myStatusList.add(new Sign(roulette3.getCurrentItem()));
            listOfStatusSigns.add(myStatusList);
        }
    }

    /**
     * setPrevStatus
     * sets previous status to the right current one
     **/
    private void setPrevStatus() {
        prevStatus1 = roulette1.getCurrentItem();
        prevStatus2 = roulette2.getCurrentItem();
        prevStatus3 = roulette3.getCurrentItem();
    }

    /**
     * avoidRepetitions
     * checks if the previuous and the current are different
     * in one of the 3 status, then we add this to the history
     *
     * @return boolean
     **/
    private boolean avoidRepetitions() {
        return prevStatus1 != roulette1.getCurrentItem()
                || prevStatus2 != roulette2.getCurrentItem()
                || prevStatus3 != roulette3.getCurrentItem();
    }

    /**
     * changeActualResult
     * does the scroll of the component
     **/
    private void changeActualResult(WheelView roulette) {
        roulette.scroll(getItemsToScroll(), TIME);
    }

    /**
     * Initializes roulette
     *
     * @param roulette Wheel View widget
     */
    private void initializeRoulette(WheelView roulette) {
        roulette.setViewAdapter(new RouletteAdapter(this));
        int randomNumberToAssign = getRandomNumber();
        roulette.setCurrentItem(randomNumberToAssign);
        roulette.addChangingListener(changedListener);
        roulette.addScrollingListener(scrolledListener);
        roulette.setCyclic(true);
        roulette.setEnabled(false);
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 10);
    }

    private void hideShowControlsInFullScreenMode() {
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View frameView = findViewById(R.id.press);
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, frameView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        frameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        startGameButton.setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * Updates win status
     */
    public void updateResultFromRoulette() {
        if (checkIfWinWinWinCombination()) {
            String winMsg = "";
            winMsg = whatType();
            statusGame.setText(winMsg);
            dismissDialog();
            createAlertDialog(winMsg, roulette1.getCurrentItem());
        } else {
            statusGame.setText("");
            dismissDialog();
        }
        /**
         * History settings
         **/
        setRecyclerViewHistory();
    }

    /**
     * setRecyclerViewHistory
     * moves values and updates adapter
     */
    private void setRecyclerViewHistory() {
        int positionToRefresh = listOfStatusSigns.size();
        updateStatusCurrentDataStructures();
        recyclerHistoryPrizesAdapter.notifyItemChanged(positionToRefresh);
    }

    private void dismissDialog() {
        if (materialDialog != null && materialDialog.isShowing()) {
            materialDialog.dismiss();
        }
    }

    /**
     * whatType
     * creates and shows the dialog of the prize
     *
     * @return String of type, whatever it is
     **/
    private String whatType() {
        switch (roulette1.getCurrentItem()) {
            case Constants.AVOCADO:
                return "GUACAMOLE";
            case Constants.BURRITO:
                return "BURRITO";
            case Constants.SKELETON:
                return "SKELETON";
        }
        return "";
    }

    /**
     * createAlertDialog
     * creates and shows the dialog of the prize
     *
     * @param winMsg winning message to show to the user
     * @param type   type of prize (integer)
     **/
    private void createAlertDialog(String winMsg, int type) {
        materialDialog = DialogUtils.getDialogWithImage(
                this,
                winMsg,
                type,
                new DialogCallback() {
                    @Override
                    public void onPositive(DialogInterface dialogInterface, int pos, String data) {
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        materialDialog.dismiss();
                    }
                }).create();
        materialDialog.show();
    }

    /**
     * checkIfWinWinWinCombination
     * if the three combinations does the winning combination, then true
     *
     * @return boolean
     */
    private boolean checkIfWinWinWinCombination() {
        int actualValue = roulette1.getCurrentItem();
        return checkTwoValues(roulette2.getCurrentItem(), actualValue)
                && checkTwoValues(roulette3.getCurrentItem(), actualValue);
    }

    /**
     * checkTwoValues
     *
     * @param currentItem item to check
     * @param actualValue with the actual value
     * @return boolean
     */
    private boolean checkTwoValues(int currentItem, int actualValue) {
        return currentItem == actualValue;
    }

    /**
     * getItemsToScroll
     * Random function that scrolls in an angle randomly
     *
     * @return int of this measure
     */
    public int getItemsToScroll() {
        return -350 + (int) (Math.random() * 50);
    }
}
