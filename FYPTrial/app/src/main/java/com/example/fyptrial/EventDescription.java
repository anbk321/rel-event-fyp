package com.example.fyptrial;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class EventDescription extends AppCompatActivity {

    AllEventsModel model;
    ImageView picture;
    String date = "";
    TextView title_tv, time_tv, city_tv, venue_tv, category_tv, description_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        title_tv = findViewById(R.id.name);
        time_tv = findViewById(R.id.time);
        city_tv = findViewById(R.id.city);
        venue_tv = findViewById(R.id.venue);
        category_tv = findViewById(R.id.category_tv);
        description_tv = findViewById(R.id.description);

        picture = findViewById(R.id.img_iv);

        model = new AllEventsModel();

        // To retrieve object in second Activity
        model = (AllEventsModel) getIntent().getSerializableExtra("MyClass");

//        String[] parts = model.getTime().split("to");
//        String part1 = parts[0]; // 004
//        String part2 = parts[1]; // 034556
//
//        date = part1 +"\n" + "to" + "\n" + part2;


        title_tv.setText(model.getTitle());
        time_tv.setText(model.getTime());
        city_tv.setText(model.getCity());
        venue_tv.setText(model.getVenue());
        category_tv.setText(model.getType());
        description_tv.setText(model.getDescription());
        if(!model.getPicture().isEmpty())
            Picasso.get().load(model.getPicture()).into(picture);

    }

}
