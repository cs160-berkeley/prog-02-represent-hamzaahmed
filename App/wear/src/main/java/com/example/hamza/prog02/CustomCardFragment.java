package com.example.hamza.prog02;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Hamza on 2/29/16.
 */

public class CustomCardFragment extends Fragment implements View.OnClickListener {



    public static CustomCardFragment newInstance(String mName, String mPosition, int mIconId) {
        CustomCardFragment fragmentDemo = new CustomCardFragment();
        Bundle args = new Bundle();
        args.putString("name", mName);
        args.putString("position", mPosition);
        args.putInt("iconId", mIconId);
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
        int iconId = getArguments().getInt("iconId", 0);
        View iconView = v.findViewById(R.id.icon);
        View nameView = v.findViewById(R.id.name);
        View clickListener = v.findViewById(R.id.clickListener);
        View positionView = v.findViewById(R.id.position);
        ((TextView) nameView).setText(name);
        ((TextView) positionView).setText(position);
        clickListener.setOnClickListener(this);
        Drawable d = getResources().getDrawable(iconId, this.getActivity().getTheme());
        iconView.setBackground(d);
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
        this.getActivity().startService(sendIntent);
    }

}
