package com.example.hamza.prog02;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String SHAKE = "/send_shake";
    private static final String CLICK = "/send_click";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if (messageEvent.getPath().equalsIgnoreCase(CLICK)) {

            // Value contains the String we sent over in WatchToPhoneService
            String name = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent intent = new Intent(this, DetailedViewActivity.class);
            intent.putExtra("Name", name);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.startActivity(intent);

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else if (messageEvent.getPath().equalsIgnoreCase(SHAKE)) {
            Intent intent = new Intent(this, CongressionalViewActivity.class);

            String newLocation = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Toast.makeText(this, "New location:" + newLocation, Toast.LENGTH_LONG).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}