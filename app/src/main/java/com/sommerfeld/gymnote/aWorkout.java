package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class aWorkout extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newevent);



        //Set up variables
        BottomNavigationView bnw = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);


        bnw.setSelectedItemId(R.id.ic_workout);
        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(aWorkout.this, aPlan.class);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        //Since we already are in this case, we do not reload the activity
                        break;
                    case R.id.ic_analysis:
                        Intent intent_analysis = new Intent(aWorkout.this, aAnalysis.class);
                        startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });
    }
}
