package com.example.zekun.mypersonalconcierge.utilities;

import com.example.zekun.mypersonalconcierge.models.Coordinates;
import com.example.zekun.mypersonalconcierge.models.UberPriceEstimate;
import com.example.zekun.mypersonalconcierge.models.UberPriceEstimateQuery;

import java.util.List;
import java.util.Set;

public class UberAPIAccessor {
    public static List<UberPriceEstimate> estimatePrice(UberPriceEstimateQuery query) {
        // TODO: implement this
        return null;
    }

    public static List<UberPriceEstimate> queryAllDestinations(Coordinates startCoordinates,
            Set<String> acceptableUberProductNames) {
        // TODO: create a bunch of queries and return all their results
        return null;
    }
}