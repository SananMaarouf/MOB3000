package com.example.semesteroppgave;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Session extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Liste med aktive brukere i session
    ArrayList<String> brukereSession = new ArrayList<>();

    Button btn_finnFilm;
    private TextView bruker1;
    private TextView bruker2;
    private TextView bruker3;
    private TextView bruker4;
    private TextView bruker5;
    private TextView sessionId;

    // Innloggede bruker/ party leader
    String brukerid = "kekekek";
    String sessionidDB = "420kekekek";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        getSupportActionBar().hide();

        // setter filedsene
        btn_finnFilm = findViewById(R.id.btn_finnFilm);
        sessionId = findViewById(R.id.sessionId);
        bruker1 = findViewById(R.id.bruker1);
        bruker2 = findViewById(R.id.bruker2);

        // Setter arralisisten til å inneholde null verider for alle plassene
        brukereSession.add(null);
        brukereSession.add(null);
        brukereSession.add(null);
        brukereSession.add(null);
        brukereSession.add(null);

        // Sjekker først om bruker har en aktiv session
        hasActiveSession();

        // Oppretter session og setter brukernavnet inn
       // createSession();
        bruker1.setText(brukerid);
        sessionId.setText("SessionID: #"+sessionidDB);

        btn_finnFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    // Oppretter session i db
    public void createSession(){
        Map<String, Object> sessionut = new HashMap<>();
        sessionut.put("sessionId", sessionidDB);
        sessionut.put("admin", brukerid);

        Map<String, Object> sessionBrukere = new HashMap<>();
        sessionBrukere.put("bruker1", brukerid);

        Map<String, Object> sessionAktiv = new HashMap<>();
        sessionAktiv.put("active", true);

        // legger inn i db
        // Legger først inn selve session
        db.collection("Session").document(sessionidDB).set(sessionut)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Legger inn brukeren i sessionen
                db.collection("Session").document(sessionidDB)
                        .collection("Users").document("AlleBrukere")
                        .set(sessionBrukere).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Setter aktiv på brukeren som opprettet session
                        db.collection("Users").document(brukerid).update(sessionAktiv)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Alt er klar for session
                                        Toast.makeText(Session.this, "Session created", Toast.LENGTH_SHORT).show();
                                        brukereSession.set(0, brukerid);
                                    }
                                });

                    }
                });


            }
        });
      //  visAktiveBrukere();
    }

    // Sjekk for om bruker har en session som er aktiv, en bruker kan bare ha en aktiv session om gangen
    public void hasActiveSession(){
        CollectionReference bruker = db.collection("Users");
        bruker.whereEqualTo("email", brukerid).whereEqualTo("active", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                // Har ingen aktiv session, kan opprette en ny session
                                createSession();

                            } else {
                                // Er allerede medlem av en session, vises til den
                                Intent intent = new Intent(Session.this, SessionJoin.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }
                });


    }

    // Setter alle aktive brukere til textboksene
    public void visAktiveBrukere(){
        bruker1.setText(brukereSession.get(0));
        bruker2.setText(brukereSession.get(1));
        bruker3.setText(brukereSession.get(2));
        bruker4.setText(brukereSession.get(3));
        bruker5.setText(brukereSession.get(4));

    }


}
