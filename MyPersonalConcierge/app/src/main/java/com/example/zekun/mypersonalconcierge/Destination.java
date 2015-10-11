package com.example.zekun.mypersonalconcierge;

public class Destination {

    private String name;
    private String description;
    private String lon;
    private String lat;
    private String price;
    //private String url;

    /*public String getURL(){
        return url;
    }*/
    public String getLon() {
        return lon;
    }
    public String getLat(){
        return lat;
    }
    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(String price){

        this.price = price;
    }

    public String toString(){

        return name + ": " + price;
    }

    public Destination(String name, String description, String lat,
                       String lon){//, String url) {
        this.name = name;
        this.description = description;
        this.lon = lon;
        this.lat=lat;
        //this.url=url;
    }
}