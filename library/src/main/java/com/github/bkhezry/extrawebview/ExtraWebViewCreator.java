/*
 * Copyright (c) 2016 Behrouz Kherzy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.bkhezry.extrawebview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;

import com.github.bkhezry.extrawebview.data.DataModel;
import com.github.bkhezry.extrawebview.data.ViewOption;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ExtraWebViewCreator {
    private Context context;
    private DataModel dataModel;
    private ViewOption viewOption;
    private boolean _bookMarkIcon = false;
    private boolean _voteIcon = false;
    private int _color;
    private String _defaultFontPath;
    private String _themeName;

    public ExtraWebViewCreator() {
        this.viewOption = new ViewOption();
    }

    public ExtraWebViewCreator withContext(Context context) {
        this.context = context;
        return this;
    }

    public ExtraWebViewCreator withDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
        return this;
    }

    public ExtraWebViewCreator withBookmarkIcon(boolean hasBookMarkIcon) {
        this._bookMarkIcon = hasBookMarkIcon;
        return this;
    }

    public ExtraWebViewCreator withVoteIcon(boolean hasVoteIcon) {
        this._voteIcon = hasVoteIcon;
        return this;
    }

    public ExtraWebViewCreator withTextColor(@ColorInt int color) {
        this._color = color;
        return this;
    }

    public ExtraWebViewCreator withCustomFont(String defaultFontPath) {
        this._defaultFontPath = defaultFontPath;
        return this;
    }

    public ExtraWebViewCreator withThemeName(String themeName) {
        this._themeName = themeName;
        return this;
    }

    public ExtraWebViewCreator show() {
        viewOption.setBookMarkIcon(_bookMarkIcon);
        viewOption.setVoteIcon(_voteIcon);
        viewOption.setColor(_color);
        viewOption.setDefaultFontPath(_defaultFontPath);
        viewOption.setThemeName(_themeName);
        if (_defaultFontPath != null) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(viewOption.getDefaultFontPath())
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra(ItemActivity.EXTRA_ITEM, dataModel);
        intent.putExtra(ItemActivity.EXTRA_VIEW_OPTION, viewOption);
        context.startActivity(intent);
        return this;
    }
}
