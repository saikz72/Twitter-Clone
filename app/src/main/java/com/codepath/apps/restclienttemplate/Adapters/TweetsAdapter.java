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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.ReplyActivity;
import com.codepath.apps.restclienttemplate.TweetDetailsActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return  new ViewHolder(view);
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
        private ImageView ivLike;
        private TextView tvLikeCount;
        private TextView tvRetweetCount;
        private TextView tvUserName;

        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivReply = itemView.findViewById(R.id.ivReply);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.getBody());
            tvScreenName.setText("@" +tweet.getUser().getScreenName());
            Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
            tvTimeStamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
            tvUserName.setText( tweet.getUser().getName());
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

        }

        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return relativeDate;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: djd");
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
