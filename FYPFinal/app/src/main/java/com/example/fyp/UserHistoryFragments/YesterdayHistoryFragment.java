package com.example.fyp.UserHistoryFragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp.AllEventsModel;
import com.example.fyp.EventDescription;
import com.example.fyp.MyAdapter;
import com.example.fyp.R;
import com.example.fyp.ReadXlFile;
import com.example.fyp.UserEventsModel;
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
import java.util.Collections;
import java.util.List;

public class YesterdayHistoryFragment extends Fragment implements MyAdapter.ClickListener
{

    Boolean check = false;
    private RecyclerView rv;
    ReadXlFile file;
    private List<AllEventsModel> eventList;

    List<AllEventsModel> filterArr;

    private List<UserEventsModel> userEventsList;
    private MyAdapter adapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventRef;
    DatabaseReference myRef;
    String email;
    RatingBar likeButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yesterday_history_fragment, container, false);

        file = new ReadXlFile();

        likeButton = view.findViewById(R.id.like_bt);

        eventList = new ArrayList<>();

        userEventsList = new ArrayList<>();
        filterArr = new ArrayList<>();
        rv = view.findViewById(R.id.recycler_view);

        readAllData();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);


        return view;
    }

    public void readAllData() {
        /**
         * get user Events data
         */
        final String userId = Settings.Secure.getString(getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        myRef = database.getReference("Users-Events");
        if(userEventsList.isEmpty()) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        UserEventsModel object;
                        object = postSnapshot.getValue(UserEventsModel.class);
                        if (object != null) {
                            if(userId.equals(object.getUserId())){
                                userEventsList.add(object);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        /**
         * get All events data filtered on user Event Id
         */
        eventRef = database.getReference("All-Events");
        if(eventList.isEmpty()) {
            eventRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        AllEventsModel object;
                        object = postSnapshot.getValue(AllEventsModel.class);
                        eventList.add(object);
                    }
                    filterData();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        adapter = new MyAdapter(filterArr, this, new MyAdapter.MyAdapterListener() {
            @Override
            public void iconRatingBarOnClick( final int position, int rating) {
                updateUserEvent(position, rating);
                return;
            }
        });
    }

    @Override
    public void onTouch(int position) {
        Intent intent=new Intent(getActivity(), EventDescription.class);
        intent.putExtra("MyClass", (Serializable) filterArr.get(position));
        startActivity(intent);

    }

    @Override
    public void onLongTouch(int position) {

    }

    public void filterData() {
        for (UserEventsModel obj: userEventsList) {
            for (AllEventsModel object: eventList) {
                if(object.getId().equals(obj.getEventId())){
                    AllEventsModel updatedObj = new AllEventsModel(
                            object.getId(), object.getTitle(), object.getTime(),object.getVenue().trim(),object.getDescription().trim(),object.getSource(),
                            object.getPicture(),object.getType(),object.getCity(),object.getPastFuture()
                            ,obj.getRating());

                    Log.e("my fav events", "=====>>>>>>>>"+ updatedObj.getTitle());
                    Log.e("my fav events", "=====>>>>>>>>"+ updatedObj.getCity());
                    Log.e("my fav events", "=====>>>>>>>>"+ updatedObj.getType());
                    filterArr.add(updatedObj);
                }
            }
        }
        Collections.sort(filterArr);
        adapter.eventList = filterArr; // update the list in the adapter
        adapter.notifyDataSetChanged(); // refresh
    }

    public void updateUserEvent(final int position, final int rating)
    {
        final String eID = filterArr.get(position).getId();
        final String userId = Settings.Secure.getString(getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
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
                                    //Toast.makeText(UserHistory.this, "Suggestion Rating Updated Successfully", Toast.LENGTH_LONG).show();
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
}
