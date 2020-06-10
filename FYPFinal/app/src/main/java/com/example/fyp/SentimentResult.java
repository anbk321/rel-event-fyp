package com.example.fyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class SentimentResult extends AppCompatActivity {

    ImageView happyIv, sadIv;
    TextView happyTv, sadTv , text_tv;
    String category;
    int hrs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Random r = new Random();

        happyIv = findViewById(R.id.happy_iv);
        sadIv = findViewById(R.id.sad_iv);

        happyTv = findViewById(R.id.happy_tv);
        sadTv = findViewById(R.id.sad_tv);

        text_tv = findViewById(R.id.text_info);

        String text = "  Event's success rate is: ";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        category = preferences.getString("CATEGORY", "");
        hrs = preferences.getInt("HOURS", 0);

        String citySentiment = preferences.getString("userCity", "");

        citySentiment = citySentiment.toLowerCase();
        Log.e("City for senti: ", citySentiment);
        Log.e("CATEGORY:", category);
        Log.e("Hours in numbers:", String.valueOf(hrs));

        int analysis = 0;

        if(citySentiment.equals("karachi") || citySentiment.equals("lahore") || citySentiment.equals("rawalpindi") || citySentiment.equals("islamabad") || citySentiment.equals("multan") || citySentiment.equals("faisalabad"))
        {
            if(category.equals("ENTERTAINMENT") && hrs >= 18)
            {
                analysis = r.nextInt(57 - 51) + 51;
            }
            else if(category.equals("ENTERTAINMENT") && hrs < 18)
            {
                analysis = r.nextInt(61 - 57) + 57;
            }
            else if(category.equals("SPORTS") && hrs >= 18)
            {
                analysis = r.nextInt(37 - 34) + 34;
            }
            else if(category.equals("SPORTS") && hrs < 18)
            {
                analysis = r.nextInt(41 - 36) + 36;
            }
            else if(category.equals("FOOD-DRINKS")  && hrs >= 18)
            {
                analysis = r.nextInt(46 - 39) + 39;
            }
            else if(category.equals("FOOD-DRINKS")  && hrs < 18)
            {
                analysis = r.nextInt( 51 - 47) + 47;
            }
            else if(category.equals("TRIPS-ADVENTURES")  && hrs >= 18)
            {
                analysis = r.nextInt(31 - 27) + 27;
                Log.e("I am Value: ", String.valueOf(analysis));
            }
            else if(category.equals("TRIPS-ADVENTURES")  && hrs < 18)
            {
                analysis = r.nextInt(27 - 23) + 23;
            }
            else if(category.equals("BUSINESS")  && hrs >= 18)
            {
                analysis = r.nextInt(69 - 61) + 61;
            }
            else if(category.equals("BUSINESS")  && hrs < 18)
            {
                analysis = r.nextInt(81 - 73) + 73;
            }
            else if(category.equals("WORKSHOPS")  && hrs >= 18)
            {
                analysis = r.nextInt(73 - 67) + 67;
            }
            else if(category.equals("WORKSHOPS")  && hrs < 18)
            {
                analysis = r.nextInt( 84 - 76) + 76;
            }
        }
        else if(citySentiment.equals("skardu") || citySentiment.equals("gilgit") || citySentiment.equals("murree") || citySentiment.equals("peshawar"))
        {
            if(category.equals("ENTERTAINMENT") && hrs >= 18)
            {
                analysis = r.nextInt(57 - 51) + 51;
            }
            else if(category.equals("ENTERTAINMENT") && hrs < 18)
            {
                analysis = r.nextInt(61 - 57) + 57;
            }
            else if(category.equals("SPORTS") && hrs >= 18)
            {
                analysis = r.nextInt(56 - 51) + 51;
            }
            else if(category.equals("SPORTS") && hrs < 18)
            {
                analysis = r.nextInt(64 - 57) + 57;
            }
            else if(category.equals("FOOD-DRINKS")  && hrs >= 18)
            {
                analysis = r.nextInt(46 - 39) + 39;
            }
            else if(category.equals("FOOD-DRINKS")  && hrs < 18)
            {
                analysis = r.nextInt( 51 - 47) + 47;
            }
            else if(category.equals("TRIPS-ADVENTURES")  && hrs >= 18)
            {
                analysis = r.nextInt(71 - 67) + 67;
                Log.e("I am Value: ", String.valueOf(analysis));
            }
            else if(category.equals("TRIPS-ADVENTURES")  && hrs < 18)
            {
                analysis = r.nextInt(81 - 74) + 74;
            }
            else if(category.equals("BUSINESS")  && hrs >= 18)
            {
                analysis = r.nextInt(41 - 37) + 61;
            }
            else if(category.equals("BUSINESS")  && hrs < 18)
            {
                analysis = r.nextInt(49 - 43) + 43;
            }
            else if(category.equals("WORKSHOPS")  && hrs >= 18)
            {
                analysis = r.nextInt(37 - 31) + 31;
            }
            else if(category.equals("WORKSHOPS")  && hrs < 18)
            {
                analysis = r.nextInt( 51 - 43) + 43;
            }
        }


//        else
//            analysis = r.nextInt(90 - 29) + 29;

        int sadAnalysis = 100-analysis;
        Log.e("I am Value after sad: ", String.valueOf(analysis));
        Log.e("I am Value of sad: ", String.valueOf(sadAnalysis));

        if(analysis > 50)
        {
            happyTv.setText(Integer.toString(analysis)+"%");
            sadTv.setText(Integer.toString(sadAnalysis)+"%");
            text_tv.setText(text+analysis+"%");

        }
        else
        {
            happyTv.setText(Integer.toString(analysis)+"%");
            sadTv.setText(Integer.toString(sadAnalysis)+"%");
            text_tv.setText(text+sadAnalysis+"%");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}