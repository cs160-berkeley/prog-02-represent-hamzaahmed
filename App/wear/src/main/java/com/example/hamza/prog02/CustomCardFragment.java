package com.example.hamza.prog02;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hamza on 2/29/16.
 */

public class CustomCardFragment extends Fragment implements View.OnClickListener {

    String bioguideId;

    public static CustomCardFragment newInstance(String mName, String mPosition, String mParty, String mBioguideId) {
        CustomCardFragment fragmentDemo = new CustomCardFragment();
        Bundle args = new Bundle();
        args.putString("name", mName);
        args.putString("position", mPosition);
        args.putString("party", mParty);
        args.putString("bioguideId", mBioguideId);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_card_fragment, container, false);
        String name = getArguments().getString("name", "");
        String position = getArguments().getString("position", "");
        String party = getArguments().getString("party", "");
        bioguideId = getArguments().getString("bioguideId", "");

        View partyIcon = v.findViewById(R.id.icon);
        partyIcon.bringToFront();
        View nameView = v.findViewById(R.id.name);
        View partyBox = v.findViewById(R.id.bg);
        ImageView thumbnail = (ImageView) v.findViewById(R.id.img);

        //thumbnail.setImageBitmap(iconId);

        if (party.equalsIgnoreCase("D")) {
            partyBox.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.democrat));
            partyIcon.setBackgroundResource(R.drawable.democrat);
        } else if (party.equalsIgnoreCase("R")){
            partyBox.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.republican));
            partyIcon.setBackgroundResource(R.drawable.rebulican);
        } else if (party.equalsIgnoreCase("I")) {
            partyBox.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.independent));
            partyIcon.setBackgroundResource(R.drawable.rebulican);
        }
        View clickListener = v.findViewById(R.id.clickListener);
        View positionView = v.findViewById(R.id.position);
        ((TextView) nameView).setText(name);
        ((TextView) positionView).setText(position);
        clickListener.setOnClickListener(this);
       // Drawable d = getResources().getDrawable(iconId, this.getActivity().getTheme());
        //iconView.setBackground(d);
        return v;
    }

    @Override
    public void onClick(View v) {
        //YOUR CODE HERE
        View nameView = v.findViewById(R.id.name);
        String name = ((TextView) nameView).getText().toString();
        Intent sendIntent = new Intent(this.getActivity().getBaseContext(), WatchToPhoneService.class);
        sendIntent.putExtra("/send_signal", "click");
        sendIntent.putExtra("name", name);
        sendIntent.putExtra("bioguide", bioguideId);
        this.getActivity().startService(sendIntent);
    }

}
