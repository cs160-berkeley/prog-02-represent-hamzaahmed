package com.example.hamza.prog02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            final String name = extras.getString("Name");
            TextView member = (TextView) findViewById(R.id.BarbaraName);
            member.setText(name);
        }
    }

    public void CommitteeContainerVisible(View view) {
        // Do something in response to zip button
        LinearLayout test = (LinearLayout) findViewById(R.id.CommitteesItemContainer);
        int visibility = test.getVisibility();
        if (visibility == 0) {
            test.setVisibility(View.GONE);
        } else {
            test.setVisibility(View.VISIBLE);
        }
    }

    public void BillContainerVisible(View view) {
        // Do something in response to zip button
        LinearLayout test = (LinearLayout) findViewById(R.id.BillItemContainer);
        int visibility = test.getVisibility();
        if (visibility == 0) {
            test.setVisibility(View.GONE);
        } else {
            test.setVisibility(View.VISIBLE);
        }
    }
}
