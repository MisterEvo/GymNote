package com.sommerfeld.gymnote;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up variables
        BottomNavigationView bnw = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);


        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(MainActivity.this, a_workout_overview.class);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(MainActivity.this, CompletedLog.class);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                     //   Intent intent_analysis = new Intent(MainActivity.this, aAnalysis.class);
                     //   startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });
    }
}
