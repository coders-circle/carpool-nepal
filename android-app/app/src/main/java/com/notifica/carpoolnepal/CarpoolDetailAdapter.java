package com.notifica.carpoolnepal;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class CarpoolDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Carpool mCarpool;
    List<Comment> mComments;
    Context mContext;
    CarpoolDetailFragment mFragment;

    public CarpoolDetailAdapter(CarpoolDetailFragment fragment, List<Comment> comments){
        mComments = comments;
        mFragment = fragment;
        mContext = fragment.getActivity();
    }

    public void setCarpool(Carpool carpool) {
        mCarpool = carpool;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View detailView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carpool_detail_layout, parent, false);
            return new DetailsViewHolder(detailView);
        }
        else {
            View commentsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
            return new CommentsViewHolder(commentsView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            if (mCarpool != null) {
                final DetailsViewHolder holder = (DetailsViewHolder)viewHolder;
                String location = mCarpool.source + " to " + mCarpool.destination;
                User poster = mCarpool.poster;
                String username = "", contact = "";

                if (poster != null) {
                    username = poster.firstName + " " + poster.lastName;
                    contact = poster.contactNumber + "\n" + poster.contactAddress + "\n" + poster.email;
                }

                holder.vTime.setText(CarpoolHandler.LongToTime12(mCarpool.time));
                holder.vDate.setText(CarpoolHandler.LongToDayMonthDate(mCarpool.date));
                holder.vLocation.setText(location);
                holder.vDescription.setText(mCarpool.description);
                holder.vUsername.setText(username);
                holder.vContact.setText(contact);

                holder.vPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                        String username = preferences.getString("username", "");
                        String password = preferences.getString("password", "");
                        CarpoolHandler handler = new CarpoolHandler(username, password);

                        Comment comment = new Comment();
                        comment.carpool = mCarpool;
                        comment.message = holder.vComment.getText().toString();
                        handler.postComment(comment, new Callback() {
                            @Override
                            public void onComplete(String response) {
                                if (response.equals("Success")) {
                                    mFragment.refreshData();    // To refresh with new comment in case server connection is slow
                                    mFragment.getData();        // To get new comments from server and refresh again when done

                                    holder.vComment.setText("");
                                    Toast.makeText(mContext, "Posted", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (response.equals(""))
                                        response = "Error while trying to post comment";
                                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        }
        else {
            position = position - 1;
            CommentsViewHolder holder = (CommentsViewHolder)viewHolder;
            Comment c = mComments.get(position);
            String username = c.poster.firstName + " " + c.poster.lastName;
            holder.vUser.setText(username);
            holder.vPostedAt.setText(CarpoolHandler.LongToDateTime(c.postedOn));
            holder.vComment.setText(c.message);
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size() + 1;
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImage;
        protected TextView vUser;
        protected TextView vPostedAt;
        protected TextView vComment;

        public CommentsViewHolder(View commentsView) {
            super(commentsView);
            vImage = (ImageView)commentsView.findViewById(R.id.comment_user_image);
            vUser = (TextView)commentsView.findViewById(R.id.comment_user_name);
            vPostedAt = (TextView)commentsView.findViewById(R.id.comment_posted_at);
            vComment = (TextView)commentsView.findViewById(R.id.comment_user_comment);
        }
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTime;
        protected TextView vDate;
        protected TextView vLocation;
        protected TextView vDescription;
        protected ImageView vUserImgae;
        protected TextView vUsername;
        protected TextView vContact;

        protected EditText vComment;
        protected Button vPost;

        public DetailsViewHolder(View detailsView) {
            super(detailsView);

            vTime = (TextView) detailsView.findViewById(R.id.cd_time);
            vDate = (TextView) detailsView.findViewById(R.id.cd_date);
            vLocation = (TextView) detailsView.findViewById(R.id.cd_source_destination);
            vDescription = (TextView) detailsView.findViewById(R.id.cd_description);
            vUserImgae = (ImageView) detailsView.findViewById(R.id.cd_user_image);
            vUsername = (TextView) detailsView.findViewById(R.id.cd_user_name);
            vContact = (TextView) detailsView.findViewById(R.id.cd_contact_info);

            vComment = (EditText) detailsView.findViewById(R.id.edit_add_comment);
            vPost = (Button) detailsView.findViewById(R.id.button_post_comment);
        }
    }


}
