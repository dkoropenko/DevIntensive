package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
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
    private Context mContext;
    private List<String> mRepoList;

    private LayoutInflater mInflater;

    public RepositoriesAdapter(Context context, List<String> repoList) {
        mContext = context;
        mRepoList = repoList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRepoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mRepoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemView = view;

        if (itemView == null) {
            itemView = mInflater.inflate(R.layout.item_repository_list, viewGroup, false);
        }

        TextView repoName = (TextView)itemView.findViewById(R.id.user_profile_user_github);
        repoName.setText(mRepoList.get(i).toString());

        return itemView;
    }
}
