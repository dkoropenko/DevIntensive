package com.softdesign.devintensive.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by koropenkods on 27.06.16.
 */
public class CircleImageView extends ImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float radWidth = canvas.getHeight()/2;
        final float radHeight = canvas.getWidth()/2;

        final float radius = Math.max(radWidth, radHeight);
        final Path path = new Path();
        path.addCircle(radWidth,radHeight,radius, Path.Direction.CCW);

        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
