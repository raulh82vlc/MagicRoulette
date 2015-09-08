package com.raulh82vlc.magicroulette.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raulh82vlc.magicroulette.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul Hernandez Lopez on 07/09/2015.
 *
 * adaptation of the WheelAdapter from google,
 * totally customised also in subclasses used,
 * as the aim of this assignment "is not reinventing the wheel"
 * but using appropiated libraries and customised in a concrete amount
 * of time, all this has been tested and modelled properly to achieve a
 * fine final result
 *
 * @author Raul Hernandez Lopez
 */
public class RouletteAdapter extends AbstractWheelAdapter {
    // Image size
    final int IMAGE_WIDTH = 200;
    final int IMAGE_HEIGHT = 140;

    // Slot machine symbols
    private final int items[] = new int[] {
            R.drawable.fruittype_avocado,
            R.drawable.fruittype_burrito,
            R.drawable.fruittype_skeleton
    };

    private Context context;

    // Cached images
    private List<SoftReference<Bitmap>> images;

    public RouletteAdapter(Context context) {
        this.context = context;
        images = new ArrayList<SoftReference<Bitmap>>(items.length);
        for (int id : items) {
            images.add(new SoftReference<Bitmap>(loadImage(id)));
        }
    }

    /**
     * Loads image from resources
     */
    private Bitmap loadImage(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
        bitmap.recycle();
        return scaled;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }

    // Layout params for image view
    final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        ImageView img;
        if (cachedView != null) {
            img = (ImageView) cachedView;
        } else {
            img = new ImageView(context);
        }
        img.setLayoutParams(params);
        SoftReference<Bitmap> bitmapRef = images.get(index);
        Bitmap bitmap = bitmapRef.get();
        if (bitmap == null) {
            bitmap = loadImage(items[index]);
            images.set(index, new SoftReference<Bitmap>(bitmap));
        }
        img.setImageBitmap(bitmap);

        return img;
    }
}
