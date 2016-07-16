package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.LoginModelRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        mUserName.setText(mDataManager.getPreferencesManager().loadLoginEmail());

        mUserAuth.setOnClickListener(this);
        mRememberPasswd.setOnClickListener(this);

        tokenLogin();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_auth_btn:
                mLogin = mUserName.getText().toString().trim();
                mPass = mUserPassword.getText().toString().trim();
                singIn();
                break;
            case R.id.login_remember_paswd:
                rememberPassword();
                break;
        }

    }

    private void showSnackBar(String message) {
        Snackbar.make(coordLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void rememberPassword() {
        Intent remPass = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(remPass);
    }

    private void loginSuccess() {
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
    }

    private void tokenLogin() {
        if (!mDataManager.getPreferencesManager().getAuthToken().isEmpty()){
            if (NetworkStatusChecker.isNetworkAvaliable(this)) {
                showProgress();
                Call<LoginModelRes> call = mDataManager.isValid(mDataManager.getPreferencesManager().getUserId());

                call.enqueue(new Callback<LoginModelRes>() {
                    @Override
                    public void onResponse(Call<LoginModelRes> call, Response<LoginModelRes> response) {
                        if (response.code() == 200) {
                            saveUserValuesWithToken(response.body());
                            hideProgress();
                            loginSuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModelRes> call, Throwable t) {
                        showSnackBar(getString(R.string.error_wrong_user_or_password));
                    }
                });
            } else {
                hideProgress();
                showSnackBar(getString(R.string.error_server_not_response));
            }
        }
    }

    private void singIn() {
        if (NetworkStatusChecker.isNetworkAvaliable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin, mPass));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        //Сохраняет токен, userId и введенные email
                        mDataManager.getPreferencesManager().saveAuthToken(response.body().getData().getToken());
                        mDataManager.getPreferencesManager().saveUserId(response.body().getData().getUser().getId());
                        mDataManager.getPreferencesManager().saveLoginEmail(mLogin);
                        saveUserValues(response.body());
                        loginSuccess();

                    } else if (response.code() == 404) {
                        showSnackBar(getString(R.string.error_wrong_user_or_password));
                    } else {
                        showSnackBar(getString(R.string.error_magic));
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                }
            });
        } else {
            showSnackBar(getString(R.string.error_server_not_response));
        }

    }

    private void saveUserValuesWithToken(LoginModelRes modelRes) {
        //Сохраняет данные области информации
        int[] userValues = {
                modelRes.getData().profileValues.getRait(),
                modelRes.getData().profileValues.getLinesCode(),
                modelRes.getData().profileValues.getProjects()};
        mDataManager.getPreferencesManager().saveUserValues(userValues);

        //Сохраняем ФИО
        mDataManager.getPreferencesManager().saveFIO(modelRes.getData().getFirstName() + " " +
                modelRes.getData().getSecondName());

        //Сохраняет основную информацию
        List<String> userFields = new ArrayList<>();
        userFields.add(modelRes.getData().getContacts().getPhone());
        userFields.add(modelRes.getData().getContacts().getEmail());
        userFields.add(modelRes.getData().getContacts().getVk());
        userFields.add(modelRes.getData().getRepositories().getRepo().get(0).getGit());
        userFields.add(modelRes.getData().getPublicInfo().getBio());
        mDataManager.getPreferencesManager().saveUserProfileData(userFields);

        //Сохраняет фото и аватар
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(modelRes.getData().getPublicInfo().getPhoto()));
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(modelRes.getData().getPublicInfo().getAvatar()));
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
}
