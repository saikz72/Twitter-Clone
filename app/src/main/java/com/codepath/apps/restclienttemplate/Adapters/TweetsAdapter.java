package com.codepath.apps.restclienttemplate.Adapters;

import android.content.Context;
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
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivProfileImage;
        private TextView tvBody;
        private TextView tvScreenName;
        private TextView tvTimeStamp;
        private ImageView ivMedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.getBody());
            tvScreenName.setText(tweet.getUser().getScreenName());
            Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
            tvTimeStamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
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
    }
}
