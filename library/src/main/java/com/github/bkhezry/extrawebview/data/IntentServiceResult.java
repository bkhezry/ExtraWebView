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

/**
 * use in EventBus for send data between activities
 */
public class IntentServiceResult {
    private Long id;
    private String typeEvent;
    private boolean isChecked;

    /**
     * get Id of blog's post
     *
     * @return Long value as post Id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set Id of blog's post
     *
     * @param id as Long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get type of event
     *
     * @return String value as event that occurred
     */
    public String getTypeEvent() {
        return typeEvent;
    }

    /**
     * Set type of event occurred when click icon in WebView ui
     *
     * @param mTypeAction as String
     */
    public void setTypeEvent(String mTypeAction) {
        this.typeEvent = mTypeAction;
    }

    /**
     * return status of icon as boolean
     *
     * @return true if checked and return false if unchecked
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * set status of icon in WebView ui
     *
     * @param checked true when icon is checked, false when icon is unchecked.
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
