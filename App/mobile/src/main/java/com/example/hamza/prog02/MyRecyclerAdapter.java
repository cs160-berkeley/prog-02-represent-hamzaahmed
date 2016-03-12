package com.example.hamza.prog02;

/**
 * Created by Hamza on 3/6/16.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
    private List<CongressItem> congressItemList;
    private Context mContext;
    private View view;

    public MyRecyclerAdapter(Context context, List<CongressItem> congressItemList) {
        this.congressItemList = congressItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final CongressItem congressItem = congressItemList.get(i);

        final CustomViewHolder viewHolder = customViewHolder;

        //Setting text view title
        viewHolder.name.setText(congressItem.getName());
        viewHolder.title.setText(congressItem.getTitle());

        LinearLayout partyBox = (LinearLayout) view.findViewById(R.id.partyBox);
        ImageView partyIcon = (ImageView) view.findViewById(R.id.partyIcon);
        viewHolder.thumbnail.setImageBitmap(congressItem.getThumbnail());

        if (congressItem.getParty().equalsIgnoreCase("D")) {
            partyBox.setBackgroundColor(ContextCompat.getColor(mContext, R.color.democrat));
            partyIcon.setBackgroundResource(R.drawable.democrat);
        } else if (congressItem.getParty().equalsIgnoreCase("R")){
            partyBox.setBackgroundColor(ContextCompat.getColor(mContext, R.color.republican));
            partyIcon.setBackgroundResource(R.drawable.rebulican);
        } else if (congressItem.getParty().equalsIgnoreCase("I")) {
            partyBox.setBackgroundColor(ContextCompat.getColor(mContext, R.color.independent));
            partyIcon.setBackgroundResource(R.drawable.twitter);
        }

        viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailedViewActivity.class);
                intent.putExtra("firstName", congressItem.getFirstName());
                intent.putExtra("lastName", congressItem.getLastName());
                intent.putExtra("thumbnail", congressItem.getThumbnail());
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                CongressItem congressItem = congressItemList.get(position);

                Uri uriUrl = null;
                String party = congressItem.getParty();
                if (party.equalsIgnoreCase("D")) {
                    Log.d("T", "WTFFFF");
                    uriUrl = Uri.parse("https://www.democrats.org/");
                } else if (party.equalsIgnoreCase("R")) {
                    uriUrl = Uri.parse("https://www.gop.com/");
                } else if (party.equalsIgnoreCase("I")) {
                    uriUrl = Uri.parse("http://www.independentamericanparty.org/");
                }

                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                v.getContext().startActivity(launchBrowser);
            }
        });

        viewHolder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                String[] TO = {congressItem.getEmail()};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                v.getContext().startActivity(Intent.createChooser(emailIntent, "Send your email using:"));
            }
        });

        viewHolder.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("T", "DETECTING CLICK");
                int visibility = viewHolder.twitterBox.getVisibility();
                if (visibility == 0) {
                    viewHolder.twitterBox.setVisibility(View.GONE);
                } else {
                    viewHolder.twitterBox.setVisibility(View.VISIBLE);
                }
            }
        });

        viewHolder.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(congressItem.getWebsite());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

                v.getContext().startActivity(launchBrowser);
            }
        });

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        String twitterId = congressItem.getTwitter();
        statusesService.userTimeline(null, twitterId, 1, null, null, false, true, false, false, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                TweetView tweetView = new TweetView(mContext, result.data.get(0));
                viewHolder.twitterBox.addView(tweetView);
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != congressItemList ? congressItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView name;
        protected TextView title;
        protected ImageView thumbnail;
        protected ImageView party;
        protected ImageView email;
        protected ImageView website;
        protected ImageView twitter;
        protected LinearLayout twitterBox;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.name = (TextView) view.findViewById(R.id.name);
            this.title = (TextView) view.findViewById(R.id.title);
            this.twitterBox = (LinearLayout) view.findViewById(R.id.twitterBox);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.party = (ImageView) view.findViewById(R.id.partyIcon);
            this.email = (ImageView) view.findViewById(R.id.emailIcon);
            this.website = (ImageView) view.findViewById(R.id.websiteIcon);
            this.twitter = (ImageView) view.findViewById(R.id.twitterIcon);
        }
    }
}