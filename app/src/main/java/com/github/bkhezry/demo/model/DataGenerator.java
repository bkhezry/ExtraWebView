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

package com.github.bkhezry.demo.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.bkhezry.demo.R;

import java.util.ArrayList;

public class DataGenerator {
    private Context mContext;

    public DataGenerator(Context context) {
        mContext = context;
    }

    public ArrayList<SampleData> createThemeList() {
        String[] themeNames = {
                "Light",
                "Dark",
                "Sepia",
                "Green",
                "Solarized",
                "Solarized Dark"
        };
        String[] urls = {
                "https://github.com/mikepenz/MaterialDrawer",
                "https://hive.ir/",
                "https://material.uplabs.com/",
                "https://curiosity.com/",
                "http://www.flowasia.cn/",
                "https://jitpack.io/"
        };
        String[] titles = {
                "mikepenz/MaterialDrawer",
                "مجله طراحی و برنامه نویسی",
                "Material Design, Daily – MaterialUp",
                "Curiosity Makes You Smarter",
                "北京网站建设|网页设计|SEO及logo/VI设计公司 | Flow Asia",
                "JitPack | Publish JVM and Android libraries"
        };
        String[] descriptions = {
                "MaterialDrawer - The flexible, easy to use, all in one drawer library for your Android project.",
                "انتشار آن\u200Cچه می\u200Cخوانیم و آن\u200Cچه می\u200Cدانیم",
                "MaterialUp curates the best of Material Design inspiration, tools and freebies. Get your daily dose of Material design inspiration!",
                "Get informed with 5 new amazing topics, delivered daily.",
                "Flow是位于北京的网络科技公司，主营包括网站建设与网页设计，Logo设计，VI设计及平面设计，SEO，SEM等在内的IT外包服务。",
                "JitPack makes it easy to release your Java or Android library. Publish straight from GitHub or BitBucket."
        };
        String[] authors = {
                "@mike_penz",
                "@hive_ir",
                "@MaterialUp",
                "@curiositydotcom",
                "@flowasia",
                "@jitpack"
        };
        int[] colors = {
                ContextCompat.getColor(mContext, R.color.grey100),
                ContextCompat.getColor(mContext, R.color.grey900),
                ContextCompat.getColor(mContext, R.color.orange400),
                ContextCompat.getColor(mContext, R.color.teal300),
                ContextCompat.getColor(mContext, R.color.orange300),
                ContextCompat.getColor(mContext, R.color.solarizedBase03)
        };
        int[] textColors = {
                ContextCompat.getColor(mContext, R.color.blackT87),
                ContextCompat.getColor(mContext, R.color.white),
                ContextCompat.getColor(mContext, R.color.brown800),
                ContextCompat.getColor(mContext, R.color.teal900),
                ContextCompat.getColor(mContext, R.color.solarizedBase01),
                ContextCompat.getColor(mContext, R.color.solarizedBase2)
        };
        ArrayList<SampleData> sampleDataArrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SampleData sampleData = new SampleData();
            sampleData.setColor(colors[i]);
            sampleData.setTextColor(textColors[i]);
            sampleData.setThemeName(themeNames[i] + "\n\n" + authors[i]);
            sampleData.setAuthor(authors[i]);
            sampleData.setTitle(titles[i]);
            sampleData.setDescription(descriptions[i]);
            sampleData.setUrl(urls[i]);
            sampleDataArrayList.add(sampleData);
        }
        return sampleDataArrayList;
    }
}