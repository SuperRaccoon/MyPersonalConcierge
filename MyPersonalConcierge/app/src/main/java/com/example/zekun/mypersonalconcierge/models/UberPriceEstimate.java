package com.example.zekun.mypersonalconcierge.models;

public class UberPriceEstimate {

    private UberPriceEstimateQuery query; // The query that resulted in this estimate.
    private double cost; // TODO: localize currencies
    private string uberProductName; // TODO: create an enum?

    public UberPriceEstimate(UberPriceEstimateQuery query, double cost) {
        this.query = query;
        this.cost = cost;
    }

    public UberPriceEstimateQuery getQuery() {
        return query;
    }

    public double getCost() {
        return cost;
    }
}