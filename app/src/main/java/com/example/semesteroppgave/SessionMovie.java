package com.example.semesteroppgave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class SessionMovie extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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


    }

}
