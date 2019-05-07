package com.sommerfeld.gymnote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class aCreate_plan extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan);

        ListView lv = (ListView) findViewById(R.id.lv_excer);
        final EditText execName = (EditText) findViewById(R.id.et_exec_name);
        final EditText execDay = (EditText) findViewById(R.id.et_exec_day);
        Button addExec = (Button) findViewById(R.id.btn_add);
        final ArrayList<String> arrayList;
        final ArrayAdapter<String> adapter;

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(aCreate_plan.this,android.R.layout.simple_list_item_1, arrayList);
        lv.setAdapter(adapter);

        addExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultName = execName.getText().toString();
                String resultDay = execDay.getText().toString();
                arrayList.add(resultName);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
