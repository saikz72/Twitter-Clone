package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

import static com.codepath.apps.restclienttemplate.TimelineActivity.RESULT_KEY;

public class ReplyActivity extends AppCompatActivity {
    TextView tvBodyReply;
    EditText etReply;
    ImageView ivProfileReply;
    TextView tvCharactersLeft;
    TwitterClient client;
    Button btnSendReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        setContentView(R.layout.activity_reply);
        tvBodyReply = findViewById(R.id.tvBodyReply);
        etReply = findViewById(R.id.etReply);
        ivProfileReply = findViewById(R.id.ivProfileReply);
        tvCharactersLeft = findViewById(R.id.tvCharactersLeft);
        btnSendReply = findViewById(R.id.btnSendReply);
        client = TwitterApp.getRestClient(this);

        Tweet tweet = getIntent().getParcelableExtra("tweet");
        tvBodyReply.setText(tweet.getBody());
        etReply.setText("@" + tweet.getUser().getScreenName() + " ");
        tvCharactersLeft.setText(280 - etReply.getText().length() + " characters left");
        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileReply);
        replyListener();

    }
    public void sendReply() {
        client.publishTweet(etReply.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    Tweet response = Tweet.fromJson(jsonObject);
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY,  response);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

    }});}

    private void replyListener() {
        btnSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReply();
                etReply.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int characters = etReply.getText().length();
                        tvCharactersLeft.setText((280 - s.length()) + " characters left");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        });
    }
        }