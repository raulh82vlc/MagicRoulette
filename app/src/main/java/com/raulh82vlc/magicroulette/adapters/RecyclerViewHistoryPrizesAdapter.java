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

package com.raulh82vlc.magicroulette.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raulh82vlc.magicroulette.R;
import com.raulh82vlc.magicroulette.models.Sign;
import com.raulh82vlc.magicroulette.util.ImageLibraryUtils;
import com.raulh82vlc.magicroulette.util.ModelUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raul Hernandez Lopez on 08/09/2015.
 * A List of Lists of Sign (status) is used to show a little summary
 * in form of history
 */
public class RecyclerViewHistoryPrizesAdapter extends RecyclerView.Adapter<RecyclerViewHistoryPrizesAdapter.PrizesHolder> {

    /**
     * Vars initialisation
     */
    private final List<List<Sign>> listOfSigns;
    private Activity activity;

    /**
     * RecyclerViewHistoryPrizesAdapter constructor
     */
    public RecyclerViewHistoryPrizesAdapter(Activity ctx, List<List<Sign>> symbols) {
        this.activity = ctx;
        this.listOfSigns = symbols;
    }

    @Override
    public PrizesHolder onCreateViewHolder(ViewGroup parent, int position) {
        final View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_prizes, parent, false);
        return new PrizesHolder(view);
    }

    @Override
    public void onBindViewHolder(PrizesHolder prizesHolder, int position) {
        List<Sign> myActualList = listOfSigns.get(position);
        buildViewHolder(position, myActualList, prizesHolder);
    }

    private void buildViewHolder(int position, List<Sign> myActualList, PrizesHolder prizesHolder) {
        int size = getItemCount();
        if (position < size && size > 0) {
            for (int i = 0; i < myActualList.size(); i++) {
                setImage(i, myActualList, prizesHolder);
            }
        }
    }

    /**
     * setImage
     *
     * @param position     int
     * @param myActualList list of actual three-status row
     * @param prizesHolder where all UI views are
     */
    private void setImage(int position, List<Sign> myActualList, PrizesHolder prizesHolder) {
        int idResource = ModelUtils.giveMeResourceId(activity, myActualList.get(position).getId());
        if (idResource != 0) {
            switch (position) {
                case 0:
                    ImageLibraryUtils.loadImageByResourceId(idResource, prizesHolder.sign1);
                    break;
                case 1:
                    ImageLibraryUtils.loadImageByResourceId(idResource, prizesHolder.sign2);
                    break;
                case 2:
                    ImageLibraryUtils.loadImageByResourceId(idResource, prizesHolder.sign3);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listOfSigns.size();
    }

    public class PrizesHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.position_1)
        ImageView sign1;
        @InjectView(R.id.position_2)
        ImageView sign2;
        @InjectView(R.id.position_3)
        ImageView sign3;

        public PrizesHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
