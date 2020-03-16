package com.example.fyptrial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadXlFile extends AppCompatActivity {

    public List<EventsModel> eventList = new ArrayList<>();
    public List<AllEventsModel> allEventsList = new ArrayList<>();

    public List<EventsModel> readEventData(Context context, String category) {

//        try {
//            InputStream read = context.getResources().openRawResource(R.raw.activities);
//            Workbook wb = Workbook.getWorkbook(read);
//            Sheet sh = wb.getSheet(0);
//            int rows = sh.getRows();
//
//            for (int i = 0; i < rows; i++)
//            {
//                if(i != 0)
//                {
//                    Cell[] cell = sh.getRow(i);
//
//                    //Read the data
//                    EventsModel model = new EventsModel();
//                    model.setTitle(cell[0].getContents());
//                    model.setTime(cell[1].getContents());
//                    model.setVenue(cell[2].getContents());
//                    model.setDescription(cell[3].getContents());
//                    model.setSource(cell[4].getContents());
//                    model.setPicture(cell[5].getContents());
//                    model.setType(cell[6].getContents());
//                    model.setCity(cell[7].getContents());
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//                    String city = prefs.getString("MYCITY", "defaultStringIfNothingFound");
//
//                    city = city.toUpperCase();
//                    String compareCity = cell[7].getContents().toUpperCase();
//                    category = category.toUpperCase();
//                    String compareCategory = cell[6].getContents().toUpperCase();
//
//                    Log.d("My City", "Local City: " + city);
//                    Log.d("My City", "City from file: " + compareCity);
//
//                    Log.d("My Category", "Local Category: " + category);
//                    Log.d("My Category", "Category from file: " + compareCategory);
//
//                    if(city.equals(compareCity) && category.equals(compareCategory))
//                    {
//                        eventList.add(model);
//                    }
//                }
//            }
//
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return eventList;

    }

    public List<AllEventsModel> readAllEventsData(Context context) {

//        try {
//            InputStream read = context.getResources().openRawResource(R.raw.activities);
//            Workbook wb = Workbook.getWorkbook(read);
//            Sheet sh = wb.getSheet(0);
//            int rows = sh.getRows();
//
////            Date date = Calendar.getInstance().getTime();
////            int currentDay = 0;
////            int currentMonth = 0;
////
////            SimpleDateFormat mf = new SimpleDateFormat("MM");
////            String monthString = mf.format(date);
////            SimpleDateFormat df = new SimpleDateFormat("dd");
////            String dayString = df.format(date);
////            currentMonth = Integer.parseInt(monthString);
////            currentDay = Integer.parseInt(dayString);
//
//            for (int i = 0; i < rows; i++)
//            {
//                if(i != 0)
//                {
//                    Cell[] cell = sh.getRow(i);
//
//                    //Read the data
//                    AllEventsModel model = new AllEventsModel();
//                    model.setTitle(cell[0].getContents());
//                    model.setTime(cell[1].getContents());
//                    model.setVenue(cell[2].getContents());
//                    model.setDescription(cell[3].getContents());
//                    model.setSource(cell[4].getContents());
//                    model.setPicture(cell[5].getContents());
//                    model.setType(cell[6].getContents());
//                    model.setCity(cell[7].getContents());
//                    model.setPastFuture(1);
//                    model.setRated(cell[8].getContents());
//                    allEventsList.add(model);
//                }
//            }
//
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//       }
//
        return allEventsList;


    }

}

