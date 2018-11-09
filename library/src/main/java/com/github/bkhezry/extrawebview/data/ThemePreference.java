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
package com.github.bkhezry.extrawebview.data;

import com.github.bkhezry.extrawebview.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.StyleRes;


/**
 * use for Theme ui of app
 */
public class ThemePreference {
    public static final String LIGHT = "light";
    public static final String DARK = "dark";
    public static final String SEPIA = "sepia";
    public static final String GREEN = "green";
    public static final String SOLARIZED = "solarized";
    public static final String SOLARIZED_DARK = "solarized_dark";
    private static final HashMap<String, Integer> VALUES = new HashMap<>();
    public static final ArrayList<String> THEME_NAMES = new ArrayList<>();

    /**
     * generate List of theme name.
     */
    static {
        VALUES.put(LIGHT, R.style.AppTheme);
        THEME_NAMES.add(LIGHT);
        VALUES.put(DARK, R.style.AppTheme_Dark);
        THEME_NAMES.add(DARK);
        VALUES.put(SEPIA, R.style.Sepia);
        THEME_NAMES.add(SEPIA);
        VALUES.put(GREEN, R.style.Green);
        THEME_NAMES.add(GREEN);
        VALUES.put(SOLARIZED, R.style.Solarized);
        THEME_NAMES.add(SOLARIZED);
        VALUES.put(SOLARIZED_DARK, R.style.Solarized_Dark);
        THEME_NAMES.add(SOLARIZED_DARK);
    }

    /**
     * @param value name of theme.
     * @return StyleRes of theme name.
     */
    public static
    @StyleRes
    int getTheme(String value) {
        return VALUES.get(value);
    }
}
