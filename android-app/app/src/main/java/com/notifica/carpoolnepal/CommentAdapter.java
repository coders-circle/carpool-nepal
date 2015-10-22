package com.notifica.carpoolnepal;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Comment> mComments;

    public CommentAdapter(List<Comment> comments){
        mComments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment c = mComments.get(position);
        String username = c.poster.firstName + " " + c.poster.lastName;
        holder.vUser.setText(username);
        holder.vPostedAt.setText(CarpoolHandler.LongToDateTime(c.postedOn));
        holder.vComment.setText(c.message);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImage;
        protected TextView vUser;
        protected TextView vPostedAt;
        protected TextView vComment;

        public ViewHolder(View v) {
            super(v);
            vImage = (ImageView)v.findViewById(R.id.comment_user_image);
            vUser = (TextView)v.findViewById(R.id.comment_user_name);
            vPostedAt = (TextView)v.findViewById(R.id.comment_posted_at);
            vComment = (TextView)v.findViewById(R.id.comment_user_comment);
        }
    }
}
