package com.example.semesteroppgave;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    boolean isInSession = false;

    Button btn_finnSession;
    private TextInputLayout SessionIDInput;
    private TextView bruker1;
    private TextView bruker2;
    private TextView bruker3;
    private TextView bruker4;
    private TextView bruker5;
    private TextView sessionId;

    // Innloggede bruker/ party leader
    String brukerid = "ratCity";
    String sessionidDB = "420kekekek";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionjoin);
        getSupportActionBar().hide();

        // setter filedsene
        btn_finnSession = findViewById(R.id.btn_finnSession);

        SessionIDInput = findViewById(R.id.SessionIDInput);
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
        //bruker1.setText(brukerid);
        //sessionId.setText("SessionJoinID: #"+sessionidDB);

        btn_finnSession.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Editable sessionTallet = SessionIDInput.getEditText().getText();

                //Toast.makeText(SessionJoin.this, sessionTallet.toString(), Toast.LENGTH_SHORT).show();
                joinSession(sessionTallet.toString());
            }
        });

    }


    // Join session
    public void joinSession(String sessionIden){
        // Sjekk om sessioniden eksisterer
        db.collection("Session").whereEqualTo("sessionId", sessionIden)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        Toast.makeText(SessionJoin.this, "Did not find session", Toast.LENGTH_SHORT).show();

                    } else {
                        // Session eksisterer, sjekk om det er plass i den
                        db.collection("Session").document(sessionIden)
                                .collection("Users").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.getResult().size()<=5){
                                            int tallet = task.getResult().size()+1;

                                            String brukernr = "bruker"+ tallet;
                                            Map<String, Object> dataen = new HashMap<>();
                                            dataen.put(brukernr, brukerid);
                                            // Det er plass, legg bruker til
                                            db.collection("Session").document(sessionIden).collection("Users")
                                                    .document("AlleBrukere").update(dataen).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Sett bruker som aktive
                                                    Map<String, Object> brukerAktiv = new HashMap<>();
                                                    brukerAktiv.put("active", true);

                                                    db.collection("Users").document(brukerid).update(brukerAktiv)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Lagt til
                                                                    Toast.makeText(SessionJoin.this, "", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                }
                                            });

                                        } else {
                                            // er ikke plass gi bedkjed
                                            Toast.makeText(SessionJoin.this, "Session is full", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                }
            }
        });


    }

    // Sjekk for om bruker har en session som er aktiv, en bruker kan bare ha en aktiv session om gangen
    // endre til "contains i array" VIKTIG
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
                                isInSession = false;
                                Toast.makeText(SessionJoin.this, "You have no current session, join or create one", Toast.LENGTH_SHORT).show();
                                btn_finnSession.setVisibility(View.VISIBLE);
                            } else {
                                // Er allerede medlem av en session, vises til den
                                finnAktiveSession(sessionidDB);
                                Toast.makeText(SessionJoin.this, "You already have a session, you can only have 1 at a time", Toast.LENGTH_SHORT).show();
                                isInSession = true;
                                btn_finnSession.setVisibility(View.INVISIBLE);
                            }
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