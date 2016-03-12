package com.example.hamza.prog02;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {
    ArrayList<String> results = new ArrayList<>();
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        Intent intent = new Intent(this, CongressionalView.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        for (String word: value.split("split")) {
            results.add(word);
        }
        intent.putExtra("CongressInfo", results.get(0));
        intent.putExtra("CountyInfo", results.get(1));
        startActivity(intent);
    }
}