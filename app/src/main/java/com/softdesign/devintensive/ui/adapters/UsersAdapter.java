package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by smalew on 14.07.16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private Context mContext;
    private List<UserListRes.UserData> mUsers;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener listener) {
        this.mUsers = users;
        this.mCustomClickListener = listener;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(view, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);

        if (!user.getPublicInfo().getPhoto().isEmpty()){
            Picasso.with(mContext).
                    load(user.getPublicInfo().getPhoto()).
                    placeholder(mContext.getResources().getDrawable(R.drawable.nav_header_bg)).
                    resize(200,0).
                    into(holder.mUserFoto);
        }

        holder.mUserFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        if (user.getPublicInfo().getBio() != null && user.getPublicInfo().getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setText(user.getPublicInfo().getBio());
            holder.mBio.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private AspectRatioImageView mUserFoto;
        private TextView mUserFullName;
        private TextView mRating;
        private TextView mCodeLines;
        private TextView mProjects;
        private TextView mBio;
        private Button mOpenInfo;

        private CustomClickListener mListener;

        public UserViewHolder(View itemView, CustomClickListener listener) {
            super(itemView);

            this.mListener = listener;

            this.mUserFoto = (AspectRatioImageView) itemView.findViewById(R.id.list_user_photo);
            this.mUserFullName = (TextView) itemView.findViewById(R.id.list_user_fullname);
            this.mRating = (TextView) itemView.findViewById(R.id.list_user_rating);
            this.mCodeLines = (TextView) itemView.findViewById(R.id.list_user_code_lines);
            this.mProjects = (TextView) itemView.findViewById(R.id.list_user_projects);
            this.mBio = (TextView) itemView.findViewById(R.id.list_user_bio);
            this.mOpenInfo = (Button) itemView.findViewById(R.id.list_open_user_info);

            this.mOpenInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (this.mListener != null){
                this.mListener.onClickOpenUserInfoListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener{

            void onClickOpenUserInfoListener(int position);
        }
    }
}
