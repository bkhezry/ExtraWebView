/*
 * Copyright (c) 2016 Behrouz Khezry
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
package com.github.bkhezry.extrawebview.data;

import android.support.annotation.ColorInt;

import java.io.Serializable;

/**
 * some parameter need for WebView ui.
 */
public class ViewOption implements Serializable {
    private boolean bookMarkIcon;
    private boolean voteIcon;
    private
    @ColorInt
    int color;
    private String defaultFontPath;
    private String themeName;

    /**
     * @return custom font path in assets
     */
    public String getDefaultFontPath() {
        return defaultFontPath;
    }

    /**
     * set custom font path
     *
     * @param defaultFontPath path of font in assets as String
     */
    public void setDefaultFontPath(String defaultFontPath) {
        this.defaultFontPath = defaultFontPath;
    }

    /**
     * @return bookmarkIcon status as boolean
     */
    public boolean isBookMarkIcon() {
        return bookMarkIcon;
    }

    /**
     * set status of bookmark icon
     *
     * @param bookMarkIcon if set true, bookmark icon show in ui. default value is false
     */
    public void setBookMarkIcon(boolean bookMarkIcon) {
        this.bookMarkIcon = bookMarkIcon;
    }

    /**
     * @return vote icon status as boolean
     */
    public boolean isVoteIcon() {
        return voteIcon;
    }

    /**
     * set status of vote icon
     *
     * @param voteIcon if set true, vote icon show in ui. default value is false
     */
    public void setVoteIcon(boolean voteIcon) {
        this.voteIcon = voteIcon;
    }


    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    /**
     * @return theme name of WebView Ui.
     */
    public String getThemeName() {
        return themeName;
    }

    /**
     * set theme name of WebView Ui.
     *
     * @param themeName list of theme with name declare in ThemePreference class.
     */
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
