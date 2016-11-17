/*
 * Copyright (c) 2016. Behrouz Khezry
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.bkhezry.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.bkhezry.demo.R;
import com.github.bkhezry.demo.model.SampleData;

import java.util.List;

public class ThemesAdapter extends
        RecyclerView.Adapter<ThemesAdapter.ViewHolder> {
    private List<SampleData> mDataGenerators;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public ThemesAdapter(Context context, List<SampleData> dataGenerators) {
        mDataGenerators = dataGenerators;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_theme, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        SampleData sampleData = mDataGenerators.get(position);
        Button button = holder.messageButton;
        button.setText(sampleData.getThemeName());
        button.setBackgroundColor(sampleData.getColor());
        button.setTextColor(sampleData.getTextColor());
    }

    @Override
    public int getItemCount() {
        return mDataGenerators.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }
}