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

import com.github.bkhezry.extrawebview.widget.PopupMenu;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(
        injects = {
                ItemActivity.class,
                WebFragment.class
        },
        library = true,
        complete = false
)
class UiModule {
    @Provides
    PopupMenu providePopupMenu() {
        return new PopupMenu.Impl();
    }

    @Provides
    @Singleton
    CustomTabsDelegate provideCustomTabsDelegate() {
        return new CustomTabsDelegate();
    }

    @Provides
    @Singleton
    KeyDelegate provideKeyDelegate() {
        return new KeyDelegate();
    }

}
