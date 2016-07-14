package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";
    @BindView(R.id.list_navigation_drawer) DrawerLayout mDrawerLayout;
    @BindView(R.id.list_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.list_toolbar) Toolbar mToolbar;
    @BindView(R.id.user_list) RecyclerView mUserList;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUserData;

    //Боковое меню
    @BindView(R.id.list_navigation_view)
    NavigationView mNavigationView;
    private View mDrawerHeader;
    ImageView mUserAvatar;
    TextView mUserFio, mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mUserList.setLayoutManager(llManager);

        setupToolbar();
        setupDrawable();
        initUsersData();
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home){
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUsersData() {
        Call<UserListRes> call = mDataManager.getUsersList();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                Log.d(TAG, "onResponse: "+ response.code());
                try{
                    mUserData = response.body().getData();
                    mUsersAdapter = new UsersAdapter(mUserData, new UsersAdapter.UserViewHolder.CustomClickListener() {
                        @Override
                        public void onClickOpenUserInfoListener(int position) {
                            UserDTO user = new UserDTO(mUserData.get(position));

                            Intent openProfile = new Intent(UserListActivity.this, UsersProfileActivity.class);
                            openProfile.putExtra(ConstantManager.PARCEBLE_INFORMATION, user);
                            startActivity(openProfile);
                        }
                    });
                    mUserList.setAdapter(mUsersAdapter);    
                }catch (NullPointerException e){
                    Log.d(TAG, "onResponse error: "+ e);
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // TODO: 14.07.16 сделать обработку ошибок. 
            }
        });
    }

    private void setupDrawable() {
        //Боковое меню
        mDrawerHeader = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        mUserAvatar = (ImageView) mDrawerHeader.findViewById(R.id.menu_header_avatar);
        //Вставляем аватар в выдвижное меню
        Picasso.with(this).
                load(mDataManager.getPreferencesManager().
                        loadUserAvatar()).
                placeholder(R.drawable.empty_avatar).
                into(mUserAvatar);

        mUserFio = (TextView) mDrawerHeader.findViewById(R.id.menu_header_user_name);
        mUserFio.setText(mDataManager.getPreferencesManager().loadFIO());

        mUserEmail = (TextView) mDrawerHeader.findViewById(R.id.menu_header_user_mail);
        mUserEmail.setText(mDataManager.getPreferencesManager().loadLoginEmail());

        final NavigationView navigationView = (NavigationView) findViewById(R.id.list_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_exit:
                        Intent intent = new Intent(getBaseContext(), LogInActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        showSnackBar(item.getTitle().toString());
                        item.setChecked(true);
                        break;
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
