package com.example.fyptrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ListView myList;
    public String city;
    Context context;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    Button register;
    EditText et_name, et_city;
    String uEmail, uCity, uName;
    UserModel uModel = new UserModel();
    DatabaseReference userRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ReadXlFile file;
    //private List<EventsModel> eventList;
    private List<AllEventsModel> allEventsList;
    DatabaseReference eventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = RegisterActivity.this;

        file = new ReadXlFile();

        allEventsList = new ArrayList<>();

        allEventsList = file.readAllEventsData(getApplicationContext());

        myList = findViewById(R.id.lv);

        register = findViewById(R.id.register);
        et_name = findViewById(R.id.et_name);
        et_city = findViewById(R.id.et_city);

        list = new ArrayList<String>();

        list.add("Islamabad");
        list.add("Rawalpindi");
        list.add("Karachi");
        list.add("Lahore");
        list.add("Multan");
        list.add("Hyderabad");
        list.add("Faisalabad");
        list.add("Gujrat");
        list.add("Sialkot");
        list.add("Bahawalpur");
        list.add("Sargodha");
        list.add("Peshawar");
        list.add("Quetta");
        list.add("Attock");
        list.add("Abbottabad");
        list.add("Chakwal");

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
                uCity = et_city.getText().toString();
                SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                prefEditor.putString("MYCITY", uCity);
                prefEditor.apply();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //addAllFilteredEventsToDatabaseOnlyOnce();

                uEmail = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
                uName = et_name.getText().toString();
                uCity = et_city.getText().toString();


                if (!isEmpty(uName, uEmail, uCity)){

                    Toast.makeText(RegisterActivity.this, "Cannot submit an empty text field.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    uName = et_name.getText().toString();
                    uCity = et_city.getText().toString();

                    addUser();

                    Intent intent = new Intent(RegisterActivity.this, UserRequest.class);
                    startActivity(intent);

                }
            }
        });

    }

    public void addUser()
    {

        userRef = database.getReference("Users").push();
        uModel = new UserModel(uName, uEmail, uCity);

        userRef.setValue(uModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "User Added", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isEmpty(String name, String email, String userCity) {
        if (name.matches("") || userCity.matches(""))
            return false;

        return true;
    }

    public void addAllFilteredEventsToDatabaseOnlyOnce() {

        for (int position = 0; position < allEventsList.size(); position++) {
            Log.e("Update Database","Hanjiii"+allEventsList.get(position).getCity());
            eventRef = database.getReference("All-Events").push();

            String id = eventRef.child("All-Events").push().getKey();
            allEventsList.get(position).setId(id);



            AllEventsModel model = new AllEventsModel(
                    allEventsList.get(position).getId(),
                    allEventsList.get(position).getTitle(),
                    allEventsList.get(position).getTime(),
                    allEventsList.get(position).getVenue(),
                    allEventsList.get(position).getDescription(),
                    allEventsList.get(position).getSource(),
                    allEventsList.get(position).getPicture(),
                    allEventsList.get(position).getType(),
                    allEventsList.get(position).getCity(),
                    allEventsList.get(position).getPastFuture()
            );

            eventRef.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    }
                }
            });
        }
        Toast.makeText(RegisterActivity.this, "All Events added to the DATABASE", Toast.LENGTH_LONG).show();
    }

}
