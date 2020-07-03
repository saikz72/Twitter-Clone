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
    private boolean isTapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        client = TwitterApp.getRestClient(this);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        ivMedia = findViewById(R.id.ivMedia);
        ivReply = findViewById(R.id.ivReply);
        ivFavorite = findViewById(R.id.ivFavorite);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        ivRetweet = findViewById(R.id.ivRetweet);
        tvUsername = findViewById(R.id.tvUsername);

        tweet = getIntent().getParcelableExtra(Tweet.class.getSimpleName());
        tvBody.setText(tweet.getBody());
        tvUsername.setText(tweet.getUser().getName());

        tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        setButton(ivFavorite, tweet.isLiked(), R.drawable.ic_vector_heart_stroke, R.drawable.ic_vector_heart, R.color.medium_red);
        setButton(ivRetweet, tweet.isRetweeted(), R.drawable.ic_vector_retweet_stroke, R.drawable.ic_vector_retweet, R.color.medium_green);
        tvFavoriteCount.setText(String.format("%d Likes", tweet.getLikeCount()));
        tvRetweetCount.setText(String.format("%d Retweets", tweet.getRetweetCount()));
        Glide.with (this)
                .load(tweet.getUser().getProfileImageUrl())
                //.bitmapTransform(new RoundedCornersTransformation(this, 30, 0))
                .into(ivProfileImage);
        if(!tweet.getMedia().equals("")) {
            Glide.with(this)
                    .load(tweet.getMedia())
                   // .bitmapTransform(new RoundedCornersTransformation(this, 10, 0))
                    .into(ivMedia);
        }

    }

    // sets the color of a button, depending on whether it is active
    private void setButton(ImageView iv, boolean isActive, int strokeResId, int fillResId, int activeColor) {
        iv.setImageResource(isActive ? fillResId : strokeResId);
        iv.setColorFilter(ContextCompat.getColor(this, isActive ? activeColor : R.color.medium_gray));
    }
}

