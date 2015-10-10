package com.example.zekun.mypersonalconcierge.utilities;

import com.example.zekun.mypersonalconcierge.models.Coordinates;
import com.example.zekun.mypersonalconcierge.models.UberPriceEstimate;
import com.example.zekun.mypersonalconcierge.models.UberPriceEstimateQuery;

import com.example.zekun.mypersonalconcierge.utilities.DestinationDatabaseAccessor;

import java.util.List;
import java.util.Set;

public class UberAPIAccessor {
    public static List<UberPriceEstimate> estimatePrice(UberPriceEstimateQuery query) {
        // TODO: implement this
        return null;
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