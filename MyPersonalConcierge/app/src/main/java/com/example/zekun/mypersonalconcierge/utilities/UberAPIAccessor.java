package com.example.zekun.mypersonalconcierge.utilities;

import android.os.AsyncTask;

import com.example.zekun.mypersonalconcierge.models.Coordinates;
import com.example.zekun.mypersonalconcierge.models.Destination;
import com.example.zekun.mypersonalconcierge.models.UberPriceEstimate;
import com.example.zekun.mypersonalconcierge.models.UberPriceEstimateQuery;

import com.example.zekun.mypersonalconcierge.utilities.DestinationDatabaseAccessor;

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
import java.util.List;
import java.util.Set;

public class UberAPIAccessor {
    String server_id = "$SECRET";
    public static List<UberPriceEstimate> estimatePrice(UberPriceEstimateQuery query) {
        // TODO: implement this
        return null;
    }
    private class UBERLookup extends AsyncTask<Destination, Integer, String> {
        //coords = {end lat, end long}
        protected String doInBackground(Destination... dest) {
            for(Destination desstination : dest){




            }
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

                //THIS IS WHERE WE HANDLE THE POST-PROCESS DATA
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
    public static List<UberPriceEstimate> queryAllDestinations(Coordinates startCoordinates,
            Set<String> acceptableUberProductNames) {
        // TODO: this will be really inefficient if there are tons of destinations

        List<UberPriceEstimate> allPriceEstimates = new ArrayList<UberPriceEstimate>();

        List<Destination> allDestinations = DestinationDatabaseAccessor.getAllDestinations();

        for (Destination possibleDestination : allDestinations) {
            // Create a query for the current possible destination
            UberPriceEstimateQuery query = new UberPriceEstimateQuery(startCoordinates,
                    acceptableUberProductNames, possibleDestination);

            List<UberPriceEstimate> estimatesForCurrentDestination = estimatePrice(query);
            allPriceEstimates.addAll(estimatesForCurrentDestination);
        }

        return allPriceEstimates;
    }
}