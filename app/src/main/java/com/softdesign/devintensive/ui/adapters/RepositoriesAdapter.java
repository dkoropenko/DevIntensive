package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

/**
 * Created by smalew on 14.07.16.
 */
public class RepositoriesAdapter extends BaseAdapter {

    private static final String TAG = "RepositoriesAdapter";
    private List<String> mRepoList;
    private LayoutInflater mLayoutInflater;

    public RepositoriesAdapter(Context context, List<String> repoList) {
        mRepoList = repoList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRepoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRepoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = mLayoutInflater.inflate(R.layout.item_repository_list, parent, false);
        }

        TextView repoName = (TextView) itemView.findViewById(R.id.user_profile_github_repos);
        repoName.setText(mRepoList.get(position));

        return itemView;
    }
}
