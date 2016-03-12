package com.example.hamza.prog02;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.*;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "BUK042kZRYAmA940jyMkNXLbL";
    private static final String TWITTER_SECRET = "kqsJdVmVaklxrHd0NSskiI8Nc2ZBObKJt7QbbEIi7xnR5IqoFI";


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public Location LastLocation;
    public static int UPDATE_INTERVAL_MS = 10000;
    public static int FASTEST_INTERVAL_MS = 20000;
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);


        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        mGoogleApiClient = buildGoogleApiClient(this);

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

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
                goToCongress(v);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    protected GoogleApiClient buildGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .addApi(LocationServices.API)
                .build();
    }

    public void goToZipCode(View view) {
        // Do something in response to zip button
        Intent intent = new Intent(this, ZipCodeActivity.class);
        startActivity(intent);
    }

    public void goToCongress(View view) {
        // Do something in response to zip button
        Intent intent = new Intent(MainActivity.this, CongressionalViewActivity.class);
        if (LastLocation != null) {
            intent.putExtra("latitude", LastLocation.getLatitude() + "");
            intent.putExtra("longitude", LastLocation.getLongitude() + "");
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
         LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (LastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
        } else {
            handleNewLocation(LastLocation);
        }
    }

    private void handleNewLocation(Location location) {
        Log.d("T", location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}
