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
import java.lang.StringBuffer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UberAPIAccessor {
    public static final String SERVER_ID = "$SECRET";

    public static final String API_CALL_PROTOCOL = "https";
    public static final String API_CALL_DOMAIN = "sandbox-api.uber.com";
    public static final String API_CALL_VERSION = "v1";
    public static final String API_CALL_ENDPOINT = "estimates/price";

    static {
        public static final Set<String> DEFAULT_PRODUCTS =
                new HashSet<String>();

        DEFAULT_PRODUCTS.add("UberX"); // Just use this one for now.
    }

    /**
     * This method blocks. queryAllDestinations does not.
     */
    public static List<UberPriceEstimate> estimatePrice(UberPriceEstimateQuery query) {

        StringBuffer apiCall = new StringBuffer();
        apiCall.append(API_CALL_PROTOCOL);
        apiCall.append("://");
        apiCall.append(API_CALL_DOMAIN);
        apiCall.append("/");
        apiCall.append(API_CALL_VERSION);
        apiCall.append("/");
        apiCall.append(API_CALL_ENDPOINT);
        apiCall.append("?");

        // Add the coordinate parameters
        apiCall.append("start_latitude=");
        apiCall.append(Double.toString(query.getStartCoordinates().getLatitude()));

        apiCall.append("&");

        apiCall.append("start_longitude=");
        apiCall.append(Double.toString(query.getStartCoordinates().getLongitude()));

        apiCall.append("&");

        apiCall.append("end_latitude=");
        apiCall.append(Double.toString(query.getDestinationCoordinates().getLatitude()));

        apiCall.append("&");

        apiCall.append("end_longitude=");
        apiCall.append(Double.toString(query.getDestinationCoordinates().getLongitude()));

        String retrievedString = "";

        try {
            URL url = new URL(apiCall.toString());
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Authorization", "Token " + SERVER_ID);
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            retrievedString = convertinputStreamToString(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getEstimatesFromJSON(retrievedString, query);
    }

    public static List<UberPriceEstimate> getEstimatesFromJSON(String json,
                                                               UberPriceEstimateQuery query) {
        List<UberPriceEstimate> estimates = new List<UberPriceEstimate>();

        JSONObject rootObject = new JSONOjbect(json);

        JSONArray prices = rootObject.getJSONArray("prices");

        for (int i = 0; i < prices.length(); ++i) {
            JSONObject price = prices.getJSONObject(i);

            String productType = price.getString("display_name"); // TODO: use IDs instead

            // Ignore prices with productTypes not in the acceptable set.
            if (query.getAcceptableUberProductNames().contains(productType)) {
                // Make an UberPriceEstimate to encapsulate the data
                UberPriceEstimate uberPriceEstimate =
                        new UberPriceEstimate(query, price.getDouble("high_estimate"), productType);
                estimates.add(uberPriceEstimate);
            }
        }

        return estimates;
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

    public void queryAllDestinations(Coordinates startCoordinates,
            Set<String> acceptableUberProductNames, List<UberPriceEstimate> results,
             final Runnable onFinishCallback) {
        // TODO: this will be really inefficient if there are tons of destinations

        // Define the AsyncTask that performs this function asynchronously.
        // It will be able to access some of this function's values through pseudo-closure,
        // even after this function returns.
        class QueryAllDestinationsTask extends
                AsyncTask<UberPriceEstimateQuery, Integer, List<UberPriceEstimate>> {
            public Object doInBackground(UberPriceEstimateQuery... queries) {
                List<UberPriceEstimate> result = new ArrayList<UberPriceEstimate>();

                int progress = 0;
                publishProgress(progress);
                for (UberPriceEstimateQuery query : queries) {
                    List<UberPriceEstimate> priceEstimates = estimatePrice(query);
                    ++progress;
                    publishProgress(progress);
                    result.addAll(priceEstimates);
                }

                return result;
            }

            public void onPostExecute(Object result) {
                // Hey, we're finished!
                results.addAll(result);
                onFinishCallback.run();
            }
        }

        // Generate all the queries
        DestinationDatabaseAccessor destinationDatabaseAccessor =
                new EmptyDestinationDatabaseAccessor();

        List<Destination> allDestinations = destinationDatabaseAccessor.getAllDestinations();
        List<UberPriceEstimateQuery> allQueries = new ArrayList<UberPriceEstimateQuery>();

        for (Destination possibleDestination : allDestinations) {
            // Create a query for the current possible destination

            UberPriceEstimateQuery query = new UberPriceEstimateQuery(startCoordinates,
                    acceptableUberProductNames, possibleDestination);
            allQueries.add(query);
        }

        allQueries.toArray(new UberPriceEstimateQuery[]);
        // Start the AsyncTask, which will not block this thread, then return.
        new QueryAllDestinationsTask().execute(allQueries);
    }

    public static List<UberPriceEstimate> filterEstimatesByCost(List<UberPriceEstimate> estimates,
                                                                Double maxCost) {
        List<UberPriceEstimate> filteredEstimates = new ArrayList<UberPriceEstimate>();

        for (UberPriceEstimate uberPriceEstimate : estimates) {
            if (uberPriceEstimate.getCost() <= maxCost) {
                filteredEstimates.add(uberPriceEstimate);
            }
        }

        return filteredEstimates;
    }
}