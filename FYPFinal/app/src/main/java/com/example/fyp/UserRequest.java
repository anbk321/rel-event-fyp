package com.example.fyp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.UserHistoryFragments.UserHistoryController;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserRequest extends AppCompatActivity {

    GridLayout mainGrid;

    TextView tv_name, tv_city;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    ImageView entertainment, sports, tripsAndadventures, foodAndDrinks, workshops, business, setting_iv, search_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.user_request);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        navigationView = (NavigationView) findViewById(R.id.nav);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);


        tv_name = navigationView.getHeaderView(0).findViewById(R.id.name);
        tv_city = navigationView.getHeaderView(0).findViewById(R.id.city);
        checkUser();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                // Handle item selection
                switch (item.getItemId()) {
                    case R.id.analyzeEvent:
                        startActivity(new Intent(UserRequest.this, EventDetails.class));
                        return true;
                    case R.id.delete:
                        AlertDialog diaBox = AskOption();
                        diaBox.show();
                        return true;
                    case R.id.history:

                        startActivity(new Intent(UserRequest.this, UserHistory.class));
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(UserRequest.this, UserUpdateSettings.class));
                        return true;
                    case R.id.home:
                        startActivity(new Intent(UserRequest.this, UserRequest.class));
                        return true;
                    default:
                        return false;
                }
            }
        });

        /**
         * Set Action Listener to open and close the Navigation Drawer
         */
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(UserRequest.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(UserRequest.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserRequest.this,Entertainment.class);
                    intent.putExtra("gridIndexValue", finalI);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private AlertDialog AskOption()
    {
        final String userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete your Profile?")
                .setIcon(R.drawable.ic_delete_24px)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        Query userDeleteQuery = ref.orderByChild("userID").equalTo(userId);

                        userDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userDeleteSnapshot: dataSnapshot.getChildren()) {
                                    userDeleteSnapshot.getRef().removeValue();
                                    Toast.makeText(UserRequest.this, "User Deleted Successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(UserRequest.this, RegisterActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
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

    public void checkUser()
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
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserRequest.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userCity", userCity);
                    editor.putInt("val", 1);
                    editor.apply();

                    tv_name.setText(name);
                    tv_city.setText(userCity);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        return;
    }
}
