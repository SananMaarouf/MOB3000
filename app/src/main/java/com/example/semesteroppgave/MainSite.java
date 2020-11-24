package com.example.semesteroppgave;

import android.content.Intent;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import KlasseKomponenter.FinnApi;

public class MainSite extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Movie> filmer = new ArrayList<>();
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
    private ImageView bilde;
    private Button like;
    private Button dislike;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_site);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        release = findViewById(R.id.release);
        title = findViewById(R.id.title);
        bilde = findViewById(R.id.image);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);

        // 93e7133aa45445f8651ca9eda8a953b5
        System.out.println("STØØØØØRELSE: "+filmer.size());

        // prøver å hente ut informasjon
        try {

            JSONObject filmliste = finnApi.httpEtterFilm(sideApi);
            // finner maks antall sider;
            sisteApi = filmliste.getInt("total_pages");
            // Gjør JSON om til array
            JSONArray filmlisteArray = filmliste.getJSONArray("results");
            leggInnFilm(filmlisteArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settFilm(title,bilde,duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settFilm(title,bilde,release);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void settFilm(TextView title, ImageView bilde, TextView release) throws Exception {
        System.out.println("Er på filmnr: "+erPaFilm);
        System.out.println("Er på sidenr: "+sideApi);
        if(erPaFilm == filmer.size()){
            erPaFilm=0;
            filmer.clear();
            hentNyeFilmer();

        }else{
            Movie filmen = filmer.get(erPaFilm);
            title.setText(filmen.getName());
            release.setText(filmen.getRelease());
            erPaFilm++;
        }




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.home) {

        }
        if(menuItem.getItemId()==R.id.session) {
        }

        if(menuItem.getItemId()==R.id.settings){
        }
        if (menuItem.getItemId() == R.id.logout) {
            Intent intent = new Intent(MainSite.this, Login.class);
            startActivity(intent);
            finish();
        }
        return true;
    }


    // legger inn filmene fra moviedatabase i listen vår
    public void leggInnFilm(JSONArray filmliste) throws JSONException {
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
            filmer.add(nyfilm);
        }
        System.out.println("Størrelsen: "+filmer.size());

    }

    public void hentNyeFilmer() throws Exception {
        if(sideApi >= sisteApi){
            sideApi =0;

        } else {
            sideApi++;
            JSONObject filmliste = finnApi.httpEtterFilm(sideApi);
            // finner maks antall sider;
            sisteApi = filmliste.getInt("total_pages");
            // Gjør JSON om til array
            JSONArray filmlisteArray = filmliste.getJSONArray("results");
            leggInnFilm(filmlisteArray);
        }
    }

}
