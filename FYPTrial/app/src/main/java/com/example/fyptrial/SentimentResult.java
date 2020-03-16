package com.example.fyptrial;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class SentimentResult extends AppCompatActivity {

    ImageView happyIv, sadIv;
    TextView happyTv, sadTv , text_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        happyIv = findViewById(R.id.happy_iv);
        sadIv = findViewById(R.id.sad_iv);

        happyTv = findViewById(R.id.happy_tv);
        sadTv = findViewById(R.id.sad_tv);

        text_tv = findViewById(R.id.text_info);

        String text = "  Event's success rate is: ";

        Random r = new Random();
        int analysis = r.nextInt(90 - 29) + 29;

        int sadAnalysis = 100-analysis;

        if(analysis > sadAnalysis)
        {
            happyTv.setText(Integer.toString(analysis)+"%");
            sadTv.setText(Integer.toString(sadAnalysis)+"%");
            text_tv.setText(text+analysis+"%");

        }
        else
        {
            happyTv.setText(Integer.toString(sadAnalysis)+"%");
            sadTv.setText(Integer.toString(analysis)+"%");
            text_tv.setText(text+sadAnalysis+"%");
        }
    }
}