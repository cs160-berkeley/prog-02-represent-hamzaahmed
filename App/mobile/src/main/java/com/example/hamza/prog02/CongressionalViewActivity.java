package com.example.hamza.prog02;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CongressionalViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);

        ImageButton WebButton = (ImageButton) findViewById(R.id.thirdIcon);
        ImageButton PartyButton = (ImageButton) findViewById(R.id.firstIcon);
        ImageButton EmailButton = (ImageButton) findViewById(R.id.secondIcon);

        ImageButton mainButton = (ImageButton) findViewById(R.id.BarbaraImage);
        mainButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CongressionalViewActivity.this, DetailedViewActivity.class);
                startActivity(intent);

            }
        });

        WebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.boxer.senate.gov/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

                startActivity(launchBrowser);
            }
        });

        PartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("http://www.californiademocrat.com/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

                startActivity(launchBrowser);
            }
        });

        EmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                String[] TO = {"senator@boxer.senate.gov"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                startActivity(Intent.createChooser(emailIntent, "Send your email using:"));
            }
        });


    }

    public void TweetVisible(View view) {
        // Do something in response to zip button
        LinearLayout test = (LinearLayout) findViewById(R.id.CommitteesItemContainer);
        int visibility = test.getVisibility();
        if (visibility == 0) {
            test.setVisibility(View.GONE);
        } else {
            test.setVisibility(View.VISIBLE);
        }
    }
}
