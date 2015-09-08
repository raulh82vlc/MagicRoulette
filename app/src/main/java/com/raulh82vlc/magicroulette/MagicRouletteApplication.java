package com.raulh82vlc.magicroulette;

import android.app.Application;

import com.raulh82vlc.magicroulette.util.ImageLibraryUtils;

/**
 * Created by Raul Hernandez Lopez on 07/09/2015.
 */
public class MagicRouletteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLibraryUtils.init(this);
    }
}
