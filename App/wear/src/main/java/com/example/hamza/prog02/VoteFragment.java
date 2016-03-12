package com.example.hamza.prog02;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Hamza on 2/29/16.
 */

public class VoteFragment extends Fragment {
    static String county;
    static String state;
    static Double obamaVal;
    static Double romneyVal;

    public static VoteFragment newInstance(String County, String State, Double ObamaVal, Double RomneyVal) {
        VoteFragment fragmentDemo = new VoteFragment();
        county = County;
        state = State;
        obamaVal = ObamaVal;
        romneyVal = RomneyVal;

        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_vote, container, false);

        TextView tv = (TextView) v.findViewById(R.id.icon2);
        TextView tv2 = (TextView) v.findViewById(R.id.name2);
        TextView countyView = (TextView) v.findViewById(R.id.county);


        double obama_val = obamaVal / 100.0;
        Log.d("T", obama_val + "");
        //double obama_val = Math.round(Math.random() * 100.0) / 100.0;
        double romney_val = 1.00 - obama_val;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) obama_val);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) romney_val);

        int obamaDisplay = (int) Math.ceil(obama_val * 100.0);
        tv.setText("OBAMA\n" + obamaDisplay  + "%");
        Log.d("T", obamaDisplay + "");
        int romneyDisplay = 100 - obamaDisplay;
        tv2.setText("ROMNEY\n" + romneyDisplay + "%");
        countyView.setText(county + ", " + state);
        tv.setLayoutParams(params);
        tv2.setLayoutParams(params2);

        return v;
    }

}
