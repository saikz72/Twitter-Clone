package com.codepath.apps.restclienttemplate.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Tweet implements Parcelable {
    private String body;
    private String createdAt;
    private long id;
    private User user;
    private String media;
    private long likeCount;
    private boolean liked;
    private long retweetCount;
    private boolean retweeted;
    private long uid;




    public Tweet() {
    }

    protected Tweet(Parcel in) {
        body = in.readString();
        createdAt = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        media = in.readString();
        likeCount = in.readLong();
        retweetCount = in.readLong();
    }
    public String getBody() {
        return body;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public User getUser() {
        return user;
    }
    public long getId() {
        return id;
    }
    public String getMedia(){
        return media;
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getRetweetCount() {
        return retweetCount;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(createdAt);
        dest.writeParcelable(user, flags);
        dest.writeString(media);
        dest.writeLong(retweetCount);
        dest.writeLong(likeCount);
    }


    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");
        tweet.likeCount = jsonObject.getLong("favorite_count");
        tweet.liked = jsonObject.getBoolean("favorited");
        tweet.retweetCount = jsonObject.getLong("retweet_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");

        JSONObject entities = jsonObject.getJSONObject("entities");
        if(entities.has("media")){
            JSONArray medias =  entities.getJSONArray("media");
            JSONObject object2 = (JSONObject) medias.get(0);
            tweet.media = object2.getString("media_url_https");
        } else {
            tweet.media = "";
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public void switchFavorite(Context context, JsonHttpResponseHandler handler) {
        TwitterApp.getRestClient(context).favoriteTweet(liked = !liked, uid, handler);
        likeCount += (liked ? 1 : -1);
    }

    public void switchRetweet(Context context, JsonHttpResponseHandler handler) {
        TwitterApp.getRestClient(context).retweetTweet(retweeted = !retweeted, uid, handler);
        retweetCount += (retweeted ? 1 : -1);
    }

}