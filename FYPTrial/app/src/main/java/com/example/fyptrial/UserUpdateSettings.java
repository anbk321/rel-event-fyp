package com.example.fyptrial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserUpdateSettings extends AppCompatActivity {

    boolean check = false;
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
        setContentView(R.layout.activity_user_update_settings);

        context = UserUpdateSettings.this;

        checkUser();

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

//                addAllEventsToDatabase();

                uEmail = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
                uName = et_name.getText().toString();
                uCity = et_city.getText().toString();


                if (!isEmpty(uName, uEmail, uCity)){

                    Toast.makeText(UserUpdateSettings.this, "Cannot submit an empty text field.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    uName = et_name.getText().toString();
                    uCity = et_city.getText().toString();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserUpdateSettings.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userCity", uCity);
                    editor.apply();

                    updateUserEvent();

                    Intent intent = new Intent(UserUpdateSettings.this, UserRequest.class);
                    startActivity(intent);

                }
            }
        });

    }

    public void updateUserEvent()
    {

        uModel = new UserModel(uName, uEmail, uCity);
        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Query Query = myRef.orderByChild("userID").equalTo(userId);

        // Read from the database
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    child.getRef().child("city").setValue(uCity);
                    child.getRef().child("userName").setValue(uName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(UserUpdateSettings.this, "User Info Updated Successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public boolean isEmpty(String name, String email, String userCity) {
        if (name.matches("") || userCity.matches(""))
            return false;

        return true;
    }

    public Boolean checkUser()
    {
        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Query Query = myRef.orderByChild("userID").equalTo(userId);

        // Read from the database
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String name = child.child("userName").getValue().toString();
                    String userCity = child.child("city").getValue().toString();

                    et_name.setText(name);
                    et_city.setText(userCity);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        return check;
    }

}
