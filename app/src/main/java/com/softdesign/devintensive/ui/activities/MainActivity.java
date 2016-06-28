package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static String TAG = ConstantManager.PREFIX_TAG + " MainActivity: ";

    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText userPhone, userMail, userVK, userRepo, userSelf;
    private View mDrawerHeader;
    private NavigationView mNavigationView;
    private ImageView mAvatar;
    private LinearLayout mInfoPanel;

    private List<EditText> mUserInfo;
    private boolean mCurrentEditMode;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициализация ярлыков для взаимодействия с user information
        mCallImg = (ImageView) findViewById(R.id.call_img);
        mCallImg.setOnClickListener(this);

        //Инициализация слоев для бокового меню и user content
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);

        //User container
        mInfoPanel = (LinearLayout) findViewById(R.id.info_container);
        userPhone = (EditText) findViewById(R.id.user_phone);
        userMail = (EditText) findViewById(R.id.user_mail);
        userVK = (EditText) findViewById(R.id.user_vk);
        userRepo = (EditText) findViewById(R.id.user_repo);
        userSelf = (EditText) findViewById(R.id.user_self);

        mUserInfo = new ArrayList();
        mUserInfo.add(userPhone);
        mUserInfo.add(userMail);
        mUserInfo.add(userVK);
        mUserInfo.add(userRepo);
        mUserInfo.add(userSelf);
        mDataManager = DataManager.getInstance();

        //Боковое меню
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mDrawerHeader = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        mAvatar = (ImageView)mDrawerHeader.findViewById(R.id.avatar_img);
        mAvatar.setImageResource(R.drawable.avatar);

        //ToolBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
        setupNavigation();
        loadUserInfoValue();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        if (savedInstanceState != null) {
            //Проверяем режим редактирования данных
            fabChangeMode(savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_img:

                break;
            case R.id.fab:
                if (!mCurrentEditMode) {
                    fabChangeMode(true);
                } else {
                    fabChangeMode(false);
                    saveUserInfoValue();
                }
                break;
        }
    }

    /**
     * Проверяет режим редактирования по нажатию кнопки fab
     *
     * @param mode - true - режим включен, false выключен
     */
    private void fabChangeMode(boolean mode) {
        for (EditText userValue : mUserInfo) {
            mCurrentEditMode = mode;
            userValue.setEnabled(mode);
            userValue.setFocusable(mode);
            userValue.setFocusableInTouchMode(mode);
        }
        if (mode)
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
        else
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
    }

    /**
     * Загружает переменные бользователя
     */
    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();

        for (int i = 0; i < mUserInfo.size() ; i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }

    }

    /**
     * Сохраняет переменные пользователя
     */
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();

        for (EditText userInfo: mUserInfo) {
            userData.add(userInfo.getText().toString());
        }

        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }


    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            mNavigationDrawer.openDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigation() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START))
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveUserInfoValue();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }


}
