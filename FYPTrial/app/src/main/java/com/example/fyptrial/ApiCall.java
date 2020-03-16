package com.example.fyptrial;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCall extends AsyncTask<String, String, String> {
    Context context;

    public ApiCall(Context context) {
        //set context variables if required
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String data = params[0]; //data to post
        try {
            postData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void postData(String userId) throws Exception {
        String wurl = "http://192.168.18.60:8000/get_recommendations";

        //Change the URL with any other publicly accessible POST resource, which accepts JSON request body
        URL url = new URL(wurl);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);

        //JSON String need to be constructed for the specific resource.
        //We may construct complex JSON using any third-party JSON libraries such as jackson or org.json
        String jsonInputString = "{id:" + userId + "}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        System.out.println(code);
        Log.e("Agya", "" + code);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}
