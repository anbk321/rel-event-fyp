package com.example.fyptrial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class EventDetails extends AppCompatActivity {


    ListView myList;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    EditText et_city;
    String city;

    Button result_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        myList = findViewById(R.id.lv);

        et_city = findViewById(R.id.et_city);
        result_bt = findViewById(R.id.result);

        list = new ArrayList<String>();

        Field[] fields=R.raw.class.getFields();

        for(int count=0; count < fields.length-1; count++){
            list.add(fields[count].getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                myList.setAdapter(adapter);
                adapter.getFilter().filter(et_city.getText().toString());
                int iter = 0;
                if(et_city.getText().toString().length() != 0)
                {
                    for(int i = 0; i < list.size(); i++)
                    {
                        String temp = list.get(i);
                        Character ch1 = Character.toUpperCase(et_city.getText().toString().charAt(0));
                        Character ch2 = Character.toUpperCase(temp.charAt(0));
                        if(et_city.getText().toString().length() != 0 && ch1 == ch2)
                        {
                            Collections.swap(list, i, iter);
                            iter++;
                        }

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                et_city.setText(list.get(i));
            }
        });



        result_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                city = et_city.getText().toString();


                if (!isEmpty(city)) {

                    Toast.makeText(EventDetails.this, "Cannot submit an empty text field.", Toast.LENGTH_LONG).show();
                }
                else
                {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(EventDetails.this, SentimentResult.class);
                            startActivity(intent);

                            et_city.getText().clear();
                        }
                    }, 2000);

                }
            }

        });
    }

    public boolean isEmpty(String city) {
        if (city.matches(""))
            return false;

        return true;
    }
}
