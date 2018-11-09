package com.github.bkhezry.extrawebview.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.MenuRes;
import androidx.annotation.StringRes;

public interface PopupMenu {
    /**
     * Create a new popup menu with an anchor view and alignment
     * gravity. Must be called right after construction.
     *
     * @param context Context the popup menu is running in, through which it
     *                can access the current theme, resources, etc.
     * @param anchor  Anchor view for this popup. The popup will appear below
     *                the anchor if there is room, or above it if there is not.
     * @param gravity The {@link Gravity} value for aligning the popup with its
     *                anchor.
     * @return this (fluent API)
     */
    PopupMenu create(Context context, View anchor, int gravity);

    /**
     * Inflate a menu resource into this PopupMenu. This is equivalent to calling
     * popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu()).
     *
     * @param menuRes Menu resource to inflate
     * @return this (fluent API)
     */
    PopupMenu inflate(@MenuRes int menuRes);

    /**
     * Set menu item visibility
     *
     * @param itemResId item ID
     * @param visible   true to be visible, false otherwise
     * @return this (fluent API)
     */
    PopupMenu setMenuItemVisible(@IdRes int itemResId, boolean visible);

    /**
     * Set menu item title
     *
     * @param itemResId item ID
     * @param title     item title
     * @return this (fluent API)
     */
    PopupMenu setMenuItemTitle(@IdRes int itemResId, @StringRes int title);

    /**
     * Set a listener that will be notified when the user selects an item from the menu.
     *
     * @param listener Listener to notify
     * @return this (fluent API)
     */
    PopupMenu setOnMenuItemClickListener(OnMenuItemClickListener listener);

    /**
     * Show the menu popup anchored to the view specified during construction.
     */
    void show();

    /**
     * Interface responsible for receiving menu item click events if the items themselves
     * do not have individual item click listeners.
     */
    interface OnMenuItemClickListener {
        /**
         * This method will be invoked when a menu item is clicked if the item itself did
         * not already handle the event.
         *
         * @param item {@link MenuItem} that was clicked
         * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
         */
        boolean onMenuItemClick(MenuItem item);
    }

    class Impl implements PopupMenu {
      private androidx.appcompat.widget.PopupMenu mSupportPopupMenu;

        @Override
        public PopupMenu create(Context context, View anchor, int gravity) {
          mSupportPopupMenu = new androidx.appcompat.widget.PopupMenu(context, anchor, gravity);
            return this;
        }

        @Override
        public PopupMenu inflate(@MenuRes int menuRes) {
            mSupportPopupMenu.inflate(menuRes);
            return this;
        }

        @Override
        public PopupMenu setMenuItemVisible(@IdRes int itemResId, boolean visible) {
            mSupportPopupMenu.getMenu().findItem(itemResId).setVisible(visible);
            return this;
        }

        @Override
        public PopupMenu setMenuItemTitle(@IdRes int itemResId, @StringRes int title) {
            mSupportPopupMenu.getMenu().findItem(itemResId).setTitle(title);
            return this;
        }

        @Override
        public PopupMenu setOnMenuItemClickListener(final OnMenuItemClickListener listener) {
          mSupportPopupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            return this;
        }

        @Override
        public void show() {
            mSupportPopupMenu.show();
        }
    }
}
