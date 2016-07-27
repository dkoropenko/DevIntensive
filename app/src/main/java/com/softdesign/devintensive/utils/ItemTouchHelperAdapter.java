package com.softdesign.devintensive.utils;

/**
 * Created by smalew on 20.07.16.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
