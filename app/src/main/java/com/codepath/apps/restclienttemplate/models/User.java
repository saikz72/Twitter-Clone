package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class User implements Parcelable {
    private String name;
    private String screenName;
    private String profileImageUrl;

    protected User(Parcel in) {
        name = in.readString();
        screenName = in.readString();
        profileImageUrl = in.readString();
    }
    public User(){
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
    }

    public static User fromJson(JSONObject jsonObject) throws JSONException{
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString(("screen_name"));
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        return user;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
