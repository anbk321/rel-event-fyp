//package com.example.fyptrial;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//public class Categories extends AppCompatActivity {
//
//    ImageView entertainment, sports, tripsAndadventures, foodAndDrinks, workshops, business, setting_iv, search_iv;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_categories);
//
//        setting_iv = findViewById(R.id.settingsIcon);
//        search_iv = findViewById(R.id.settingsIcon);
//        entertainment = findViewById(R.id.entertainment_iv);
//        sports = findViewById(R.id.sport_iv);
//        tripsAndadventures = findViewById(R.id.adventure_iv);
//        foodAndDrinks = findViewById(R.id.food_iv);
//        business = findViewById(R.id.business_iv);
//        workshops = findViewById(R.id.workshop_iv);
//
//        entertainment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Categories.this, Entertainment.class);
//                startActivity(intent);
//            }
//        });
//
//        foodAndDrinks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Categories.this, FoodAndDrinks.class);
//                startActivity(intent);
//
//            }
//        });
//
//        sports.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Categories.this, SportEventsList.class);
//                startActivity(intent);
//
//            }
//        });
//
//        tripsAndadventures.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Categories.this, TripsAndAdventures.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//        business.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Categories.this, BusinessEventsList.class);
//                startActivity(intent);
//
//            }
//        });
//
//        workshops.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Categories.this, WorkshopEventsList.class);
//                startActivity(intent);
//
//            }
//        });
//    }
//}
