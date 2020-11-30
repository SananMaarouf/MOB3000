package Movie;

public class MovieChecked extends Movie {
    private boolean liked;

    public MovieChecked(String name, String image, String release, String overview, String id, float rating,boolean liked) {
        super(name, image, release, overview, id, rating);
        this.liked = liked;
    }

    public void setLiked(boolean liked){
        this.liked = liked;
    }

    public boolean getLiked(){
        return liked;
    }
}
