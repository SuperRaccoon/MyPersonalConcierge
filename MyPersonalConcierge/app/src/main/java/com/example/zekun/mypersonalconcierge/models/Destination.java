package com.example.zekun.mypersonalconcierge.models;

public class Destination {

    private String name;
    private String description;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    private Coordinates coordinates;
    private double rating; // 1 to 5 stars?
    private int id; // This is the index in the master destination list.

    public Destination(String name, String description, Coordinates coordinates, double rating,
                       int id) {
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
        this.rating = rating;
        this.id = id;
    }
}