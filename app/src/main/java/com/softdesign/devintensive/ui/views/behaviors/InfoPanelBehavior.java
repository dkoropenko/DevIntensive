package com.softdesign.devintensive.ui.views.behaviors;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.utils.ConstantManager;


/**
 * Класс для изменения положения панели информации относительно другого элемента.
 */
public class InfoPanelBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private static final String TAG = ConstantManager.PREFIX_TAG + "InfoPanelBehavior";

    //Переменные для управления отступами LineatLayout
    private float mMinScroll, mMaxScroll;
    private float mMaxPaddingSize;
    private float mCurrentPaddingSize;

    //Переменные для вычисления % передвижения ScrollView
    private float mProcScroll;
    private float mScrollSize;

    //Переменные для управления отступом NestedScrollView
    CoordinatorLayout.LayoutParams mNestedScrollParam;
    private int mTopMarginMax;

    public InfoPanelBehavior(Context context, AttributeSet attrs) {
        mMinScroll = 0; //Начальный минимальный уровень.
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
        // TODO: 06.07.16 Переделать на appbar. Говорят так интереснее.
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        //Начальная инициализация переменных
        if (mMinScroll == 0) {
            mMinScroll = parent.getHeight() - dependency.getHeight();
            mMaxScroll = child.getY();
            mScrollSize = mMaxScroll - mMinScroll;

            mMaxPaddingSize = child.getPaddingTop();
            mNestedScrollParam = (CoordinatorLayout.LayoutParams)dependency.getLayoutParams();
            mTopMarginMax = mNestedScrollParam.topMargin;
        }

        //Вычисление в % перелистывание ScrollView
        // и пропорциональное изменение отступов.
        float check = mMaxScroll - child.getY();
        if (check < mScrollSize && check > 0) {
            mProcScroll = (100 * (mMaxScroll - child.getY())) / mScrollSize;

            mCurrentPaddingSize = mMaxPaddingSize - (int)((mMaxPaddingSize * mProcScroll) / 100);
            mNestedScrollParam.topMargin = mTopMarginMax - (int)((mTopMarginMax * mProcScroll) / 140);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                child.setPaddingRelative(0, (int)mCurrentPaddingSize, 0, (int)mCurrentPaddingSize);
            }
            else{
                child.setPadding(0, (int)mCurrentPaddingSize, 0, (int)mCurrentPaddingSize);
            }
        }
        return true;
    }
}
