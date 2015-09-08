package com.raulh82vlc.magicroulette.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raulh82vlc.magicroulette.R;
import com.raulh82vlc.magicroulette.callbacks.DialogCallback;

/**
 * Created by Raul Hernandez Lopez on 08/09/2015.
 */
public class DialogUtils {

    /**
     * getDialogWithImage
     *
     * @param activity,
     * @param title
     * @param type
     * @param callBack DialogCallback
     * @return AlertDialog.Builder
     **/
    public static AlertDialog.Builder getDialogWithImage(
            Activity activity,
            String title,
            int type,
            final DialogCallback callBack) {

        /** Applying material style */
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

        /** Inflating custom view for our alert dialog */
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_image_layout, null);
        builder.setView(dialogView);

        /** Getting views */
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.image_claim);
        TextView titleClaim = (TextView) dialogView.findViewById(R.id.tittle_type);

        /** image setting */
        int idResource = ModelUtils.giveMeResourceId(activity, type);
        if (idResource != 0) {
            ImageLibraryUtils.loadImageByResourceId(idResource, imageView);
        }
        /** title setting */
        titleClaim.setText(title);

        /** builder settings */
        builder.setNeutralButton(activity.getResources().getText(R.string.general_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // callBack.onPositive(dialog, -1, null);
                callBack.onCancel(dialog);
            }
        });

        builder.setCancelable(false);

        buildContent(builder, null, null);

        return builder;
    }

    private static void buildContent(AlertDialog.Builder materialDialog, String title, String content) {
        if (title != null) {
            materialDialog.setTitle(title);
        }

        if (content != null) {
            materialDialog.setMessage(content);
        }
    }
}
