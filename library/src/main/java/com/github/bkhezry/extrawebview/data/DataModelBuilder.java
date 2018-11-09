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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * data model Builder. use to generate DataModel class as fluent interface.
 */
public class DataModelBuilder {
    private Long _id;
    private String _type = "blog";
    private String _by;
    private Long _time;
    private String _url;
    private String _description;
    private CharSequence _pageTitle;
    private boolean _bookmark = false;
    private boolean _viewed = false;
    private int _rank = 0;
    private boolean _voted = false;
    private DataModel dataModel;

    public DataModelBuilder() {
        dataModel = new DataModel();
    }

    /**
     * set id of blog post
     *
     * @param id as Long
     * @return DataModelBuilder object
     */
    public DataModelBuilder withId(@NonNull Long id) {
        this._id = id;
        return this;
    }

    /**
     * set type of blog post
     *
     * @param type as String. default value is "blog"
     * @return DataModelBuilder object
     */
    public DataModelBuilder withType(@Nullable String type) {
        this._type = type;
        return this;
    }

    /**
     * set Author name
     *
     * @param by as String. author of blog post name
     * @return DataModelBuilder object
     */
    public DataModelBuilder withBy(@NonNull String by) {
        this._by = by;
        return this;
    }

    /**
     * set time of creating post
     *
     * @param time as Long, timestamp format.
     * @return DataModelBuilder object
     */
    public DataModelBuilder withTime(@NonNull Long time) {
        this._time = time;
        return this;
    }

    /**
     * set url of post
     *
     * @param url as String, url format
     * @return DataModelBuilder object
     */
    public DataModelBuilder withUrl(@NonNull String url) {
        this._url = url;
        return this;
    }

    /**
     * set description tag of post
     *
     * @param description as String
     * @return DataModelBuilder object
     */
    public DataModelBuilder withDescription(@NonNull String description) {
        this._description = description;
        return this;
    }

    /**
     * set favorite status of post
     *
     * @param isBookmark as boolean, default value is false
     * @return DataModelBuilder object
     */
    public DataModelBuilder withBookmark(@NonNull Boolean isBookmark) {
        this._bookmark = isBookmark;
        return this;
    }

    /**
     * set post viewed status
     *
     * @param isViewed is true post viewed or false post not viewed, default value is false
     * @return DataModelBuilder object
     */

    public DataModelBuilder withViewed(@NonNull Boolean isViewed) {
        this._viewed = isViewed;
        return this;
    }

    /**
     * set rank of post
     *
     * @param rank as Integer, default value is 0
     * @return DataModelBuilder object
     */
    public DataModelBuilder withRank(@NonNull Integer rank) {
        this._rank = rank;
        return this;
    }

    /**
     * set status of post vote
     *
     * @param isVoted as boolean, if true post voted or false post not voted, default value is false
     * @return DataModelBuilder object
     */
    public DataModelBuilder withVoted(@NonNull Boolean isVoted) {
        this._voted = isVoted;
        return this;
    }

    /**
     * set title of post tag
     *
     * @param pageTitle as String.
     * @return DataModelBuilder object
     */
    public DataModelBuilder withPageTitle(@NonNull String pageTitle) {
        this._pageTitle = pageTitle;
        return this;
    }

    /**
     * return DataModel object or return IllegalStateException in value validation
     *
     * @return DataModel object
     */
    public DataModel build() {
        List<String> messageList = new ArrayList<>();
        if (_id == null) {
            messageList.add("should be set Id with Long type.\n");
        } else {
            dataModel.setId(_id);
        }
        dataModel.setType(_type);

        if (_by == null) {
            messageList.add("should be set By with Author name.\n");
        } else {
            dataModel.setBy(_by);
        }
        if (_time == null) {
            messageList.add("should be set time with post create time.\n");
        } else {
            dataModel.setTime(_time);
        }
        if (_url == null) {
            messageList.add("should be set url with url of website.\n");
        } else {
            dataModel.setUrl(_url);
        }
        if (_pageTitle == null) {
            messageList.add("should be set page title with title of page.\n");
        } else {
            dataModel.setPageTitle(_pageTitle);
        }
        if (_description == null) {
            messageList.add("should be set title with description of website.\n");
        } else {
            dataModel.setDescription(_description);
        }
        if (messageList.size() > 0) {
            throw new IllegalStateException(getMessage(messageList));
        }
        dataModel.setFavorite(_bookmark);
        dataModel.setViewed(_viewed);
        dataModel.setRank(_rank);
        dataModel.setVoted(_voted);
        return dataModel;
    }

    /**
     * @param messageList list of message
     * @return joined of messages as one message
     */
    private String getMessage(List<String> messageList) {
        String messageString = "";
        for (int i = 0; i < messageList.size(); i++) {
            if (messageString.equals("")) {
                messageString = messageList.get(i);
            } else {
                messageString += "\n" + messageList.get(i);
            }
        }
        return messageString;
    }
}
