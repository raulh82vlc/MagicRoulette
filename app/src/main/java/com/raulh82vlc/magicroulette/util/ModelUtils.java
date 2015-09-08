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
