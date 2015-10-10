package com.example.zekun.mypersonalconcierge.models;

public class UberPriceEstimateQuery {

    private Coordinates startCoordinates;
    private Set<String> acceptableUberProductNames;
    private Destination destination;

    public UberPriceEstimateQuery(Coordinates startCoordinates,
                                  Set<String> acceptableUberProductNames, Destination destination) {
        this.startCoordinates = startCoordinates;
        this.acceptableUberProductNames = acceptableUberProductNames;
        this.destination = destination;
    }

    public Coordinates getStartCoordinates() {
        return startCoordinates;
    }

    public Set<String> getAcceptableUberProductNames() {
        return acceptableUberProductNames;
    }

    public Destination getDestination() {
        return destination;
    }

    // Convenience getters
    public getDestinationCoordinates() {
        return destination.getCoordinates();
    }

    public getDestinationId() {
        return destination.getId();
    }
}