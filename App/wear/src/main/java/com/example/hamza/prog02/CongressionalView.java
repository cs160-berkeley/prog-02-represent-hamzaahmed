package com.example.hamza.prog02;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

public class CongressionalView extends Activity {

    private SensorManager mSensorManager;
    private ShakeDetector mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        final GridViewPager mGridPager = (GridViewPager) findViewById(R.id.pager);
        Bundle extras = getIntent().getExtras();
        final String CongressInfo = extras.getString("CongressInfo");
        final String CountyInfo = extras.getString("CountyInfo");

        try {
            mGridPager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager(), CongressInfo, CountyInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeDetector();

        mSensorListener.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            public void onShake() {
                Intent sendIntent = new Intent(CongressionalView.this, WatchToPhoneService.class);
                sendIntent.putExtra("/send_signal", "shake");
                startService(sendIntent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

}
