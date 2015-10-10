package com.example.zekun.mypersonalconcierge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class OptionsPresentation extends ActionBarActivity {

    Integer myBudgetInt;
    TextView label;
    //-----------------get from gps pls--------------------------
    String start_latitude="37.8668";
    String start_longitude="-122.2536";
    String server_id = "$SECRET";
    private class UBERLookup extends AsyncTask<String, Integer, String> {
        //coords = {end lat, end long}
        protected String doInBackground(String... coords) {
            String parsedString = "";

            try {
                String api_call = "https://sandbox-api.uber.com/v1/estimates/price?start_latitude=" + start_latitude + "&start_longitude=" + start_longitude + "&end_latitude=" + coords[0] + "&end_longitude=" + coords[1];
                URL url = new URL(api_call);
                URLConnection conn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.setRequestProperty("Authorization", "Token " + server_id);
                httpConn.connect();

                InputStream is = httpConn.getInputStream();
                parsedString = convertinputStreamToString(is);
                return parsedString;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "failed";
        }


        protected void onPostExecute(String result) {

            try {
                JSONObject mainObject = new JSONObject(result);
                JSONArray prices = mainObject.getJSONArray("prices");
                String estimates = "";
                JSONObject offer;
                //uncomment to get prices for every car option :D
                //for(int i=0; i<prices.length();i++){

                    offer = prices.getJSONObject(0);//i);
                    estimates += offer.getString("localized_display_name") + ": " + offer.getString("estimate") + "\n" + offer.getString("distance") + " miles" + "\n";
                //}

                label.setText(estimates);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    public static String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                        ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_presentation);

        Intent myGivenInfo = getIntent();

        myBudgetInt = Integer.parseInt(getIntent().getStringExtra("inputBudget"));
        label = (TextView) findViewById(R.id.textView);
        label.setText("Loading...");
        new UBERLookup().execute("37.8545738","-122.2918573");





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
