package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class aPlan extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newplan);



        //Set up variables
        BottomNavigationView bnw = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPlan);


        //Set the OnClickListener for our Fab to show the UI where the user can create different workout plans
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_Create = new Intent(aPlan.this, aCreate_plan.class);
                startActivity(intent_to_Create);
            }
        });


        bnw.setSelectedItemId(R.id.ic_new_plan);


        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        //Since we already are in this case, we do not reload the activity
                        break;
                    case R.id.ic_workout:
                        Intent intent_workout = new Intent(aPlan.this, aWorkout.class);
                        startActivity(intent_workout);
                        break;
                    case R.id.ic_analysis:
                        Intent intent_analysis = new Intent(aPlan.this, aAnalysis.class);
                        startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });
    }
}
