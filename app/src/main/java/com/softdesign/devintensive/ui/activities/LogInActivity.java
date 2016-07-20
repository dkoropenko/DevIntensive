package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.LoginInMessage;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends BaseActivity implements View.OnClickListener {

    public final static String TAG = "LogInActivity";

    @BindView(R.id.login_coordination_layout)
    CoordinatorLayout coordLayout;
    @BindView(R.id.login_user)
    EditText mUserName;
    @BindView(R.id.login_password)
    EditText mUserPassword;
    @BindView(R.id.login_auth_btn)
    Button mUserAuth;
    @BindView(R.id.login_remember_paswd)
    TextView mRememberPasswd;

    private DataManager mDataManager;
    private String mLogin;
    private String mPass;

    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;
    private Handler mHandler;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginInMessage event) {
        Log.d(TAG, "onMessageEvent: message" + event.sMessage);

        switch (event.sMessage) {
            case ConstantManager.SERVER_NOT_RESPONSE:
                if (mDataManager.getUserListFromDatabase().isEmpty()) {
                    //Нет подключения к серверу и пустая база
                    hideSplashScreen();
                    showSnackBar(getResources().getString(R.string.error_can_not_enter));
                } else {
                    //Нет подключения к серверу. Есть локальная база
                    showSnackBar(getResources().getString(R.string.error_enter_with_network_dates));

                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                break;

            case ConstantManager.MAGIC_ERROR:
                hideSplashScreen();
                showSnackBar(getString(R.string.error_magic));
                break;

            case ConstantManager.WRONG_USER_OR_PASSWD:
                showSnackBar(getString(R.string.error_wrong_user_or_password));
            case ConstantManager.WRONG_TOKEN:
                hideSplashScreen();
                break;

            case ConstantManager.ERROR_WRITE_BASE:
                hideSplashScreen();
                showSnackBar(getString(R.string.error_write_to_db));
                break;

            case ConstantManager.LOAD_SUCCESS:
                //Данные удачно загружены
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mDataManager = DataManager.getInstance();
        mUserName.setText(mDataManager.getPreferencesManager().loadLoginEmail());

        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();

        mUserAuth.setOnClickListener(this);
        mRememberPasswd.setOnClickListener(this);

        mHandler = new Handler();
        Runnable checkTokenAndRun = new Runnable() {
            @Override
            public void run() {
                showSplashScreen();
                loadUsers();
            }
        };
        mHandler.post(checkTokenAndRun);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_auth_btn:
                mLogin = mUserName.getText().toString().trim();
                mPass = mUserPassword.getText().toString().trim();

                loadDatesAndStartApp();
                break;
            case R.id.login_remember_paswd:
                rememberPassword();
                break;
        }

    }

    private void rememberPassword() {
        Intent remPass = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(remPass);
    }

    private void loadDatesAndStartApp() {
        Runnable authAndRun = new Runnable() {
            @Override
            public void run() {
                showSplashScreen();
                singIn();
            }
        };
        mHandler.post(authAndRun);

    }

    private void singIn() {
        if (NetworkStatusChecker.isNetworkAvaliable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin, mPass));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, final Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        //Сохраняет токен, userId и введенные email
                        mDataManager.getPreferencesManager().saveAuthToken(response.body().getData().getToken());
                        mDataManager.getPreferencesManager().saveUserId(response.body().getData().getUser().getId());
                        mDataManager.getPreferencesManager().saveLoginEmail(mLogin);

                        saveUserValues(response.body());
                        loadUsers();

                    } else if (response.code() == 404) {
                        EventBus.getDefault().post(new LoginInMessage(ConstantManager.WRONG_USER_OR_PASSWD));
                    } else {
                        EventBus.getDefault().post(new LoginInMessage(ConstantManager.MAGIC_ERROR));
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                }
            });
        } else {
            EventBus.getDefault().post(new LoginInMessage(ConstantManager.SERVER_NOT_RESPONSE));
        }

    }

    private void loadUsers() {
        if (NetworkStatusChecker.isNetworkAvaliable(this)) {
            Call<UserListRes> call = mDataManager.getUsersListFromNetwork();
            call.enqueue(new Callback<UserListRes>() {
                @Override
                public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                    //Положительный ответ
                    if (response.code() == 200) {
                        try {
                            List<User> users = new ArrayList<>();
                            List<Repository> allRepositories = new ArrayList<Repository>();

                            int userPosition = 1;
                            for (UserListRes.UserData user : response.body().getData()) {
                                allRepositories.addAll(getUserRepositories(user));

                                User newUser = new User(user);
                                //Проверяем флаги удаления и позиции в списке.
                                List<User> userInDB = mDataManager.getUser(user.getId());
                                if (!userInDB.isEmpty()) {
                                    newUser.setDeleteFlag(userInDB.get(0).getDeleteFlag());
                                    newUser.setPositionFlag(userInDB.get(0).getPositionFlag());
                                } else {
                                    newUser.setPositionFlag(userPosition);
                                }

                                users.add(newUser);
                                userPosition++;
                            }

                            mRepositoryDao.insertOrReplaceInTx(allRepositories);
                            mUserDao.insertOrReplaceInTx(users);
                            EventBus.getDefault().post(new LoginInMessage(ConstantManager.LOAD_SUCCESS));
                        } catch (NullPointerException e) {
                            EventBus.getDefault().post(new LoginInMessage(ConstantManager.ERROR_WRITE_BASE));
                        }
                    } else
                        //Плохой токен
                        if (response.code() == 401) {
                            EventBus.getDefault().post(new LoginInMessage(ConstantManager.WRONG_TOKEN));
                        }

                }

                @Override
                public void onFailure(Call<UserListRes> call, Throwable t) {
                    // TODO: 19.07.16 Обработать ошибки
                }
            });
        } else {
            EventBus.getDefault().post(new LoginInMessage(ConstantManager.SERVER_NOT_RESPONSE));
        }

    }

    private List<Repository> getUserRepositories(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();

        for (UserModelRes.Repo repo : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repo, userId));
        }

        return repositories;
    }

    private void saveUserValues(UserModelRes modelRes) {

        //Сохраняет данные области информации
        int[] userValues = {
                modelRes.getData().getUser().getProfileValues().getRating(),
                modelRes.getData().getUser().getProfileValues().getLinesCode(),
                modelRes.getData().getUser().getProfileValues().getProjects()};
        mDataManager.getPreferencesManager().saveUserValues(userValues);

        //Сохраняем ФИО
        mDataManager.getPreferencesManager().saveFIO(modelRes.getData().getUser().getFirstName() + " " +
                modelRes.getData().getUser().getSecondName());

        //Сохраняет основную информацию
        List<String> userFields = new ArrayList<>();
        userFields.add(modelRes.getData().getUser().getContacts().getPhone());
        userFields.add(modelRes.getData().getUser().getContacts().getEmail());
        userFields.add(modelRes.getData().getUser().getContacts().getVk());
        userFields.add(modelRes.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userFields.add(modelRes.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferencesManager().saveUserProfileData(userFields);

        //Сохраняет фото и аватар
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(modelRes.getData().getUser().getPublicInfo().getPhoto()));
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(modelRes.getData().getUser().getPublicInfo().getAvatar()));
    }

    private void showSnackBar(String message) {
        Snackbar.make(coordLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
