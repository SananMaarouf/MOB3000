package com.example.semesteroppgave;

public class Movie {
    private String name;
    private String image;
    private String duration;

    public Movie(String name, String image, String duration) {
        this.name = name;
        this.image = image;
        this.duration = duration;
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
