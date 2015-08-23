package com.android.chandan.myapp;

/**
 * Created by chandan on 8/23/2015.
 */
public class Application {
    private String artist;
    private String price;
    private String name;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "Name: "+ this.getName()+ "\n"+
                "Artist: "+this.getArtist()+ "\n"+
                "Price: "+this.getPrice()+"\n";
    }
}
