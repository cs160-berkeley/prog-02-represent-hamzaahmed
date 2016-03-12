package com.example.hamza.prog02;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;
    private ArrayList<CongressItem> CongressPeople = new ArrayList<>();;
    String firstName;
    String lastName;
    String title;
    String party;
    String state;
    String county;
    Double ObamaVal;
    Double RomneyVal;
    Bitmap mIcon;

    public SampleGridPagerAdapter(Context context, FragmentManager fm, String CongressInfo, String CountyInfo) throws JSONException {
        super(fm);
        mContext = context;
        initPages(CongressInfo, CountyInfo);
    }

    private void initPages(String CongressInfo, String CountyInfo) {
        try {
            mPages = new ArrayList<>();
            parseResult(CongressInfo);
            parseResult2(CountyInfo);
            SimpleRow row1 = new SimpleRow();
            for (int i = 0; i < CongressPeople.size(); i++) {
                row1.addPages(CongressPeople.get(i));
            }
            SimpleRow row2 = new SimpleRow();
            row2.addPages(new CongressItem("Vote", "View", "County", null, ""));
            //row2.addPages(new SimplePage("Vote View", "County", R.drawable.democrat, 0));
            mPages.add(row1);
            mPages.add(row2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseResult(String CongressInfo) throws JSONException {
        try {
            //String temp = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            JSONObject legislatorDetails = new JSONObject(CongressInfo);
            JSONArray representatives = legislatorDetails.getJSONArray("results");

            if (representatives.length() == 0) {
                return;
            }
            for (int i = 0; i < representatives.length(); i++) {
                JSONObject nextLegislator = representatives.getJSONObject(i);

                firstName = nextLegislator.getString("first_name");
                String lastName = nextLegislator.getString("last_name");
                String title = nextLegislator.getString("title");
                String party = nextLegislator.getString("party");
                String bioGuideId = nextLegislator.getString("bioguide_id");

                CongressPeople.add(new CongressItem(firstName, lastName, title, bioGuideId, party));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseResult2(String CountyInfo) throws JSONException {
        try {
            JSONObject legislatorDetails = new JSONObject(CountyInfo);
            state = legislatorDetails.getString("state-postal");
            county = legislatorDetails.getString("county-name");
            ObamaVal = legislatorDetails.getDouble("obama-percentage");
            RomneyVal = legislatorDetails.getDouble("romney-percentage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        CongressItem page = (mPages.get(row)).getPages(col);
        if (page.getName().equalsIgnoreCase("Vote View")) {
            VoteFragment fragment = VoteFragment.newInstance(county, state, ObamaVal, RomneyVal);
            return fragment;
        }
        CustomCardFragment fragment = CustomCardFragment.newInstance(page.getName(), page.getTitle(), page.getParty(), page.getBioguideId());
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        //SimplePage page = (mPages.get(row)).getPages(col);
        //Drawable d = mContext.getResources().getDrawable(page.mBackgroundId, mContext.getTheme());
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        return transparentDrawable;
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }
}