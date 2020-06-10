package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class EventDetails extends AppCompatActivity implements View.OnClickListener{


    ListView myList;
    ListView myListCategories;
    ArrayList<String> list;
    ArrayList<String> listCategories;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterCategories;

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    EditText et_city;
    EditText et_time;
    EditText et_category;
    String city;
    String category;
    String time;
    String date;

    Button result_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        myList = findViewById(R.id.lv);
        myListCategories = findViewById(R.id.lvC);

        et_city = findViewById(R.id.et_city);
        et_category = findViewById(R.id.et_category);
        //et_time = findViewById(R.id.et_time);
        result_bt = findViewById(R.id.result);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        list = new ArrayList<String>();
        listCategories = new ArrayList<String>();

        listCategories.add("BUSINESS");
        listCategories.add("WORKSHOPS");
        listCategories.add("SPORTS");
        listCategories.add("TRIPS-ADVENTURES");
        listCategories.add("ENTERTAINMENT");
        listCategories.add("FOOD-DRINKS");

        Field[] fields=R.raw.class.getFields();

        for(int count=0; count < fields.length-1; count++){
            list.add(fields[count].getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listCategories);


        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(adapter.getCount()==0)
                {
                    adapter.addAll(list);
                    myList.setAdapter(adapter);
                }
                else
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

        et_category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(adapterCategories.getCount()==0)
                {
                    adapterCategories.addAll(listCategories);
                    myListCategories.setAdapter(adapterCategories);
                }
                else
                    myListCategories.setAdapter(adapterCategories);

                adapterCategories.getFilter().filter(et_category.getText().toString());
                int iter = 0;
                if(et_category.getText().toString().length() != 0)
                {
                    for(int i = 0; i < listCategories.size(); i++)
                    {
                        String temp = listCategories.get(i);
                        Character ch1 = Character.toUpperCase(et_category.getText().toString().charAt(0));
                        Character ch2 = Character.toUpperCase(temp.charAt(0));


                        if(et_category.getText().toString().length() != 0 && ch1 == ch2)
                        {

                            Collections.swap(listCategories, i, iter);
                            iter++;
                        }

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        myListCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                et_category.setText(myListCategories.getAdapter().getItem(i).toString());
                adapterCategories.clear();
                //myListCategories.setAdapter(null);
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                et_city.setText(myList.getAdapter().getItem(i).toString());
                adapter.clear();
                //myList.setAdapter(null);
            }
        });



        result_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                city = et_city.getText().toString();
                category = et_category.getText().toString();
                category = category.toUpperCase();
                time = txtTime.getText().toString();
                date = txtDate.getText().toString();

                Date todayDate = new Date();
                Log.e("AAJ KI Date: ", todayDate.toString());
                Date dateReal = null;

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    dateReal = format.parse(date);
                    System.out.println(dateReal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int count = 0;
                int count1 = 0;
                for(int i = 0; i < list.size(); i++)
                {
                    city = city.toLowerCase();
                    //Log.e("Text field city: ", uCity);

                    String temp = list.get(i).toLowerCase();
                    Log.e("list item city: ", temp);
                    if(temp.equals(city) && isEmpty(city))
                    {
                        count++;
                    }
                }

                for(int i = 0; i < listCategories.size(); i++)
                {
                    category = category.toUpperCase();
                    //Log.e("Text field city: ", uCity);

                    String temp = listCategories.get(i).toUpperCase();
                    Log.e("list item city: ", temp);
                    if(temp.equals(category) && isEmpty(category))
                    {
                        count1++;
                    }
                }

                if(count1 == 0 && !isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select a Category from the list.", Toast.LENGTH_LONG).show();
                    adapterCategories.addAll(list);
                }

                else if(count == 0 && !isEmpty(category))
                {
                    Toast.makeText(EventDetails.this, "Please select an Event from the list.", Toast.LENGTH_LONG).show();
                    adapter.addAll(list);
                }

                //Log.e("Entered Date: ", dateReal.toString());

                else if(dateReal != null && dateReal.compareTo(todayDate) < 0)
                {
                    Toast.makeText(EventDetails.this, "You have to enter the future date.", Toast.LENGTH_LONG).show();
                }

                else if ((!isEmpty(time) && !isEmpty(category) && !isEmpty(date)) && !isEmpty(city)) {

                    Toast.makeText(EventDetails.this, "Cannot submit an empty text field.", Toast.LENGTH_LONG).show();
                }
                //For popular event
                else if ((!isEmpty(time) && !isEmpty(category) && !isEmpty(date)) && isEmpty(city)) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(EventDetails.this, SentimentResult.class);
                            startActivity(intent);

                        }
                    }, 2000);
                }
                //For our own event
                else if ((isEmpty(time) && isEmpty(category) && isEmpty(date)) && !isEmpty(city))
                {
                    String temp = time;
                    String arr[] = temp.split(":", 2);
                    String hours = arr[0];
                    int hoursInt = Integer.parseInt(hours);
                    SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    prefEditor.putString("CATEGORY", category);
                    prefEditor.putInt("HOURS", hoursInt);
                    prefEditor.apply();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(EventDetails.this, SentimentResult.class);
                            startActivity(intent);

                        }
                    }, 2000);
                }
                else if ((!isEmpty(time) || !isEmpty(category) || !isEmpty(date)) && !isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please enter all fields Category Date & Time of your event", Toast.LENGTH_LONG).show();
                }
                else if ((!isEmpty(time) && isEmpty(category) && !isEmpty(date)) && !isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please enter all fields Category Date & Time of your event", Toast.LENGTH_LONG).show();
                }
                else if ((isEmpty(time) && !isEmpty(category) && !isEmpty(date)) && !isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please enter all fields Category Date & Time of your event", Toast.LENGTH_LONG).show();
                }
                else if ((!isEmpty(time) && !isEmpty(category) && isEmpty(date)) && !isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please enter all fields Category Date & Time of your event", Toast.LENGTH_LONG).show();
                }
                else if((!isEmpty(time) && !isEmpty(category) && !isEmpty(date)) && !isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select 1 option: Popular Event OR Enter your own Event details", Toast.LENGTH_LONG).show();
                }
                else if((isEmpty(time) && !isEmpty(category) && !isEmpty(date)) && isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select 1 option: Popular Event OR Enter your own Event details", Toast.LENGTH_LONG).show();
                }
                else if((!isEmpty(time) && isEmpty(category) && !isEmpty(date)) && isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select 1 option: Popular Event OR Enter your own Event details", Toast.LENGTH_LONG).show();
                }
                else if((!isEmpty(time) && !isEmpty(category) && isEmpty(date)) && isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select 1 option: Popular Event OR Enter your own Event details", Toast.LENGTH_LONG).show();
                }
                else if((isEmpty(time) && isEmpty(category) && isEmpty(date)) && isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select 1 option: Popular Event OR Enter your own Event details", Toast.LENGTH_LONG).show();
                }
                else if((isEmpty(time) || isEmpty(category) || isEmpty(date)) && isEmpty(city))
                {
                    Toast.makeText(EventDetails.this, "Please select 1 option: Popular Event OR Enter your own Event details", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public boolean isEmpty(String city) {
        if (city.matches(""))
            return false;

        return true;
    }
}
