package com.softdesign.devintensive.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersProfileActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "UsersProfileActivity";

    private UserDTO mUser;

    //Данные пользователя.
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


    //Toolbar
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
        mUser = getIntent().getParcelableExtra(ConstantManager.PARCEBLE_INFORMATION);

        List<String> repositories = mUser.getGit();
        RepositoriesAdapter adapter = new RepositoriesAdapter(this, repositories);
        mRepoList.setAdapter(adapter);
        mRepoList.setOnItemClickListener(this);
        setListViewHeightBasedOnChildren(mRepoList);

        if (!mUser.getPhoto().isEmpty()) {

            final Context context = getBaseContext();

            Picasso.with(this).
                    load(mUser.getPhoto()).
                    placeholder(this.getResources().getDrawable(R.drawable.nav_header_bg)).
                    error(this.getResources().getDrawable(R.drawable.nav_header_bg)).
                    networkPolicy(NetworkPolicy.OFFLINE).
                    resize(200,0).
                    into(mUserPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Load photo from cache");
                        }

                        @Override
                        public void onError() {
                            DataManager.getInstance().getPicassoCache().getPicassoInstance().with(context).
                                    load(mUser.getPhoto()).
                                    placeholder(context.getResources().getDrawable(R.drawable.nav_header_bg)).
                                    resize(200,0).
                                    into(mUserPhoto);
                        }
                    });
        }

        mRating.setText(mUser.getRating());
        mLinesCode.setText(mUser.getCodeLine());
        mProjects.setText(mUser.getProjects());
        mBio.setText(mUser.getSelf());
        mCollapsingToolbarLayout.setTitle(mUser.getFullName());
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Uri data = Uri.parse("http://" + mUser.getGit().get(i));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(data);

        if (intent != null) {
            startActivity(intent);
        }
    }

    //Позволяет засунуть полноразмерный ListView в RecycleView
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
