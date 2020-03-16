package com.example.fyptrial;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FoodAndDrinks extends AppCompatActivity implements MyAdapter.ClickListener{

    private RecyclerView rv = null;
    private List<AllEventsModel> eventList;
    private MyAdapter adapter = null;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventRef;
    DatabaseReference myRef;
    String email;
    RatingBar likeButton;
    //ReadXlFile file;
    Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_recycler_view);


        likeButton = findViewById(R.id.like_bt);
        //file = new ReadXlFile();

        eventList = new ArrayList<>();

        //eventList = file.readEventData(getApplicationContext(),"Sports");
        rv = findViewById(R.id.recycler_view);
        adapter = new MyAdapter(eventList, this, new MyAdapter.MyAdapterListener() {

            @Override
            public void iconRatingBarOnClick( final int position, int rating) {


                if(checkUser(position) == false)
                {
                    addUserEvent(position, rating);
                }
                else if(checkUser(position) == true)
                {
                    updateUserEvent(position, rating);
                }
//
            }
        });

        readAllData();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);




    }




    @Override
    public void onTouch(int position) {

        Intent intent=new Intent(this, EventDescription.class);
        intent.putExtra("MyClass", (Serializable) eventList.get(position));
        startActivity(intent);

    }

    @Override
    public void onLongTouch(int position) {

    }

    public void readAllData()
    {

        myRef = database.getReference("All-Events");
        if(eventList.isEmpty())
        {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());

                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        AllEventsModel object = new AllEventsModel();
                        object = postSnapshot.getValue(AllEventsModel.class);

                        if (object.getType().equals("food-drinks") && object.getRating() == -1)
                        {
                            eventList.add(object);
                        }
                        //Log.e("My Event ", "" + snapshot.getValue(AllEventsModel.class).toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
    public void addUserEvent(final int position, final int rating)
    {

        String eventId = eventList.get(position).getId();
        String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);

        eventRef = database.getReference("Users-Events").push();
        UserEventsModel model = new UserEventsModel(
                userId,
                eventId,
                rating
        );
        eventRef.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    updateEvent(position,rating);
                    Toast.makeText(FoodAndDrinks.this, "Event added to your Activity", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateUserEvent(final int position, final int rating)
    {
        final String eID = eventList.get(position).getId();
        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users-Events");
        Query Query = myRef.orderByChild("userId").equalTo(userId);

        // Read from the database
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String eventId = child.child("eventId").getValue().toString();

                    if(eID.equals(eventId)) {
                        child.getRef().child("rating").setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    updateEvent(position,rating);
                                    Toast.makeText(FoodAndDrinks.this, "Rating Updated Successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        });;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void updateEvent(final int position, final int rating){
        final String eID = eventList.get(position).getId();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("All-Events");

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String eventId = child.child("id").getValue().toString();

                    if(eID.equals(eventId)) {
                        child.getRef().child("rating").setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    //Toast.makeText(SportEventsList.this, "Rating in All-Events Updated Successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public Boolean checkUser(final int position)
    {
        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        final String eID = eventList.get(position).getId();


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users-Events");
        Query Query = myRef.orderByChild("userId").equalTo(userId);

        // Read from the database
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String eventId = child.child("eventId").getValue().toString();

                    if(eID.equals(eventId)) {
                        check = true;
                        break;
                    }
                    else {
                        check = false;
                    }
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
