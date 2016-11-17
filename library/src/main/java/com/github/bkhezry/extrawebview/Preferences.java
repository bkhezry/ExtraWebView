/*
 * Copyright (c)
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
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.preference.PreferenceManager;

import com.github.bkhezry.extrawebview.data.ThemePreference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;




public class Preferences {
    static boolean shouldLazyLoad(Context context) {
        return get(context, R.string.pref_lazy_load, true);
    }

    private static boolean get(Context context, @StringRes int key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(key), defaultValue);
    }

    static class Observable {
        private static Set<String> CONTEXT_KEYS;
        private final Map<String, Integer> mSubscribedKeys = new HashMap<>();
        private final SharedPreferences.OnSharedPreferenceChangeListener mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (mSubscribedKeys.containsKey(key)) {
                    notifyChanged(mSubscribedKeys.get(key), CONTEXT_KEYS.contains(key));
                }
            }
        };


        void unsubscribe(Context context) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .unregisterOnSharedPreferenceChangeListener(mListener);
        }


        private void notifyChanged(int key, boolean contextChanged) {

        }
    }

    public static class Theme {

        public static void apply(Context context, String themeName) {
            @StyleRes int themeSpec = getTheme(themeName);
            context.setTheme(themeSpec);
        }


        private static @StyleRes int getTheme(String themeName) {
            return ThemePreference.getTheme(themeName);
        }
    }

}
