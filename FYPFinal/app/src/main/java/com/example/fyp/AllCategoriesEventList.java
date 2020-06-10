package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllCategoriesEventList extends AppCompatActivity implements MyAdapter.ClickListener{


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
    RatingBar likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_recycler_view);


        //file = new ReadXlFile();

        likeButton = findViewById(R.id.like_bt);

        eventList = new ArrayList<>();
        //eventList = file.readEventData(getApplicationContext(),"Entertainment");

        rv = findViewById(R.id.recycler_view);
        adapter = new MyAdapter(eventList, this, new MyAdapter.MyAdapterListener() {

            @Override
            public void iconRatingBarOnClick( final int position, int rating) {


                String eventId = eventList.get(position).getId();
                String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);

                if(!checkUser(position))
                {
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
                                Toast.makeText(AllCategoriesEventList.this, "Event added to your Activity", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(AllCategoriesEventList.this, "You already rated this Event", Toast.LENGTH_LONG).show();
//
            }
        });

        readAllData();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(AllCategoriesEventList.this);

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);

    }

    @Override
    public void onTouch(final int position) {

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

                        Log.e("Get Data", "Hello I am in READ ALL DATA: ");

                        Log.e("Get id", "" + object.getId());
                        Log.e("Get title", "" + object.getTitle());
                        Log.e("Get city", "" + object.getCity());
                        Log.e("Get picture", "" + object.getPicture());
                        Log.e("Get time", "" + object.getTime());

                        eventList.add(object);

                        //Log.e("My Event ", "" + snapshot.getValue(AllEventsModel.class).toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
    public Boolean checkUser(final int position)
    {
        check = false;
        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.d("I am on start: ", ": " + userId);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users-Events");
        Query Query = myRef.orderByChild("eventId").equalTo(userId);

        // Read from the database
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String eventId = child.child("eventId").getValue().toString();
                    String myId = child.child("userId").getValue().toString();

                    if(userId.equals(myId) && eventList.get(position).getId().equals(eventId)) {
                        check = true;
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

