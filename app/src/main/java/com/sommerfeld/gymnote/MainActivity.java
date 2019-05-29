package com.sommerfeld.gymnote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedRepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct";
    TextView total_workout;
    TextView last_workout;
    private ArrayList<Completed> mCompleteds;
    private CompletedRepo mCompletedRepo;
    public static final int PICKFILE_RESULT_CODE = 1;
    private Uri fileUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        mCompleteds = new ArrayList<Completed>();
        mCompletedRepo = new CompletedRepo(this);

        //Set up variables
        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        bnw.setSelectedItemId(R.id.ic_dashboard);
        total_workout = findViewById(R.id.tv_main_workout_total);
        last_workout = findViewById(R.id.tv_main_last_workout);

        retrieveLog();

        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.ic_dashboard:
                        //Current view
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(MainActivity.this, a_workout_overview.class);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(MainActivity.this, CompletedLog.class);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        Intent intent_analysis = new Intent(MainActivity.this, graphics.class);
                        startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });
    }

    private void retrieveLog() {
        mCompletedRepo.retrieveCompletedTask().observe(this, new Observer<List<Completed>>() {
            @Override
            public void onChanged(List<Completed> completeds) {

                if (mCompleteds.size() > 0) {
                    mCompleteds.clear();
                }
                if (completeds != null) {
                    try {
                        mCompleteds.addAll(completeds);
                        String size = String.valueOf(mCompleteds.size());
                        total_workout.setText(size);
                        String lastWorkout = mCompleteds.get((mCompleteds.size() - 1)).getTitle();
                        last_workout.setText(lastWorkout);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_import:

                String backup1 = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "workoutsExport.db";
                String backup2 = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "workoutsExport.db-shm";
                String backup3 = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "workoutsExport.db-wal";

                String backupCOM1 = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "completedExport.db";
                String backupCOM2 = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "completedExport.db-shm";
                String backupCOM3 = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "completedExport.db-wal";


                String backupSharedPrefs = Environment.getExternalStorageDirectory() + File.separator + "GymNote" + File.separator + "pref_fileExport.xml";

                importFiles(backup1, "/databases/workouts.db");
                importFiles(backup2, "/databases/workouts.db-shm");
                importFiles(backup3, "/databases/workouts.db-wal");

                importFiles(backupCOM1, "/databases/completed.db");
                importFiles(backupCOM2, "/databases/completed.db-shm");
                importFiles(backupCOM3, "/databases/completed.db-wal");

                importFiles(backupSharedPrefs, "/shared_prefs/pref_file.xml");

                //Restart App to load new db
                Intent mStartActivity = new Intent(this, MainActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);

                break;
            case R.id.menu_export:
                try {
                    String DB_FILEPATH1 = getDataDir().toString() + "/databases/workouts.db";
                    String DB_FILEPATH2 = getDataDir().toString() + "/databases/workouts.db-shm";
                    String DB_FILEPATH3 = getDataDir().toString() + "/databases/workouts.db-wal";

                    String DB_FILEPATHCOM1 = getDataDir().toString() + "/databases/completed.db";
                    String DB_FILEPATHCOM2 = getDataDir().toString() + "/databases/completed.db-shm";
                    String DB_FILEPATHCOM3 = getDataDir().toString() + "/databases/completed.db-wal";

                    String SHAREDPREFS = getDataDir().toString() + "/shared_prefs/pref_file.xml";

                    exportFiles(DB_FILEPATH1, "workoutsExport.db");
                    exportFiles(DB_FILEPATH2, "workoutsExport.db-shm");
                    exportFiles(DB_FILEPATH3, "workoutsExport.db-wal");

                    exportFiles(DB_FILEPATHCOM1, "completedExport.db");
                    exportFiles(DB_FILEPATHCOM2, "completedExport.db-shm");
                    exportFiles(DB_FILEPATHCOM3, "completedExport.db-wal");

                    exportFiles(SHAREDPREFS, "pref_fileExport.xml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private boolean importFiles(String inputFile, String outputFile) {
        InputStream in = null;
        OutputStream out = null;

        //Clear the destination file first
        try {
            new File(getDataDir().toString() + outputFile).delete();
            Log.d(TAG, "importFiles: Deleted: " + outputFile);
        } catch (Exception e) {
            Log.e(TAG, "importFiles: " + e.getMessage());
        }

        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(getDataDir().toString() + outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();
            out.close();
            out = null;
            Toast.makeText(this, "importFiles: File has been copied to: " + Environment.getExternalStorageDirectory() + File.separator + "GymNote" + outputFile, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException fnfe1) {
            Log.e(TAG, fnfe1.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return true;
    }


    private boolean exportFiles(String inputFile, String outputFile) throws IOException {

        InputStream in = null;
        OutputStream out = null;

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "GymNote");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (success) {
            //Folder is existing
            Log.d(TAG, "exportFiles: Folder created: " + folder.toPath());
            try {
                in = new FileInputStream(inputFile);
                out = new FileOutputStream(folder.toPath() + File.separator + outputFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                out.flush();
                out.close();
                out = null;
                Log.d(TAG, "exportFiles: File has been copied to: " + folder.toPath() + File.separator + outputFile);
            } catch (FileNotFoundException fnfe1) {
                Log.e(TAG, fnfe1.getMessage());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.d(TAG, "exportFiles: Failed to create folder: " + folder.toPath());
        }

        return true;
    }
}
