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

import android.net.Uri;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * DataModel object use for declare post attribute
 */
public class DataModel implements Serializable {
    private long id;
    private String type;
    private String by;
    private long time;
    private String url;
    private String description;
    private CharSequence pageTitle;
    private boolean favorite;
    private boolean viewed;
    private int rank;
    private boolean voted;

    public CharSequence getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(CharSequence pageTitle) {
        this.pageTitle = pageTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public String getSource() {
        return TextUtils.isEmpty(getUrl()) ? null : Uri.parse(getUrl()).getHost();
    }

    public String getDisplayedTitle() {
        return description;
    }
}
