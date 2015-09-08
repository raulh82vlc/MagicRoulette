/*
 * Critizen Android Client (https://critizen.com)
 * Copyright (c) 2015 NextChance Stocks S.L. All rights reserved.
 *
 * @copyright Copyright (c) 2015 NextChance Stocks S.L. All rights reserved.
 * @link https://play.google.com/store/apps/details?id=com.critizen
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