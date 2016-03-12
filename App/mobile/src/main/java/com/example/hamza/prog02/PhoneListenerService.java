package com.example.hamza.prog02;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
            String[] data = name.split(" ");
            intent.putExtra("firstName", data[0]);
            intent.putExtra("lastName", data[1]);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.startActivity(intent);

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else if (messageEvent.getPath().equalsIgnoreCase(SHAKE)) {
            Intent intent = new Intent(this, CongressionalViewActivity.class);
            String[] LatLong = randomLatLong();
            intent.putExtra("randomLatLong", LatLong[0] + "," + LatLong[1]);
            String newLocation = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Toast.makeText(this, "New location:" + Arrays.toString(LatLong), Toast.LENGTH_LONG).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
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

        //for (String[] arr: COUNTRIES)
        //    Log.d("T", "COUNTRIES:" + Arrays.toString(arr));

        return COUNTRIES.get(i);
    }
}