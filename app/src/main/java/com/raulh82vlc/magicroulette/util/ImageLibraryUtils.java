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

package com.raulh82vlc.magicroulette.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * ImageLibraryUtils class helper
 * Created by Raul Hernandez Lopez on 07/9/15.
 *
 * @author Raul Hernandez Lopez
 */
public class ImageLibraryUtils {

    /**
     * Singleton instance
     */
    private static RequestManager mGlideInstance;

    /**
     * init
     *
     * @param context Application context
     */
    public static void init(Context context) {
        mGlideInstance = Glide.with(context);
    }

    /**
     * getInstance
     *
     * @return mGlideInstance Singleton instance
     */
    public static RequestManager getInstance() throws NullPointerException {
        if (mGlideInstance == null) {
            throw new NullPointerException("Glide instance failed");
        }
        return mGlideInstance;
    }

    /**
     * loadImageByResourceId
     *
     * @param iconResId id from resources
     * @param img       ImageView where to render
     */
    public static void loadImageByResourceId(int iconResId, ImageView img) {
        mGlideInstance
                .load(iconResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }
}