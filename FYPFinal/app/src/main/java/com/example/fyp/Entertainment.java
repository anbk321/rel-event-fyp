package com.example.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class Entertainment extends AppCompatActivity implements MyAdapter.ClickListener, View.OnClickListener {


    int rateCount = 0;
    private ProgressBar mProgressBar;

    Boolean check = false;
    EditText et_search;
    private RecyclerView rv;
    //ReadXlFile file;
    private List<AllEventsModel> eventList;
    //private List<EventsModel> eventList;
    private List<UserEventsModel> userEventsList;
    private MyAdapter adapter;
    private MyAdapter adapterB;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseDatabase databaseA = FirebaseDatabase.getInstance();
    final FirebaseDatabase databaseB = FirebaseDatabase.getInstance();
    DatabaseReference eventRef;
    DatabaseReference myRef;
    String email;
    String city;
    RatingBar likeButton;
    int categoryValue;
    int count;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_recycler_view);

        likeButton = findViewById(R.id.like_bt);
        rv = findViewById(R.id.recycler_view);
        //et_search = findViewById(R.id.et_search);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        city = preferences.getString("userCity", "");
        count = preferences.getInt("val", 1);


        Log.e("Count", String.valueOf(count));
        Log.e("City from shared",city);


        count++;
        Log.e("Count INNNNNNNN read", String.valueOf(count));

        final Intent eventsIntent = getIntent();
        categoryValue = eventsIntent.getIntExtra("gridIndexValue", 0);

        eventList = new ArrayList<>();

        readAllData();



        //adapter = new MyAdapter((ArrayList<AllEventsModel>) eventList);

        adapter = new MyAdapter(eventList, this, new MyAdapter.MyAdapterListener() {

            @Override
            public void iconRatingBarOnClick(final int position, int rating) {

//                adapter = new MyAdapter(getApplicationContext(), (ArrayList<AllEventsModel>) eventList);
                String titleTemp = eventList.get(position).getTitle();

                if (checkUser(position) == false) {
                    addUserEvent(position, rating);
//                    Collections.shuffle(eventList);
                }

//                if(rating >= 3)
//                {
//                    int iter = 0;
//
//                    int i = 0;
//                    Log.e("eventsList SIZEEE: ", String.valueOf(eventList.size()));
//                    for(int j = 0; j < eventList.size(); j++)
//                    {
//                        String words[] = eventList.get(j).getTitle().split(" ", countWords(eventList.get(j).getTitle()));
//                        Log.e("TITLE ASLIII : ", eventList.get(j).getTitle());
//                        Log.e("WORDS COUNT: ", String.valueOf(eventList.size()));
//
//                        for(int k = 0; k < countWords(eventList.get(j).getTitle()); k++)
//                        {
//                            Pattern p = Pattern.compile(words[k]);
//                            Matcher m = p.matcher( titleTemp );
//                            while (m.find())
//                            {
//                                i++;
//                            }
//                            if(i > 2)
//                            {
//                                Collections.swap(eventList, j, 0);
////                                Collections.swap(eventList, j+1, 1);
//                            }
//                        }
//                    }
//                }

                //addUsersRatedEvents(position, rating);
//                AlertDialog diaBox = AskOption(position);
//                diaBox.show();

            }
        });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(Entertainment.this);
        rv.setLayoutManager(manager);

        rv.setAdapter(adapter);

//        et_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                String arr[] = new String[3];

//                myList.setAdapter(adapter);
//                adapter.getFilter().filter(et_search.getText().toString());
//                int iter = 0;
//                if(et_search.getText().toString().length() != 0)
//                {
//                    for(int i = 0; i < eventList.size(); i++)
//                    {
//                        String temp = list.get(i);
//                        Character ch1 = Character.toUpperCase(et_city.getText().toString().charAt(0));
//                        Character ch2 = Character.toUpperCase(temp.charAt(0));
//
//
//                        if(et_city.getText().toString().length() != 0 && ch1 == ch2)
//                        {
//
//                            Collections.swap(list, i, iter);
//                            iter++;
//
//                        }
//                    }
//                }
//            }

//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

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

        final Date[] d = new Date[1];
        final Date todayDate = new Date();
        //Log.e("AAAAAJJ KI Date: ", todayDate.toString());
        myRef = database.getReference("All-Events");
        if (eventList.isEmpty()) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshotA) {
                    for (DataSnapshot postSnapshotA : snapshotA.getChildren()) {
                        AllEventsModel object = new AllEventsModel();
                        object = postSnapshotA.getValue(AllEventsModel.class);
                        object.setTime(object.getTime().trim());
                        String temp = object.getTime();
                        String arr[] = temp.split(" ", 2);
                        String word = arr[0];
                        Collections.shuffle(eventList);

                        if(word.equals("Jan") || word.equals("Feb") || word.equals("Mar") || word.equals("Apr") || word.equals("Jun") || word.equals("Jul") || word.equals("Sep") || word.equals("Oct") || word.equals("Nov") || word.equals("Dec"))
                        {
                            String temp1 = object.getTime();
                            String arr1[] = temp.split("-", 2);
                            String time = arr1[0];
                            //Log.e("TIMEEEEEEE STRING: ", time);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
                            Date date;
                            try {
                                date = dateFormat.parse(time);
                                d[0] = date;
//                                Log.e("Month Date: ", date.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(word.equals("Mon") || word.equals("Tue") || word.equals("Wed") || word.equals("Thu") || word.equals("Fri") || word.equals("Sat") || word.equals("Sun"))
                        {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, YYYY");
                            Date date;
                            try {
                                date = dateFormat.parse(temp);
                                d[0] = date;
//                                Log.e("Day Date: ", date.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("CATEGORYYYYY STRING: ", object.getType());
                        object.setDescription(object.getDescription().trim());
                        object.setVenue(object.getVenue().trim());
                        if(object.getCity().toLowerCase().equals(city.toLowerCase())) {
                            if (categoryValue == 0 && object.getType().equals("sports") && object.getRating() == -1 && d[0].compareTo(todayDate) < 0 && !object.getTitle().equals("none")) {
                                eventList.add(object);
                            } else if (categoryValue == 1 && object.getType().equals("entertainment") && object.getRating() == -1 && d[0].compareTo(todayDate) < 0 && !object.getTitle().equals("none")) {
                                eventList.add(object);
                            } else if (categoryValue == 2 && object.getType().equals("trips-adventures") && object.getRating() == -1 && d[0].compareTo(todayDate) < 0 && !object.getTitle().equals("none")) {
                                eventList.add(object);
                            } else if (categoryValue == 3 && object.getType().equals("food-drinks") && object.getRating() == -1 && d[0].compareTo(todayDate) < 0 && !object.getTitle().equals("none")) {
                                eventList.add(object);
                            } else if (categoryValue == 4 && object.getType().equals("business") && object.getRating() == -1 && d[0].compareTo(todayDate) < 0 && !object.getTitle().equals("none")) {
                                eventList.add(object);
                            } else if (categoryValue == 5 && object.getType().equals("workshops") && object.getRating() == -1 && d[0].compareTo(todayDate) < 0 && !object.getTitle().equals("none")) {
                                Log.e("CATEGORYYYYY WORKSHOP: ", object.getType());
                                eventList.add(object);
                            }
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
    }

    public void addUserEvent(final int position, final int rating) {

        String eventId = eventList.get(position).getId();
        String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        eventRef = databaseA.getReference("Users-Events").push();
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
                    String eId = eventList.get(position).getId();
                    Log.e("id", eId);
                    Toast.makeText(Entertainment.this, "Event added to your Favourites", Toast.LENGTH_LONG).show();
                    eventList.remove(position);

                    for(int i = 0; i < eventList.size(); i++)
                    {
                        for(int j = i+1; j < eventList.size(); j++)
                        {
                            if(eventList.get(i).getTitle().equals(eventList.get(j).getTitle()))
                            {
                                eventList.remove(j);
                            }
                        }
//                        Collections.reverse(eventList);
//
                    }
                    adapter.eventList.clear();

                    Log.e("Length before", String.valueOf(eventList.size()));

//                    adapter.eventList = eventList; // update the list in the adapter
//                    adapter.notifyDataSetChanged(); // refresh

                    //Collections.shuffle(eventList);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(Entertainment.this);
                    rv.setLayoutManager(manager);
                    rv.setAdapter(adapter);
                    Log.e("Length after", String.valueOf(eventList.size()));
                }
                else {
                    Toast.makeText(Entertainment.this, "Event failed to add to your Favourites", Toast.LENGTH_LONG).show();
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

        DatabaseReference myRefb = FirebaseDatabase.getInstance().getReference().child("Users-Events");
        Query Query = myRefb.orderByChild("userId").equalTo(userId);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.events_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //readAllData();
                adapter.getFilter().filter(newText);
                Log.e("In OnQueryTextChange", "HEYYYYYYYYYYY");
                return false;
            }
        });
        return true;
    }

    public static int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    @Override
    public void onClick(View v) {

    }
}
