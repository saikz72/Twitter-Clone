package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private ImageView ivLike;
    private TextView tvLikeCount;
    private TextView tvRetweetCount;
    private TextView tvUsername;

    private Tweet tweet;
    private TwitterClient client;
    private boolean isTapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        isTapped = false;
        client = TwitterApp.getRestClient(this);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        ivMedia = findViewById(R.id.ivMedia);
        ivReply = findViewById(R.id.ivReply);
        ivLike = findViewById(R.id.ivLike);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        ivRetweet = findViewById(R.id.ivRetweet);
        tvUsername = findViewById(R.id.tvUserName);

        tweet = getIntent().getParcelableExtra(Tweet.class.getSimpleName());
        tvBody.setText(tweet.getBody());
        tvUsername.setText(tweet.getUser().getName());


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
        likeTapped();
        reTweet();

    }

    public void sendRetweet(){
        client.publishTweet(tvBody.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    Tweet tweet =  Tweet.fromJson(json.jsonObject);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", tweet);
                    setResult(RESULT_OK, intent);   //set result code and bundle data for response
                    finish();   //closes the activity
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }
    private void reTweet(){
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRetweet();
            }
        });
    }
    private void likeTapped() {
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isTapped) {
                    //ivLike.setColorFilter(getResources().getColor(R.color));
                    ivLike.setColorFilter(Color.RED);
                    isTapped = true;
                } else {
                    ivLike.setColorFilter(Color.DKGRAY);
                    isTapped = false;
                }
            }
        });
    }

}