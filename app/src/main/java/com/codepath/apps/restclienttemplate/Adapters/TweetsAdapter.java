package com.codepath.apps.restclienttemplate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.ReplyActivity;
import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TweetDetailsActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    public static final String TAG = "TweetsAdapter";
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        final ViewHolder viewHolder =   new ViewHolder(view);

        //listener for when like button clicked
        viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Tweet tweet = tweets.get(position);
                tweet.switchFavorite(context, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });
                setButton(viewHolder.ivFavorite, tweet.isLiked(), R.drawable.ic_vector_heart_stroke, R.drawable.ic_vector_heart, R.color.medium_red);
                viewHolder.tvFavoriteCount.setText(String.format("%d", tweet.getLikeCount()));
            }
        });
        return viewHolder;
    }
    // sets the color of a button, depending on whether it is active
    private void setButton(ImageView iv, boolean isActive, int strokeResId, int fillResId, int activeColor) {
        iv.setImageResource(isActive ? fillResId : strokeResId);
        iv.setColorFilter(ContextCompat.getColor(context, isActive ? activeColor : R.color.medium_gray));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data at position
        Tweet tweet = tweets.get(position);
        //bind the tweet with view holder
        holder.bind(tweet);
    }



    //clean all element of the recycler view
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    //Add a list of items -- change to type used
    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfileImage;
        private TextView tvBody;
        private TextView tvScreenName;
        private TextView tvTimeStamp;
        private ImageView ivMedia;
        private ImageView ivReply;
        private ImageView ivRetweet;
        private ImageView ivFavorite;
        private TextView tvFavoriteCount;
        private TextView tvRetweetCount;
        private TextView tvUsername;



        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            ivReply = itemView.findViewById(R.id.ivReply);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.getBody());
            tvScreenName.setText("@" +tweet.getUser().getScreenName());
            Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
            tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
            tvUsername.setText( tweet.getUser().getName());
            tvFavoriteCount.setText(String.format("%d", tweet.getLikeCount()));
            tvRetweetCount.setText(String.format("%d", tweet.getRetweetCount()));
            //set color of like button
            setButton(ivFavorite, tweet.isLiked(), R.drawable.ic_vector_heart_stroke, R.drawable.ic_vector_heart, R.color.medium_red);

            //set color of retweet
            setButton(ivRetweet, tweet.isRetweeted(), R.drawable.ic_vector_retweet_stroke, R.drawable.ic_vector_retweet, R.color.medium_green);

            // populate any embedded media with glide
            Log.i("hello",tweet.getMedia());
            if (!tweet.getMedia().equals("")) { //media exist

                Glide.with(context)
                        .load(tweet.getMedia())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                        .into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            } else {
                ivMedia.setVisibility(View.GONE);
            }
            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // compose a tweet when reply button is clicked
                    if (context instanceof TimelineActivity) {
                        String reply = tvScreenName.getText().toString();
                        ((TimelineActivity) context).composeReply(reply);
                    }
                    else {
                        Log.d("TweetAdapter", "context is not TimelineActivity");
                    }
                }
            });

        }



        @Override
        public void onClick(View v) {
            //gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), tweet);
                // show the activity
                context.startActivity(intent);
            }
        }

        //creates an intent to reply tweet
        private void replyTweet(Tweet tweet){
            Intent intent = new Intent(context, ReplyActivity.class);
            intent.putExtra("tweet", tweet);
            context.startActivity(intent);
        }
    }
}
