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

package com.github.bkhezry.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.bkhezry.demo.adapter.RecyclerItemClickListener;
import com.github.bkhezry.demo.adapter.ThemesAdapter;
import com.github.bkhezry.demo.model.DataGenerator;
import com.github.bkhezry.demo.model.SampleData;
import com.github.bkhezry.extrawebview.ExtraWebViewCreator;
import com.github.bkhezry.extrawebview.data.DataModel;
import com.github.bkhezry.extrawebview.data.DataModelBuilder;
import com.github.bkhezry.extrawebview.data.IntentServiceResult;
import com.github.bkhezry.extrawebview.data.ThemePreference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private DataModel dataModel;
    private ArrayList<SampleData> dataGenerators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);
        RecyclerView rvThemes = (RecyclerView) findViewById(R.id.rvThemes);
        rvThemes.setHasFixedSize(true);
        DataGenerator dataGenerator = new DataGenerator(MainActivity.this);
        dataGenerators = dataGenerator.createThemeList();
        ThemesAdapter adapter = new ThemesAdapter(this, dataGenerators);
        rvThemes.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvThemes.setLayoutManager(layoutManager);
        rvThemes.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, rvThemes, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        generateWebView(ThemePreference.THEME_NAMES.get(position), position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void generateWebView(String themeName, int position) {
        SampleData sampleData = dataGenerators.get(position);
        Long tsLong = System.currentTimeMillis();
        dataModel = new DataModelBuilder()
                .withId(Long.valueOf(position))
                .withType("blog")
                .withBy(sampleData.getAuthor())
                .withTime(tsLong)
                .withUrl(sampleData.getUrl())
                .withDescription(sampleData.getDescription())
                .withBookmark(true)
                .withViewed(false)
                .withRank(0)
                .withVoted(true)
                .withPageTitle(sampleData.getTitle())
                .build();
        new ExtraWebViewCreator()
                .withContext(MainActivity.this)
                .withBookmarkIcon(true)
                .withVoteIcon(true)
                .withCustomFont("fonts/IRANSansMobile.ttf")
                .withThemeName(themeName)
                .withDataModel(dataModel)
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(IntentServiceResult intentServiceResult) {
        if (intentServiceResult.isChecked()) {
            Toast.makeText(this, "Record Id: " + intentServiceResult.getId() + " - " + intentServiceResult.getTypeEvent() + " Checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Record Id: " + intentServiceResult.getId() + " - " + intentServiceResult.getTypeEvent() + " UnChecked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
