package com.example.zekun.mypersonalconcierge.models;

public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public duble getLongitude() {
        return longitude;
    }

    /**
     * This is a custom implementation. Strings are returned in degree decimal notation.
     * e.x. 12.345 N 23.45678 W
     */
    public String toString() {
        return Double.toString(latitude) + " N " + Double.toString(longitude) + " W";
    }
}