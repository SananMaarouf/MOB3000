package com.example.semesteroppgave;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionJoin extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Liste med aktive brukere i session
    ArrayList<String> brukereSession = new ArrayList<>();


    Button btn_finnFilm;
    private TextView bruker1;
    private TextView bruker2;
    private TextView bruker3;
    private TextView bruker4;
    private TextView bruker5;
    private TextView bruker6;
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
        bruker3 = findViewById(R.id.bruker3);
        bruker4 = findViewById(R.id.bruker4);
        bruker5 = findViewById(R.id.bruker5);

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

                joinSession(sessionidDB);
            }
        });



    }


    // Join session
    public void joinSession(String sessionIden){


    }

    // Sjekk for om bruker har en session som er aktiv, en bruker kan bare ha en aktiv session om gangen
    // endre til "contains i array" VIKTIG
    public void hasActiveSession(){
        db.collection("Session").whereEqualTo("admin", brukerid)
                .whereEqualTo("active", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            // Finner ingen aktive sessions, pressenter muligheter for å joine en session
                           // joinSession(sessionidDB);
                            Toast.makeText(SessionJoin.this, "No active session", Toast.LENGTH_LONG).show();

                        } else {
                            // Allerede aktiv i en session, vis den aktive sessionen
                            Toast.makeText(SessionJoin.this, "Already active in an session, leave before making an new one", Toast.LENGTH_LONG).show();
                            finnAktiveSession(sessionidDB);
                        }
                    }
                });
    }

    // finner den aktive sessionen og viser den
    public void finnAktiveSession(String idSession){
        System.out.println("HEEEEEEEEEEEERE");
        db.collection("Session").document(idSession)
                .collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                // Kjører for antall brukere som er i sessionen
                                int docSize = document.getData().size();
                                for(int i=0;i<docSize;i++){
                                    int valgtBruker = i+1;
                                    // Legger til brukere i listen
                                    brukereSession.set(i,(document.getString("bruker"+valgtBruker)));

                                }
                                visAktiveBrukere();
                            }

                        }
                    }});
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
