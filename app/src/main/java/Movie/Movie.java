package Movie;

import java.util.ArrayList;

import KlasseKomponenter.StreamingService;

public class Movie {
    private String name;
    private String image;
    private String release;
    private String overview;
    private String id;
    private float rating;

    private ArrayList<StreamingService> streamList = new ArrayList<>();

    public Movie(String name, String image, String release, String overview, String id, float rating) {
        this.name = name;
        this.image = image;
        this.release = release;
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
    public String getRelease() {
        return release;
    }
    public String getOverview(){
        return overview;
    }
    public String getId(){
        return id;
    }
    public float getRating(){
        return rating;
    }
    public String toString(){

        return name +" "+image+" " + release;
    }

    // legge til listen
    public void addStreamingService(StreamingService stream){
        streamList.add(stream);
    }

    // hente listen
    public ArrayList getStreamlist(){
        return streamList;
    }

}
