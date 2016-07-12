package com.softdesign.devintensive.utils;

import android.content.Context;
import android.icu.text.DisplayContext;
import android.util.TypedValue;

/**
 * Created by smalew on 11.07.16.
 */
public class UiHelper {
    private static Context sContext = DevIntensiveApp.getContext();

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = sContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = sContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (sContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, sContext.getResources().getDisplayMetrics());
        }
        return result;
    }

    public static int lerp(int start, int end, float friction) {
        return (int) (start + (end - start) * friction);
    }

    public static float currentFriction(int start, int end, int currentValue) {
        return (float) (currentValue - start) / (end - start);
    }
}
