package com.example.zekun.mypersonalconcierge;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;




public class OptionsPresentation extends ActionBarActivity  {

    Integer myBudgetInt;
    public static ArrayList<myUberClass> mySuperList;


    static TextView label;
    String server_id = "7UB1w9eL1gz6_228W70N02oQFlU89HX5IR2Kjj5f";
    String start_latitude = "37.8668";
    String start_longitude = "-122.2536";
    Destination destinations[] = {new Destination("UC Berkeley", "The number one public university in the world. Go Bears!","37.8545738", "-122.2918573", R.drawable.berkeleycampus),
            new Destination("Pappy's Sports Bar","Great place to grab a casual drink with student discounts","37.9045738", "-122.2818573", R.drawable.pappysimage),
      
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_presentation);

        myBudgetInt = Integer.parseInt(getIntent().getStringExtra("inputBudget"));


        //START THE GOOGLE LOCATION SERVICES

        


        //PARSE THE USER SUBMITTED INFO FROM THE INTENT
       // Intent myGivenInfo = getIntent();

        myBudgetInt = Integer.parseInt(getIntent().getStringExtra("inputBudget"));


        label = (TextView) findViewById(R.id.textView);


        new UBERLookup().execute(destinations);

    }


    private class UBERLookup extends AsyncTask<Destination, Integer, ArrayList<Destination>> {
        //coords = {end lat, end long}
        protected ArrayList<Destination> doInBackground(Destination... dest) {
            ArrayList<Destination> finalDest = new ArrayList<>();
            for(Destination destination : dest){


                try {
                    String api_call = "https://sandbox-api.uber.com/v1/estimates/price?start_latitude=" + start_latitude + "&start_longitude=" + start_longitude + "&end_latitude=" + destination.getLat() + "&end_longitude=" + destination.getLon();
                    URL url = new URL(api_call);
                    URLConnection conn = url.openConnection();
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setAllowUserInteraction(false);
                    httpConn.setInstanceFollowRedirects(true);
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty("Authorization", "Token " + server_id);
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
            ArrayList<Destination> pass = new ArrayList<>();
            for(Destination dest : result){

                int price = Integer.parseInt(dest.getPrice().substring(dest.getPrice().indexOf('-') + 1));
                if(price <= myBudgetInt){
                    pass.add(dest);

                }

            }
           // displayOptions(pass);
            translateAndFeed(pass);
        }
    }

    public void translateAndFeed(ArrayList<Destination> pass){
        ArrayList<myUberClass> zekun_why = new ArrayList<>();
        int count = 0;
        for(Destination des : pass){

            //myUberClass(String inputDesc, int inputCost, String inputName, int inputID)
            zekun_why.add(new myUberClass(des.getDescription(), des.getPrice(), des.getName(), count, des.getDrawableID() ));
            count++;
        }
        if(zekun_why.size()==0){

            label.setText("No locations within your budget were found :(");
            return;
        }
        mySuperList=zekun_why;
        initializeList(this, zekun_why);
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


    public void initializeList(Context myContext, ArrayList<myUberClass> myList){

        ViewGroup parent = (ViewGroup) findViewById(R.id.myScrollLinear);
        parent.removeAllViews();



        /*Now that we have the ArrayList stored in the file, we iterate through it to grab each
        individual music entry to display in the ScrollView
         */
        label.setText("Found " + myList.size() + " matches!");
        for (int e=0;e<myList.size();e++) {

            final myUberClass myUberObj = myList.get(e);

            String myDestName = myUberObj.name;
            String myDestDesc = myUberObj.description;
            String myDestCost= myUberObj.cost;
            final int myId = myUberObj.id;
            Integer myDrawable = myUberObj.myDrawableID;

            /*Now we take care of setting it up visually

             */

            parent = (ViewGroup) findViewById(R.id.myScrollLinear);

            View view = LayoutInflater.from(myContext).inflate(R.layout.my_options_layout, null);

            ImageView layout =(ImageView)view.findViewById(R.id.myImageView);
            layout.setBackgroundResource(myDrawable);

            final TextView myTitleText = (TextView) view.findViewById(R.id.titleTextView);
            myTitleText.setText(myDestName);

            final TextView myTextview = (TextView) view.findViewById(R.id.displayWindow);
            myTextview.setText(myDestDesc + "\n" + "Cost for Uber (Each Way): " + myDestCost);

            final TextView myIdHolder = (TextView) view.findViewById(R.id.idHolder);
            myIdHolder.setText(Integer.toString(myId));



            final Button deleteButton = (Button) view.findViewById(R.id.button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mySuperList != null) {
                        for (int r = 0; r < mySuperList.size(); r++) {
                            myUberClass myCurrObj = (mySuperList.get(r));
                            if (myCurrObj.id == Integer.parseInt((String) myIdHolder.getText())) {
                                myUberClass myChosenDestObj = idObjLookup(myId);
                                label.setText("Calling an uber to take you to " + myChosenDestObj.name);
                            }
                        }



                    }

                }

            });


            parent.addView(view);
        }
    }


    private myUberClass idObjLookup(int id){
        myUberClass myCurrObj1=null;
        for (int i = 0; i<mySuperList.size();i++){
            myCurrObj1 =  mySuperList.get(i);
            if (myCurrObj1.id == id){
                return myCurrObj1;
            }
        }

        return myCurrObj1;
    }

}
