package com.raulh82vlc.magicroulette.callbacks;

import android.content.DialogInterface;

/**
 * Created by Raul Hernandez Lopez on 08/09/2015.
 */
public interface DialogCallback {
    void onPositive(DialogInterface dialogInterface, int pos, String data);

    void onCancel(DialogInterface dialogInterface);
}
