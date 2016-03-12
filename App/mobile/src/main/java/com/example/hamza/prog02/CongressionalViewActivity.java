package com.example.hamza.prog02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CongressionalViewActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";
    private List<CongressItem> feedsList;
    private RecyclerView mRecyclerView;

    private String party;
    String latitude = null;
    String longitude = null;
    String zipCode = null;
    String randomLatLong = null;
    String county = null;
    String state = null;
    JSONObject legislatorDetails = null;
    public Location LastLocation;
    String[] randomLatLongPair = null;
    String apiKey = "b77ebc6b16a64f1ab2a2a1c8d0271963";
    ArrayList<JSONObject> results = new ArrayList<>();


    private MyRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private TextView congressText;
    private TextView countyText;
    //private TweetViewFetchAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);

        countyText = (TextView) findViewById(R.id.county);
        congressText = (TextView) findViewById(R.id.congress);
        countyText.setVisibility(View.GONE);
        congressText.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
            zipCode = extras.getString("zipCode");
            randomLatLong = extras.getString("randomLatLong");
        }

        results = new ArrayList<>();

        // Downloading data from below url
        String url = null;
        String url2 = null;
        if (randomLatLong != null) {
            randomLatLongPair = randomLatLong();
            //String[] temp = randomLatLongPair.split(",");
            latitude = randomLatLongPair[0];
            longitude = randomLatLongPair[1];
            url2 = "https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude=" + longitude  + "&apikey=" + apiKey;
            url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude +  "&region=us";
        } else if (zipCode != null) {
            url2 = "https://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zipCode + "&apikey=" + apiKey;
            url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + zipCode + "&region=us";
        } else if (latitude != null && longitude != null){
            url2 = "https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude=" + longitude + "&apikey=" + apiKey;
            url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude +  "&region=us";
        }

        new AsyncHttpTask().execute(url, url2);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                for (int i = 0; i < 2; i++) {
                    URL url = new URL(params[i]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    int statusCode = urlConnection.getResponseCode();

                    if (statusCode == 200) {
                        BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }
                        if (i == 1) {
                            parseResult(response.toString());
                        } else {
                            parseResult2(response.toString());
                        }
                        result = 1;
                    } else {
                        result = 0;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            countyText.setVisibility(View.VISIBLE);
            congressText.setVisibility(View.VISIBLE);

            if (result == 1 && results.size() == 2) {
                countyText.setText(county + ", " + state);
                Intent sendIntent = new Intent(CongressionalViewActivity.this, PhoneToWatchService.class);
                sendIntent.putExtra("CongressInfo", results.get(1).toString() + "split" + results.get(0).toString());
                startService(sendIntent);
                adapter = new MyRecyclerAdapter(CongressionalViewActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                if (randomLatLong != null) {
                    results = new ArrayList<>();
                    randomLatLongPair = randomLatLong();
                    latitude = randomLatLongPair[0];
                    longitude = randomLatLongPair[1];
                    String url2 = "https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude=" + longitude  + "&apikey=" + apiKey;
                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude +  "&region=us";
                    new AsyncHttpTask().execute(url, url2);
                }
                Toast.makeText(CongressionalViewActivity.this, "Sorry, the APIs do not work on your location. " + results.size(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CongressionalViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void parseResult(String jsonData) throws JSONException {
        try {
            legislatorDetails = new JSONObject(jsonData);
            JSONArray representatives = legislatorDetails.getJSONArray("results");
            feedsList = new ArrayList<>();

            if (representatives.length() == 0) {
                return;
            }
            for (int i = 0; i < representatives.length(); i++) {
                JSONObject nextLegislator = representatives.getJSONObject(i);

                String firstName = nextLegislator.getString("first_name");
                String lastName = nextLegislator.getString("last_name");
                String title = nextLegislator.getString("title");
                party = nextLegislator.getString("party");
                String email = nextLegislator.getString("oc_email");
                String website = nextLegislator.getString("website");
                String twitter = nextLegislator.getString("twitter_id");
                String bioGuideId = nextLegislator.getString("bioguide_id");
                URL newurl = new URL("https://theunitedstates.io/images/congress/225x275/" + bioGuideId + ".jpg");
                Bitmap mIcon = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                CongressItem legislator = new CongressItem(firstName, lastName, title, mIcon, party, email, website, twitter);
                feedsList.add(legislator);
            }
            results.add(legislatorDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseResult2(String jsonData) throws JSONException {
        try {
            legislatorDetails = new JSONObject(jsonData);
            JSONArray representatives2 = legislatorDetails.getJSONArray("results");
            JSONObject representatives = representatives2.getJSONObject(0);
            JSONArray nextLegislator = representatives.getJSONArray("address_components");
            if (representatives.length() == 0) {
                return;
            }
            for (int i = 0; i < nextLegislator.length(); i++) {
                JSONObject object = nextLegislator.getJSONObject(i);
                String types = object.getString("types");
                if (types.toLowerCase().contains("administrative_area_level_2")) {
                    county = object.getString("short_name");
                }
                if (types.toLowerCase().contains("administrative_area_level_1")) {
                    state = object.getString("short_name");
                }
            }


            BufferedReader r = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources().getIdentifier("votingdata", "raw", getPackageName()))));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                response.append(line);
            }

            JSONArray countyDetails = new JSONArray(response.toString());
            for (int i = 0; i < countyDetails.length(); i++) {
                JSONObject object = countyDetails.getJSONObject(i);
                String statename = object.getString("state-postal");
                String countyname = object.getString("county-name");
                if (state.contains(statename) && county.contains(countyname)) {
                    results.add(object);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] randomLatLong() {
        ArrayList<String[]> COUNTRIES = new ArrayList<>();
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(getResources().openRawResource(getResources().getIdentifier("country", "raw", getPackageName())));

            BufferedReader reader = new BufferedReader(is);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split("\\s*,\\s*");
                COUNTRIES.add(cols);
            }
        }
        catch (Exception ex) {
            // handle exception
        }
        finally {
            try {
                is.close();
            }
            catch (Exception e) {
                // handle exception
            }
        }

        Random generator = new Random();
        int i = generator.nextInt(COUNTRIES.size());

        return COUNTRIES.get(i);
    }
}

