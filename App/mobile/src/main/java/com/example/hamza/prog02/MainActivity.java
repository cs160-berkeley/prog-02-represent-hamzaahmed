package com.example.hamza.prog02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ZipButton = (Button) findViewById(R.id.ZipButton);
        Button LocationButton = (Button) findViewById(R.id.LocationButton);

        ZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToZipCode(v);
            }
        });

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("Location", "Istanbul");
                startService(sendIntent);
                goToCongress(v);
            }
        });
    }

    public void goToZipCode(View view) {
        // Do something in response to zip button
        Intent intent = new Intent(this, ZipCodeActivity.class);
        startActivity(intent);
    }

    public void goToCongress(View view) {
        // Do something in response to zip button
        Intent intent = new Intent(this, CongressionalViewActivity.class);
        startActivity(intent);
    }
}
