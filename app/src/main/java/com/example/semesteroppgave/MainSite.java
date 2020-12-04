package com.example.semesteroppgave;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import KlasseKomponenter.FinnApi;
import Movie.Movie;
import Movie.MovieChecked;

public class MainSite extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Movie> filmer = new ArrayList<>();
    ArrayList<Movie> faktiskeFilmer = new ArrayList<>();

    // liste over likte og ulikte filmer
    ArrayList<MovieChecked> moviesRanked = new ArrayList<>();
   // ArrayList<Movie> dislikeFilm = new ArrayList<>();

    int erPaFilm = 0;
    // holder kontroll på hvilke side apien er på
    int sideApi = 1;
    // holder kontroll på hvor mange sider det er totalt i api spørringen. Husk å oppdater hver gang du gjør en ny spørring
    int sisteApi = 40;
    FinnApi finnApi = new FinnApi();

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    // Informasjon
    private TextView release;
    private TextView title;
    private TextView textheader;
    private ImageView bilde;
    private Button like;
    private Button dislike;
    private TextView rating;
    private TextView overview;
    private String url = "https://image.tmdb.org/t/p/w500";
    String brukerId = "ratCity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_site);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        release = findViewById(R.id.release);
        title = findViewById(R.id.title);
        bilde = findViewById(R.id.image);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        rating = findViewById(R.id.rating);
        overview = findViewById(R.id.overview);

        hentFilmerDB();
        // System.out.println("Størrelse totalt: "+moviesRanked.size());

        // 93e7133aa45445f8651ca9eda8a953b5


        // prøver å hente ut informasjon
        // DET UNDER ER KOMMENTERT VEKK PGA TESTING
        /*
        try {
            // Hva du skal søke på
            String sokEtter="popular";
            JSONObject filmliste = finnApi.httpEtterFilm(sideApi,sokEtter);
            // finner maks antall sider;
            sisteApi = filmliste.getInt("total_pages");
            // Gjør JSON om til array
            JSONArray filmlisteArray = filmliste.getJSONArray("results");

            leggInnFilm(filmlisteArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        //  System.out.println("STØØØØØRELSE: "+filmer.size());

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(erPaFilm!=0){
                        // Legg inn filmen
                        Movie filmen = faktiskeFilmer.get(erPaFilm-1);
                        MovieChecked filmChecked = new MovieChecked(filmen.getName(), filmen.getImage(),filmen.getRelease(),filmen.getOverview(),filmen.getId(),filmen.getRating(),true);
                        moviesRanked.add(filmChecked);
                        leggFilmListe(brukerId, filmChecked);
                    }
                  //  leggFilmListe(brukerId, filmer.get(erPaFilm).getId(), true);
                 //   moviesRanked.add(filmer.get(erPaFilm));

                    settFilm(title,bilde,release);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(erPaFilm!=0){
                        // Legg inn filmen
                        Movie filmen = faktiskeFilmer.get(erPaFilm-1);
                        MovieChecked filmChecked = new MovieChecked(filmen.getName(), filmen.getImage(),filmen.getRelease(),filmen.getOverview(),filmen.getId(),filmen.getRating(),false);
                        moviesRanked.add(filmChecked);
                        leggFilmListe(brukerId, filmChecked);

                    }
                  //  leggFilmListe(brukerId, filmer.get(erPaFilm).getId(), false);
                 //   moviesRanked.add(filmer.get(erPaFilm));

                    settFilm(title,bilde,release);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Getting user information from firebase
        if (user != null) {
            String name = user.getDisplayName();
            Uri personPhoto = user.getPhotoUrl();
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
            View headerView = navigationView.getHeaderView(0);
            TextView headername = (TextView) headerView.findViewById(R.id.headertext);
            headername.setText(name);
            ImageView headerpic = (ImageView) headerView.findViewById(R.id.headerpic);
            headerpic.setImageURI(null);
            Glide.with(this)
                    .load(personPhoto)
                    .into(headerpic);
            //headerpic.setImageURI(photo);
            //textheader.findViewById(R.id.drawerHeader);
        }
    }

    // Setter neste film i appen
    private void settFilm(TextView title, ImageView bilde, TextView release) throws Exception {
        //System.out.println("Er på filmnr: "+erPaFilm);
        // System.out.println("Er på sidenr: "+sideApi);
        // Sjekker om det er siste filmen på siden
        if(erPaFilm == faktiskeFilmer.size()){
            erPaFilm=0;
            faktiskeFilmer.clear();
            filmer.clear();
            hentNyeFilmer();

        }else{
            Movie filmen = faktiskeFilmer.get(erPaFilm);
            title.setText(filmen.getName());
            release.setText(filmen.getRelease());
            overview.setText(filmen.getOverview());
            rating.setText(filmen.getRating()+"");
            Glide.with(this)
                    .load(url+filmen.getImage())
                    .into(bilde);
            erPaFilm++;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.home) {
            Intent intent = new Intent(MainSite.this, MainSite.class);
            startActivity(intent);
            finish();
        }
        if(menuItem.getItemId()==R.id.session) {
            Intent intent = new Intent(MainSite.this, Session.class);
            startActivity(intent);

        }

        if(menuItem.getItemId()==R.id.settings){
            Intent intent = new Intent(MainSite.this, SessionJoin.class);
            startActivity(intent);

        }

        if (menuItem.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(MainSite.this, Login.class);
            startActivity(intent);
            finish();
        }
        return true;
    }


    // legger inn filmene fra moviedatabase i listen vår
    public void leggInnFilm(JSONArray filmliste) throws Exception {
        // henter ut informasjon om hver film en etter en
        for(int i=0; i< filmliste.length(); i++){
            JSONObject filmen = filmliste.getJSONObject(i);

            String name = filmen.getString("title");
            String image = filmen.getString("poster_path");
            String release = filmen.getString("release_date");
            String overview = filmen.getString("overview");
            String id = filmen.getString("id");
            float rating = Float.parseFloat(filmen.getString("vote_average"));

            // lager objektet
            Movie nyfilm = new Movie(name,image,release,overview,id,rating);

            // sjekker om filmen er testet før hvis true legg til i listen
            //boolean filmFinnes = sjekkFilm(nyfilm);
            filmer.add(nyfilm);

        }
        sjekkFilm();
        settFilm(title,bilde,release);
        //sjekkFilm();
        //System.out.println("Størrelsen: "+filmer.size());

    }

    // henter nye filmer fra the movie database
    public void hentNyeFilmer() throws Exception {
        if(sideApi >= sisteApi){
            sideApi =0;

        } else {
            sideApi++;
            String sokEtter="popular";
            JSONObject filmliste = finnApi.httpEtterFilm(sideApi,sokEtter);
            // finner maks antall sider;
            sisteApi = filmliste.getInt("total_pages");
            // Gjør JSON om til array
            JSONArray filmlisteArray = filmliste.getJSONArray("results");
            leggInnFilm(filmlisteArray);
        }
    }

    // Legger filmen inn i likt/ikke lik listen i db
    public void leggFilmListe(String brukerID, MovieChecked filmen){
        Map<String, Object> filmer = new HashMap<>();
        filmer.put("name", filmen.getName());
        filmer.put("image", filmen.getImage());
        filmer.put("release", filmen.getRelease());
        filmer.put("id", filmen.getId());
        filmer.put("overview", filmen.getOverview());
        filmer.put("rating", filmen.getRating()+"");
        filmer.put("likt", filmen.getLiked());

        // legger inn i db
        db.collection("Users").document(brukerID)
                .collection("film").document(filmen.getId()).set(filmer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //System.out.println(filmen.getId());
                    }
                });
    }

    // sjekker om filmen er likt/disliked
    // sjekker via db atm, kanskje sjekke internt i programmet
    public void sjekkFilm() {
        for (Movie filmene : filmer) {
            db.collection("Users").document(brukerId)
                    .collection("film").whereEqualTo("id", filmene.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        //System.out.println("Filmen finnes allerede");
                    }
                    if (task.getResult().isEmpty()) {
                        faktiskeFilmer.add(filmene);
                    }
                }


            });
        }
    }

    public void hentFilmerDB(){
        // Finner filmer
        db.collection("Users").document(brukerId)
                .collection("film").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String navn = document.getString("name");
                        String image = document.getString("image");
                        String release = document.getString("release");
                        String id = document.getString("id");
                        String overview = document.getString("overview");
                        float rating = Float.parseFloat(document.getString("rating"));
                        boolean liked = document.getBoolean("likt");

                        MovieChecked filmen = new MovieChecked(navn,image,release,overview,id,rating, liked);
                        moviesRanked.add(filmen);
                    }
                }
            }
        });


    }
}
