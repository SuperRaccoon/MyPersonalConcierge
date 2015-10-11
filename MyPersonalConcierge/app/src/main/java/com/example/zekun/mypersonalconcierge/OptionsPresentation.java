package com.example.zekun.mypersonalconcierge;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

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
import java.util.ArrayList;




public class OptionsPresentation extends ActionBarActivity  {

    Integer myBudgetInt;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    private Location myLocation;

    private String myLocationString;





    //MARCO'S STUFF
    TextView label;
    String server_id = "7UB1w9eL1gz6_228W70N02oQFlU89HX5IR2Kjj5f";
    String start_latitude = "37.8668";
    String start_longitude = "-122.2536";
    Destination destinations[] = {new Destination("test_name", "test_desc","37.8545738", "-122.2918573" , ""),
            new Destination("test_name2",
                    "test_desc2","37.9045738", "-122.2818573", "" ),
            new Destination("test_name3",
                    "test_desc2","37.8045738", "-122.3018573","" ),
            new Destination("test_name4",
                    "test_desc2","37.9145738", "-122.7818573","" )};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_presentation);


        //START THE GOOGLE LOCATION SERVICES




        //PARSE THE USER SUBMITTED INFO FROM THE INTENT
        Intent myGivenInfo = getIntent();

        myBudgetInt = Integer.parseInt(getIntent().getStringExtra("inputBudget"));


        TextView label = (TextView) findViewById(R.id.titleTextView);
        label.setVisibility(View.VISIBLE);
        label.setText("Loading...");




        label.setText("This is where you can go with $" +myBudgetInt.toString() +". You are located at ");




        //MY SAMPLES FOR TESTING
        myUberClass myUberObj1 = new myUberClass();
        myUberObj1.setDescCostName("Big C is a cool place to be you plebs",180, "Big C",1234);

        myUberClass myUberObj2 = new myUberClass();
        myUberObj2.setDescCostName("This place is not furd",20, "UC Berkeley",0000);


        ArrayList myArray = new ArrayList();

        myArray.add(myUberObj1);
        myArray.add(myUberObj2);


       //MAKE THE DISPLAY LIST
        initializeList(this, myArray);


    }



    public void initializeList(Context myContext, ArrayList<myUberClass> myList){

        ViewGroup parent = (ViewGroup) findViewById(R.id.myScrollLinear);
        parent.removeAllViews();






        /*Now that we have the ArrayList stored in the file, we iterate through it to grab each
        individual music entry to display in the ScrollView
         */

        for (int e=0;e<myList.size();e++) {

            myUberClass myUberObj = myList.get(e);

            String myDestName = myUberObj.name;
            String myDestDesc = myUberObj.description;
            int myDestCost= myUberObj.cost;
            int myId = myUberObj.id;

            /*Now we take care of setting it up visually

             */

            parent = (ViewGroup) findViewById(R.id.myScrollLinear);

            View view = LayoutInflater.from(myContext).inflate(R.layout.my_options_layout, null);

            final TextView myTitleText = (TextView) view.findViewById(R.id.titleTextView);
            myTitleText.setText(myDestName);

            final TextView myTextview = (TextView) view.findViewById(R.id.displayWindow);
            myTextview.setText(myDestDesc + "\n" + "Cost: " + myDestCost);


            final Button deleteButton = (Button) view.findViewById(R.id.button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }

            });


            parent.addView(view);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_presentation, menu);


        new UBERLookup().execute(destinations);

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
    private class UBERLookup extends AsyncTask<Destination, Integer,
                ArrayList<Destination>> {
        //coords = {end lat, end long}
        protected ArrayList<Destination> doInBackground(Destination... dest) {
            ArrayList<Destination> finalDest = new ArrayList<Destination>();
            for(Destination destination : dest){


                try {
                    String api_call =
                            "https://sandbox-api.uber.com/v1/estimates/price?start_latitude=" +
                                    start_latitude + "&start_longitude=" + start_longitude +
                                    "&end_latitude=" + destination.getLat() + "&end_longitude=" +
                                    destination.getLon();
                    URL url = new URL(api_call);
                    URLConnection conn = url.openConnection();
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setAllowUserInteraction(false);
                    httpConn.setInstanceFollowRedirects(true);
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty("Authorization",
                            "Token " + server_id);
                    httpConn.connect();
                    InputStream is = httpConn.getInputStream();
                    String parsedString = convertinputStreamToString(is);


                    try {
                        JSONObject mainObject = new JSONObject(parsedString);
                        JSONArray prices = mainObject.getJSONArray("prices");
                        JSONObject offer = prices.getJSONObject(0);

                        destination.setPrice(offer.getString("estimate"));

                        finalDest.add(destination);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return finalDest;
        }


        protected void onPostExecute(ArrayList<Destination> result) {
            ArrayList<Destination> pass = new ArrayList<Destination>();
            for(Destination dest : result){

                int price =
                        Integer.parseInt(dest.getPrice().substring(dest.getPrice().indexOf('-')
                                + 1));
                if(price <= myBudgetInt){
                    pass.add(dest);

                }

            }
            displayOptions(pass);

        }
    }

    public void displayOptions(ArrayList<Destination> results){
        label.setVisibility(View.VISIBLE);
        String res = "";
        TextView[] buttons =  {((TextView)
                findViewById(R.id.button1)),((TextView)
                findViewById(R.id.button2)),((TextView)
                findViewById(R.id.button3)),((TextView)
                findViewById(R.id.button4)),((TextView) findViewById(R.id.button5))} ;
        int count = 1;
        if(results.size() == 0){

            label.setText("No locations are currently within your budget!");
            label.setVisibility(View.VISIBLE);
            return;
        }
        for(Destination des : results){

            //res+=des.toString() + '\n';
            buttons[count].setText(des.toString());
            buttons[count].setVisibility(View.VISIBLE);
            count++;

        }
        label.setText(res);
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


}
