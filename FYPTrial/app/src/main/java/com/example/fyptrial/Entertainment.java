package com.example.fyptrial;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Entertainment extends AppCompatActivity implements MyAdapter.ClickListener {

    private ProgressBar mProgressBar;

    Boolean check = false;
    private RecyclerView rv;
    //ReadXlFile file;
    private List<AllEventsModel> eventList;
    //private List<EventsModel> eventList;
    private List<UserEventsModel> userEventsList;
    private MyAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventRef;
    DatabaseReference myRef;
    String email;
    String city;
    RatingBar likeButton;
    int categoryValue;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_recycler_view);

        likeButton = findViewById(R.id.like_bt);
        rv = findViewById(R.id.recycler_view);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        city = preferences.getString("userCity", "");

        Log.e("City from shared",city);
        Intent eventsIntent = getIntent();
        categoryValue = eventsIntent.getIntExtra("gridIndexValue", 0);

        eventList = new ArrayList<>();

        readAllData();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(Entertainment.this);

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        city = preferences.getString("userCity", "");

        Log.e("on res City from shared",city);
    }

    @Override
    public void onTouch(final int position) {

        Intent intent = new Intent(this, EventDescription.class);
        intent.putExtra("MyClass", (Serializable) eventList.get(position));
        startActivity(intent);
    }

    @Override
    public void onLongTouch(int position) {

    }

    public void readAllData() {
        myRef = database.getReference("All-Events");
        if (eventList.isEmpty()) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        AllEventsModel object = new AllEventsModel();
                        object = postSnapshot.getValue(AllEventsModel.class);
                        object.setTime(object.getTime().trim());
                        object.setDescription(object.getDescription().trim());
                        object.setVenue(object.getVenue().trim());
                        if(object.getCity().toLowerCase().equals(city.toLowerCase())) {
                            if (categoryValue == 0 && object.getType().equals("sports") && object.getRating() == -1) {
                                eventList.add(object);
                            } else if (categoryValue == 1 && object.getType().equals("entertainment") && object.getRating() == -1) {
                                eventList.add(object);
                            } else if (categoryValue == 2 && object.getType().equals("trips-adventures") && object.getRating() == -1) {
                                eventList.add(object);
                            } else if (categoryValue == 3 && object.getType().equals("food-drinks") && object.getRating() == -1) {
                                eventList.add(object);
                            } else if (categoryValue == 4 && object.getType().equals("business") && object.getRating() == -1) {
                                eventList.add(object);
                            } else if (categoryValue == 5 && object.getType().equals("workshops") && object.getRating() == -1) {
                                eventList.add(object);
                            }
                        }
                    }
                    while (true){
                        if(eventList.size() <= 10){
                            break;
                        }
                        else
                            {


                            int rando = (int) ((Math.random() * eventList.size()));
                            eventList.remove(rando);
                        }
                    }

                    adapter.eventList = eventList; // update the list in the adapter
                    adapter.notifyDataSetChanged(); // refresh

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        adapter = new MyAdapter(eventList, this, new MyAdapter.MyAdapterListener() {

            @Override
            public void iconRatingBarOnClick(final int position, int rating) {

                if (checkUser(position) == false) {
                    addUserEvent(position, rating);
                } else if (checkUser(position) == true) {
                    updateUserEvent(position, rating);
                }
                addUsersRatedEvents(position, rating);
//                AlertDialog diaBox = AskOption(position);
//                diaBox.show();

            }
        });

    }

    public void addUserEvent(final int position, final int rating) {

        String eventId = eventList.get(position).getId();
        String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        eventRef = database.getReference("Users-Events").push();
        UserEventsModel model = new UserEventsModel(
                userId,
                eventId,
                rating
        );
        eventRef.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateEvent(position, rating);
                    Toast.makeText(Entertainment.this, "Event added to your Favourites", Toast.LENGTH_LONG).show();
                    eventList.remove(position);

                    RecyclerView.LayoutManager manager = new LinearLayoutManager(Entertainment.this);
                    rv.setLayoutManager(manager);
                    rv.setAdapter(adapter);
                }
            }
        });
    }

    public void addUsersRatedEvents(final int position, final int rating) {

        String eventId = eventList.get(position).getId();

        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        eventRef = database.getReference("Users-Rated-Events").push();


        Log.e("User Id",userId);
        eventList.get(position).setId(userId);
        eventList.get(position).setRating(rating);
        Log.e("User Id's position",""+position);

        Log.e("User Id of eventList", eventList.get(position).getId());

        AllEventsModel model = new AllEventsModel(
                eventList.get(position).getId(),
                eventList.get(position).getTitle(),
                eventList.get(position).getTime(),
                eventList.get(position).getVenue(),
                eventList.get(position).getDescription(),
                eventList.get(position).getSource(),
                eventList.get(position).getPicture(),
                eventList.get(position).getType(),
                eventList.get(position).getCity(),
                eventList.get(position).getPastFuture(),
                eventList.get(position).getRating()
        );

        eventRef.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }
        });
        eventList.get(position).setId(eventId);
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
                                    Toast.makeText(Entertainment.this, "Rating Updated Successfully", Toast.LENGTH_LONG).show();
                                    eventList.remove(position);

                                    RecyclerView.LayoutManager manager = new LinearLayoutManager(Entertainment.this);
                                    rv.setLayoutManager(manager);
                                    rv.setAdapter(adapter);
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
                                    //Toast.makeText(Entertainment.this, "Rating in All-Events Updated Successfully", Toast.LENGTH_LONG).show();
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

    private AlertDialog AskOption(final int position)
    {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Alert from REL-EVENT")
                .setMessage("Thank You for rating our suggestion")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {



                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}
