package com.softdesign.devintensive.ui.views.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by smalew on 27.06.16.
 */
public class InfoPanelBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private static final String TAG = "Smalew";
    private final int mMaxInfoPanelSize = 56;

    private float mMinSize, mMinScroll;
    private float mMaxSize, mMaxScroll;
    private float mCurrentSize;

    private float mProcScroll;
    private float mScrollSize;

    public InfoPanelBehavior() {
        mMinScroll = 0;
    }

    public InfoPanelBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        if (mMinSize == 0){
            mMaxScroll = parent.getHeight() - dependency.getHeight();
            mMinScroll = child.getY();
            mScrollSize = mMaxScroll - mMinScroll;

            mMinSize = child.getHeight();
            mMaxSize = mMinSize * 2;
        }

        if (child.getHeight() <= mMaxSize){

            Log.d(TAG, "onDependentViewChanged: minScroll "+ mMinScroll);
            Log.d(TAG, "onDependentViewChanged: maxScroll "+ mMaxScroll);
            Log.d(TAG, "onDependentViewChanged: depY "+ dependency.getY());

            if (dependency.getY() <= mMaxScroll){
                mProcScroll = 100 *  ((mScrollSize) - (mMaxScroll - child.getY()));
                mProcScroll /= mScrollSize;
                mProcScroll = 100 - mProcScroll;

                mCurrentSize = mMinSize + (int)((mMaxSize * mProcScroll)/100);
                child.setScaleY(mCurrentSize/10);
            }
        }

        return true;
    }
}
