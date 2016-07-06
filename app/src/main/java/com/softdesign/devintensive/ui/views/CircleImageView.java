package com.softdesign.devintensive.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;

/**
 * Кастомная ImageView, которая позволяет сделать круглое фото для аватара
 * Вкладываем друг в друга setters для получение единого результата при
 * загрузке разных данных.
 */
public class CircleImageView extends ImageView {

    private static final String TAG = ConstantManager.PREFIX_TAG + "CircleImageView";

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        Bitmap avatar = BitmapFactory.decodeResource(getResources(), resId);
        this.setImageBitmap(avatar);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        RoundedAvatarDrawable drawableAvatar = new RoundedAvatarDrawable(bm);
        this.setImageDrawable(drawableAvatar);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        RoundedAvatarDrawable roundedAvatarDrawable;

        if (drawable instanceof RoundedAvatarDrawable){
            super.setImageDrawable(drawable);
        }
        else{
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            roundedAvatarDrawable = new RoundedAvatarDrawable(bitmap);
            super.setImageDrawable(roundedAvatarDrawable);
        }
    }
}
