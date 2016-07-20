package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ItemTouchHelperAdapter;
import com.softdesign.devintensive.utils.LoadDatestoUi;
import com.softdesign.devintensive.utils.RetainFragment;
import com.softdesign.devintensive.utils.SimpleItemTouchHelperCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity implements SearchView.OnQueryTextListener{

    private static final String TAG = "UserListActivity";
    private static final int LOADER = 1;

    @BindView(R.id.list_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_list)
    RecyclerView mUserList;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUserData;

    //Инициализация слоев для бокового меню и user content
    @BindView(R.id.list_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.list_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.list_navigation_drawer)
    DrawerLayout mNavigationDrawer;

    private View mDrawerHeader;
    private ImageView mUserAvatar;
    private TextView mUserFio, mUserEmail;

    private Handler mSearchHandler;
    private ChronosConnector mChronosConnector = new ChronosConnector();
    private RetainFragment mRetainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mSearchHandler = new Handler();

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mUserList.setLayoutManager(llManager);

        setupToolbar();
        setupDrawable();

        FragmentManager fm = getSupportFragmentManager();
        mRetainFragment = (RetainFragment) fm.findFragmentByTag("myModel");

        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, "myModel").commit();

            mRetainFragment.setModel(mUserData);
        }
        mUserData = mRetainFragment.getModel();
        showData(mUserData);

        mChronosConnector.onCreate(this, savedInstanceState);
        if (mUserData == null){
            mChronosConnector.runOperation(new LoadDatestoUi(), false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mChronosConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChronosConnector.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChronosConnector.onResume();
    }

    public void onOperationFinished(final LoadDatestoUi.Result result) {
        if (result.isSuccessful()) {
            showData(result.getOutput());
        }
    }

    private void showData(List<User> user){
        if (user != null){
            mUserData = user;
            loadUsers();
        }
    }

    private void loadUsers() {
        if (!mUserData.isEmpty()) {
            mUsersAdapter = new UsersAdapter(mUserData, new UsersAdapter.UserViewHolder.CustomClickListener() {
                @Override
                public void onClickOpenUserInfoListener(int position) {
                    UserDTO user = new UserDTO(mUserData.get(position));

                    Intent openProfile = new Intent(UserListActivity.this, UsersProfileActivity.class);
                    openProfile.putExtra(ConstantManager.PARCEBLE_INFORMATION, user);
                    startActivity(openProfile);
                }
            });

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mUsersAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            mUserList.setAdapter(mUsersAdapter);
            touchHelper.attachToRecyclerView(mUserList);
        } else {
            showSnackBar(getString(R.string.error_load_users));
        }
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
                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.menu_user_profile:
                        intent = new Intent(getBaseContext(), MainActivity.class);
                        break;
                    case R.id.menu_contacts:
                        intent = new Intent(getBaseContext(), UserListActivity.class);
                        break;
                    case R.id.menu_exit:
                        intent = new Intent(getBaseContext(), LogInActivity.class);
                        mDataManager.getPreferencesManager().saveAuthToken("");
                        break;
                    default:
                        showSnackBar(item.getTitle().toString());
                        item.setChecked(true);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }

                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.menu_open);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView view = (SearchView) MenuItemCompat.getActionView(item);
        view.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Проверка введенного текста.
        checkInputInformation(newText);
        return false;
    }
    /**
     * Проверяет введенные данные пользователем.
     * Собирает совпадения в коллекцию mSearchUserData
     * Выводить данные на экран через адаптер
     *
     * @param text введенный текст
     */
    private void checkInputInformation(String text) {

        if (text.equals("")){
            mUserData = mDataManager.getUserListFromDatabase();
            Runnable searchingUsers = new Runnable() {
                @Override
                public void run() {
                    showUserList();
                }
            };

            mSearchHandler.removeCallbacks(searchingUsers);
            mSearchHandler.post(searchingUsers);
        }else {
            mUserData = mDataManager.getUserListByName(text);

            Runnable searchingUsers = new Runnable() {
                @Override
                public void run() {
                    showUserList();
                }
            };

            mSearchHandler.removeCallbacks(searchingUsers);
            mSearchHandler.postDelayed(searchingUsers, ConstantManager.DELAY_MILLIS);
        }
    }

    private void showUserList(){
        if (!mUserData.isEmpty()) {
            mUsersAdapter = new UsersAdapter(mUserData, new UsersAdapter.UserViewHolder.CustomClickListener() {
                @Override
                public void onClickOpenUserInfoListener(int position) {
                    UserDTO user = new UserDTO(mUserData.get(position));

                    Intent openProfile = new Intent(UserListActivity.this, UsersProfileActivity.class);
                    openProfile.putExtra(ConstantManager.PARCEBLE_INFORMATION, user);
                    startActivity(openProfile);
                }
            });

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mUsersAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            mUserList.swapAdapter(mUsersAdapter, true);
            touchHelper.attachToRecyclerView(mUserList);
        } else {
            showSnackBar(getString(R.string.error_load_users));
        }
    }

    /**
     * Закрытие выдвижного меню по нажатию кнопки "Назад"
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START))
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            mNavigationDrawer.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

}
