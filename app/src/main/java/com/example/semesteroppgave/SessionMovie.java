package com.example.semesteroppgave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Movie.Movie;

public class SessionMovie extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> filmer = new ArrayList<String>();
    Button returnhome;
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionmovie);
        getSupportActionBar().hide();
        returnhome=findViewById(R.id.btn_returnHome);
        listview=findViewById(R.id.listview);

        returnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionMovie.this, Session.class);
                startActivity(intent);
                finish();
            }
        });

        getMovies();
        System.out.println("Størrelse "+filmer.size());




    }


    // henter filmer
    public void getMovies(){
        db.collection("Users").document(user.getUid()).collection("film").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            System.out.println("Task stør "+task.getResult().size());
                            for(QueryDocumentSnapshot document: task.getResult()){

                                String navn = document.getString("name");
                                String image = document.getString("image");
                                String release = document.getString("release");
                                String id = document.getString("id");
                                String overview = document.getString("overview");
                                float rating = Float.parseFloat(document.getString("rating"));
                                System.out.println(navn);

                                filmer.add(document.getString("name"));
                                //System.out.println("Størrelse3 "+filmer.size());

                            }
                            adapterKjør();
                        }
                    }

                });
        System.out.println("Størrelse2 "+filmer.size());
    }

    public void adapterKjør(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                filmer);
        listview.setAdapter(adapter);
    }
}
