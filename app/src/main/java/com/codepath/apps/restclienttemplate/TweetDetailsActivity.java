package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.Adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {
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

    private Tweet tweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        bind();

        // enable favorite button
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet.switchFavorite(TweetDetailsActivity.this, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });
                setButtonColor(ivFavorite, tweet.isLiked(),
                        R.drawable.ic_vector_heart_stroke, R.drawable.ic_vector_heart, R.color.medium_red);
                tvFavoriteCount.setText(String.format("%d Likes", tweet.getLikeCount()));
            }
        });
        // enable retweet button
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet.switchRetweet(TweetDetailsActivity.this, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });
                setButtonColor(ivRetweet, tweet.isRetweeted(),
                        R.drawable.ic_vector_retweet_stroke, R.drawable.ic_vector_retweet, R.color.medium_green);
                tvRetweetCount.setText(String.format("%d Retweets", tweet.getRetweetCount()));
            }
        });
    }


    // sets the color of a button, depending on whether it is active
    private void setButtonColor(ImageView iv, boolean isActive, int strokeResId, int fillResId, int activeColor) {
        iv.setImageResource(isActive ? fillResId : strokeResId);
        iv.setColorFilter(ContextCompat.getColor(this, isActive ? activeColor : R.color.medium_gray));
    }
    //sets the view
    public void setView(){
        ActivityTweetDetailsBinding binding = ActivityTweetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ivProfileImage = binding.ivProfileImage;
        tvBody = binding.tvBody;
        tvScreenName = binding.tvScreenName;
        tvTimeStamp =binding.tvTimeStamp;
        ivMedia = binding.ivMedia;
        ivReply = binding.ivReply;
        ivFavorite = binding.ivFavorite;
        tvFavoriteCount = binding.tvFavoriteCount;
        tvRetweetCount = binding.tvRetweetCount;
        ivRetweet = binding.ivRetweet;
        tvUsername = binding.tvUsername;
    }

    //binds view with content
    public void bind(){
        client = TwitterApp.getRestClient(this);
        tweet = getIntent().getParcelableExtra(Tweet.class.getSimpleName());
        tvBody.setText(tweet.getBody());
        tvUsername.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        setButtonColor(ivFavorite, tweet.isLiked(), R.drawable.ic_vector_heart_stroke, R.drawable.ic_vector_heart, R.color.medium_red);
        setButtonColor(ivRetweet, tweet.isRetweeted(), R.drawable.ic_vector_retweet_stroke, R.drawable.ic_vector_retweet, R.color.medium_green);
        tvFavoriteCount.setText(String.format("%d Likes", tweet.getRetweetCount()));
        tvRetweetCount.setText(String.format("%d Retweets", tweet.getLikeCount()));
        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                //.bitmapTransform(new RoundedCornersTransformation(this, 30, 0))
                .into(ivProfileImage);
        if (!tweet.getMedia().equals("")) {
            Glide.with(this)
                    .load(tweet.getMedia())
                    // .bitmapTransform(new RoundedCornersTransformation(this, 10, 0))
                    .into(ivMedia);
        }
    }
}

