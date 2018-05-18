package com.abhiinteractive.cricketscorecounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by Abhijeet on 21-12-2017.
 */

public class LauncherActivity extends Activity {

    Button startNewMatch;
    Button loadMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        MobileAds.initialize(this, "ca-app-pub-6229326724546843~1059025958");

        startNewMatch = findViewById(R.id.start_new_match);
        startNewMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                intent.putExtra("continue", false);
                startActivity(intent);
            }
        });

        loadMatch = findViewById(R.id.load_match);
        loadMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                intent.putExtra("continue", true);
                startActivity(intent);
            }
        });
    }

}
