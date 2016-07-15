package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersProfileActivity extends AppCompatActivity {

    private static final String TAG = "UsersProfileActivity";

    @BindView(R.id.user_profile_user_photo_img)
    ImageView mUserPhoto;
    @BindView(R.id.user_profile_user_rating)
    TextView mRating;
    @BindView(R.id.user_profile_user_code_lines)
    TextView mLinesCode;
    @BindView(R.id.user_profile_user_projects)
    TextView mProjects;
    @BindView(R.id.user_profile_user_bio)
    TextView mBio;
    @BindView(R.id.user_profile_repo_list)
    ListView mRepoList;


    @BindView(R.id.user_profile_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_profile_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile_info);
        ButterKnife.bind(this);

        initFields();
        setupToolbar();
    }

    //Инициализация полей профиля.
    private void initFields() {
        final UserDTO user = getIntent().getParcelableExtra(ConstantManager.PARCEBLE_INFORMATION);

        List<String> reposinories = user.getGit();
        RepositoriesAdapter adapter = new RepositoriesAdapter(this, reposinories);
        mRepoList.setAdapter(adapter);

        Picasso.with(this).
                load(user.getPhoto()).
                placeholder(this.getResources().getDrawable(R.drawable.nav_header_bg)).
                error(this.getResources().getDrawable(R.drawable.nav_header_bg)).
                fit().
                centerCrop().
                into(mUserPhoto);


        mRating.setText(user.getRating());
        mLinesCode.setText(user.getCodeLine());
        mProjects.setText(user.getProjects());
        mBio.setText(user.getSelf());
        mCollapsingToolbarLayout.setTitle(user.getFullName());
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
