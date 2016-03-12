package com.example.hamza.prog02;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class ZipCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zip_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        EditText editZip = (EditText) findViewById(R.id.editZip);
        editZip.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            //Intent sendIntent = new Intent(ZipCodeActivity.this, PhoneToWatchService.class);
                            EditText ZipCode = (EditText) findViewById(R.id.editZip);
                            String ZipText = ZipCode.getText().toString();
                            // sendIntent.putExtra("zipCode", ZipText);
                            // startService(sendIntent);
                            Intent intent = new Intent(ZipCodeActivity.this, CongressionalViewActivity.class);
                            intent.putExtra("zipCode", ZipText);
                            startActivity(intent);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
