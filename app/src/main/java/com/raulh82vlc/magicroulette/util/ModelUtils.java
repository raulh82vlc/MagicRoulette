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

import android.app.Activity;
import android.support.annotation.NonNull;

import com.raulh82vlc.magicroulette.common.Constants;

/**
 * Created by Raul Hernandez Lopez on 08/09/2015.
 */
public class ModelUtils {

    /**
     * giveMeResourceId
     * id from the resource of drawable folder
     *
     * @param activity
     * @param type
     * @return id integer
     **/
    public static int giveMeResourceId(Activity activity, int type) {
        String resName = extractNameFromResource(type);
        String packageName = activity.getPackageName();
        return activity.getResources().getIdentifier(resName, "drawable", packageName);
    }

    /**
     * extractNameFromResource
     *
     * @param type
     * @return resName name of the file
     **/
    @NonNull
    private static String extractNameFromResource(int type) {
        String resName;
        switch (type) {
            case Constants.AVOCADO:
                resName = "fruittype_avocado";
                break;
            case Constants.BURRITO:
                resName = "fruittype_burrito";
                break;
            case Constants.SKELETON:
            default:
                resName = "fruittype_skeleton";
                break;
        }
        return resName;
    }
}
