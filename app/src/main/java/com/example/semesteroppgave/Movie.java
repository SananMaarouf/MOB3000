package com.example.semesteroppgave;

public class Movie {
    private String name;
    private String image;
    private String duration;
    private String overview;
    private String id;
    private float rating;

    public Movie(String name, String image, String duration, String overview, String id, float rating) {
        this.name = name;
        this.image = image;
        this.duration = duration;
        this.overview = overview;
        this.id = id;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDuration() {
        return duration;
    }

    public String toString(){
        return name +" "+image+" " + duration;
    }
}
