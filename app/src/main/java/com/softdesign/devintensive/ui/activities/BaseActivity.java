package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Оснавная активность с базовым функционалом.
 */
public class BaseActivity extends AppCompatActivity {
    public static String TAG = ConstantManager.PREFIX_TAG + "BaseActivity";

    protected ProgressDialog mProgressDialog;
    protected ProgressDialog mSpashScreen;


    public void showProgress(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        } else{
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }

    public void hideProgress(){
        if (mProgressDialog != null)
            if (mProgressDialog.isShowing())
                mProgressDialog.hide();
    }

    public void showError (String message, Exception error){
        showToast(message);
        Log.e(TAG, String.valueOf(error));

    }

    public void showToast(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

    public void showSplashScreen(){
        if (mSpashScreen == null){
            mSpashScreen = new ProgressDialog(this, R.style.custom_dialog);
            mSpashScreen.setCancelable(false);
            mSpashScreen.getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
            mSpashScreen.show();
            mSpashScreen.setContentView(R.layout.progress_splash);
        } else{
            mSpashScreen.show();
            mSpashScreen.setContentView(R.layout.progress_splash);
        }
    }

    public void hideSplashScreen(){
        if (mSpashScreen != null){
            if (mSpashScreen.isShowing()){
                mSpashScreen.hide();
            }
        }
    }
}
