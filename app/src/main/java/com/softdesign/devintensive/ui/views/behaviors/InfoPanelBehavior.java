package com.softdesign.devintensive.ui.views.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

/**
 * Created by smalew on 27.06.16.
 */
public class InfoPanelBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private int mMarginTop, mMarginBottom;

    public InfoPanelBehavior() {
    }

    public InfoPanelBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        child.setScaleY(dependency.getY()/50);
        child.setScaleY(dependency.getY()/50);

        return true;

    }
}
