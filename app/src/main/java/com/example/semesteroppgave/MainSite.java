package com.example.semesteroppgave;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainSite extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Movie> filmer = new ArrayList<>();
    int erPaFilm = 0;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    // Informasjon
    private TextView duration;
    private TextView title;
    private CardView bilde;
    private Button like;
    private Button dislike;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_site);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        duration = findViewById(R.id.duration);
        title = findViewById(R.id.title);
        bilde = findViewById(R.id.image);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);


        nyFilmVis();
        System.out.println("STØØØØØRELSE: "+filmer.size());



        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settFilm(title,bilde,duration);
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settFilm(title,bilde,duration);
            }
        });
    }
    private void settFilm(TextView title, CardView bilde, TextView duration) {
        Movie filmen = filmer.get(erPaFilm);


        title.setText(filmen.getName());
        duration.setText(filmen.getDuration());
        erPaFilm++;
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

    // Viser ny film
    public void nyFilmVis(){
        CollectionReference movies = db.collection("Movie");
        movies.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        //System.out.println(document.getData());
                        String navn = document.getString("name");
                        String image = document.getString("image").toString();
                        String duration = document.getString("duration");

                        //System.out.println("Navn: "+ navn + " image: " + image+ " duration: " + duration);
                        Movie nyfilm = new Movie(navn,image,duration);
                        System.out.println("Sys: "+nyfilm.toString());
                        filmer.add(nyfilm);
                        System.out.println(filmer.size());
                    }
                } else {
                    System.out.println("monkaS");
                }
            }

        });
        for(Movie filmen: filmer){
            System.out.println(filmen.toString());

        }

    }

}
