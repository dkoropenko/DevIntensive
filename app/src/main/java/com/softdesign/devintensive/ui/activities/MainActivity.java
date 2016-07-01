package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
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

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static String TAG = ConstantManager.PREFIX_TAG + " MainActivity: ";

    //Инициализация слоев для бокового меню и user content
    @BindView( R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView( R.id.navigation_drawer) DrawerLayout mNavigationDrawer;

    //User container
    private List<EditText> mUserInfo;
    @BindView(R.id.user_phone) EditText userPhone;
    @BindView(R.id.user_mail) EditText userMail;
    @BindView(R.id.user_vk) EditText userVK;
    @BindView(R.id.user_repo) EditText userRepo;
    @BindView(R.id.user_self) EditText userSelf;

    //Инициализация ярлыков для взаимодействия с user information
    private List<ImageView> mUserAction;
    @BindView(R.id.to_call_btn) ImageView mToCall;
    @BindView(R.id.to_mail_btn) ImageView mToMail;
    @BindView(R.id.to_vk_btn) ImageView mToVk;
    @BindView(R.id.to_repo_btn) ImageView mToRepo;

    //Боковое меню
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    private View mDrawerHeader;
    ImageView mAvatar;

    //ToolBar
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;


    private boolean mCurrentEditMode;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        //Боковое меню
        mDrawerHeader = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        mAvatar = (ImageView)mDrawerHeader.findViewById(R.id.avatar_img);
        mAvatar.setImageResource(R.drawable.empty_avatar);


        setupUserInfo();
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
        Intent intent = null;
        Uri data = null;
        String url = null;

        switch (view.getId()) {
            case R.id.to_call_btn:
                intent = new Intent(Intent.ACTION_DIAL);
                data = Uri.parse("tel:"+ userPhone.getText().toString());
                intent.setData(data);
                break;
            case R.id.to_mail_btn:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String[] adress = {userMail.getText().toString()};
                intent.putExtra(Intent.EXTRA_EMAIL, adress);
                break;
            case R.id.to_vk_btn:
                url = userVK.getText().toString();

                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://"+ url;

                data = Uri.parse(url);

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(data);
                break;
            case R.id.to_repo_btn:
                url = userRepo.getText().toString();

                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://"+ url;

                data = Uri.parse(url);

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(data);
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

        if (intent != null && intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
            // TODO: 01.07.16 Добавить зависимости в manifest.
        }else{
            showSnackBar("Невозможно открыть!");
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

    private void setupUserInfo(){
        mUserInfo = new ArrayList<>();
        mUserInfo.add(userPhone);
        mUserInfo.add(userMail);
        mUserInfo.add(userVK);
        mUserInfo.add(userRepo);
        mUserInfo.add(userSelf);

        mUserAction = new ArrayList<>();
        mUserAction.add(mToCall);
        mUserAction.add(mToMail);
        mUserAction.add(mToVk);
        mUserAction.add(mToRepo);

        for (int i = 0; i < mUserAction.size(); i++) {
            mUserAction.get(i).setOnClickListener(this);
        }
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

                switch (item.getItemId()){
                    case R.id.relogin:
                        Intent intent = new Intent(getBaseContext(), LogInActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        showSnackBar(item.getTitle().toString());
                        item.setChecked(true);
                        break;
                }

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
