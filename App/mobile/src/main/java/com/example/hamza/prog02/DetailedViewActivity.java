package com.example.hamza.prog02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailedViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private String firstName;
    private String lastName;
    private String title;
    private String party;
    private String termEnd;
    private String bioguideID;
    private String committeeName;
    private String billName;
    private String billDate;
    private Bitmap thumbnail;

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private List<String> committeesList;
    private List<String> billsList;
    private List<String> datesList;


    private CommitteesAdapter adapter;
    private BillsAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Button committeesContainer = (Button) findViewById(R.id.committeesContainer);
        Button billContainer = (Button) findViewById(R.id.billContainer);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView2.setLayoutManager(llm2);
        mRecyclerView2.setAdapter(adapter);

        committeesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiKey = "b77ebc6b16a64f1ab2a2a1c8d0271963";
                String url = "https://congress.api.sunlightfoundation.com/committees?member_ids=" + bioguideID + "&apikey=" + apiKey;
                Log.d("T", "URLLLLLL: " + url);
                new AsyncHttpTask().execute(url, "committee");
            }
        });

        billContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiKey = "b77ebc6b16a64f1ab2a2a1c8d0271963";
                String url = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=" + bioguideID + "&apikey=" + apiKey;
                Log.d("T", "URLLLLLL: " + url);
                new AsyncHttpTask().execute(url, "bill");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = this.getIntent().getExtras();
        String firstName = null;
        String lastName = null;
        if (extras != null) {
            firstName = extras.getString("firstName");
            lastName = extras.getString("lastName");
            thumbnail = extras.getParcelable("thumbnail");
        }

        // Downloading data from below url
        String apiKey = "b77ebc6b16a64f1ab2a2a1c8d0271963";
        String url = "https://congress.api.sunlightfoundation.com/legislators?first_name="+ firstName + "&last_name="+ lastName +"&apikey=" + apiKey;
        Log.d("T", "URLLLLLL: " + url);
        new AsyncHttpTask().execute(url, "member");
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        private String callType = null;

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                callType = params[1];
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                Log.d("T", statusCode + " hello");
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("T", "YESSSSSS");
                    if (callType == "member") {
                        parseMember(response.toString());
                    } else if (callType == "committee") {
                        Log.d("T", "CALLING PARSE COMMITTEES");
                        parseCommittees(response.toString());
                    }  else if (callType == "bill") {
                        Log.d("T", "CALLING PARSE BILLS");
                        parseBills(response.toString());
                    }
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("T", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                if (callType.equalsIgnoreCase("member")) {
                    TextView name = (TextView) findViewById(R.id.name);
                    name.setText(firstName + " " + lastName);
                    TextView partyText = (TextView) findViewById(R.id.party);
                    partyText.setText(party);
                    TextView titleText = (TextView) findViewById(R.id.title);
                    ImageView partyIcon = (ImageView) findViewById(R.id.partyIcon);
                    ImageView thumbnailIcon = (ImageView) findViewById(R.id.thumbnail);
                    thumbnailIcon.setImageBitmap(thumbnail);

                    if (title.equalsIgnoreCase("sen")) {
                        title = "Senator";
                    } else if (title.equalsIgnoreCase("rep")) {
                        title = "Representative";
                    }  else if (title.equalsIgnoreCase("del")) {
                        title = "Delegate";
                    } else if (title.equalsIgnoreCase("com")) {
                        title = "Commissioner";
                    }

                    titleText.setText(title);
                    TextView term = (TextView) findViewById(R.id.termEnd);
                    //TextView partyText = (TextView) findViewById(R.id.termEnd);

                    term.setText(termEnd.substring(0, 4));
                    LinearLayout partyBox = (LinearLayout) findViewById(R.id.partyBox);
                    if (party.equalsIgnoreCase("Democrat")) {
                        partyBox.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.democrat));
                        partyIcon.setBackgroundResource(R.drawable.democrat);
                    } else if (party.equalsIgnoreCase("Republican")){
                        partyBox.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.republican));
                        partyIcon.setBackgroundResource(R.drawable.rebulican);
                    } else if (party.equalsIgnoreCase("Independent")) {
                        partyBox.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.independent));
                        partyIcon.setBackgroundResource(R.drawable.twitter);
                    }
                } else if (callType.equalsIgnoreCase("committee")) {
                    adapter = new CommitteesAdapter(DetailedViewActivity.this, committeesList);
                    mRecyclerView.setAdapter(adapter);
                    CommitteeContainerVisible();
                } else if (callType.equalsIgnoreCase("bill")) {
                    adapter2 = new BillsAdapter(DetailedViewActivity.this, billsList, datesList);
                    mRecyclerView2.setAdapter(adapter2);
                    BillContainerVisible();
                }
            } else {
                Toast.makeText(DetailedViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parseMember(String jsonData) throws JSONException {
        try {
            JSONObject legislatorDetails = new JSONObject(jsonData);
            JSONArray representatives = legislatorDetails.getJSONArray("results");

            if (representatives.length() == 0) {
                Log.d("T", "LENGTH IS 0");
                return;
            }

            JSONObject nextLegislator = representatives.getJSONObject(0);

            firstName = nextLegislator.getString("first_name");
            lastName = nextLegislator.getString("last_name");
            title = nextLegislator.getString("title");
            party = nextLegislator.getString("party");
            if (party.equalsIgnoreCase("D")) {
                party = "Democrat";
            } else if (party.equalsIgnoreCase("R")){
                party = "Republican";
            } else if (party.equalsIgnoreCase("I")) {
                party = "Independent";
            }
            termEnd = nextLegislator.getString("term_end");
            bioguideID = nextLegislator.getString("bioguide_id");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseCommittees(String jsonData) throws JSONException {
        try {
            JSONObject legislatorDetails = new JSONObject(jsonData);
            JSONArray committees = legislatorDetails.getJSONArray("results");
            committeesList = new ArrayList<>();

            if (committees.length() == 0) {
                return;
            }
            for (int i = 0; i < committees.length(); i++) {
                JSONObject nextCommittee = committees.getJSONObject(i);

                committeeName = nextCommittee.getString("name");
                committeesList.add(committeeName);
                Log.d("T", "COMMITTEE NAME" + committeeName);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseBills(String jsonData) throws JSONException {
        try {
            JSONObject legislatorDetails = new JSONObject(jsonData);
            JSONArray bills = legislatorDetails.getJSONArray("results");
            billsList = new ArrayList<>();
            datesList = new ArrayList<>();

            if (bills.length() == 0) {
                return;
            }
            for (int i = 0; i < bills.length(); i++) {
                JSONObject nextBill = bills.getJSONObject(i);

                billName = nextBill.getString("short_title");
                Log.d("T", "BILL NAME: " + billName);
                if (billName.equalsIgnoreCase("null")) {
                    billName = nextBill.getString("official_title");
                }
                billDate = nextBill.getString("introduced_on");

                billsList.add(billName);
                datesList.add(billDate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void CommitteeContainerVisible() {
        // Do something in response to zip button
        LinearLayout test = (LinearLayout) findViewById(R.id.CommitteesItemContainer);
        int visibility = test.getVisibility();
        if (visibility == 0) {
            test.setVisibility(View.GONE);
        } else {
            test.setVisibility(View.VISIBLE);
        }
    }

    public void BillContainerVisible() {
        // Do something in response to zip button
        LinearLayout test = (LinearLayout) findViewById(R.id.BillItemContainer);
        int visibility = test.getVisibility();
        if (visibility == 0) {
            test.setVisibility(View.GONE);
        } else {
            test.setVisibility(View.VISIBLE);
        }
    }
}
