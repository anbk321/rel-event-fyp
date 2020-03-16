package com.example.fyptrial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference userDataBase;
    UserModel user;
    Boolean check = false;


    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        handler = new Handler();

//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 3000);

        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.d("I am on start: ", ": " + userId);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Query Query = myRef.orderByChild("userID").equalTo(userId);

        // Read from the database
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    final String id = child.child("userID").getValue().toString();

                    if(userId.equals(id)) {
                        check = true;
                        Log.d("I am: ", "going in UserRequest: " + userId);
                        Intent intent = new Intent(MainActivity.this, UserRequest.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check == false)
                {
                    Log.d("I am ", "going in Register..........: " + userId);
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
