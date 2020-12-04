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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    String aktiveSession = "";
    Button btn_returnHome;
    Button btn_finnSession;
    private TextInputLayout SessionIDInput;
    private TextView bruker1;
    private TextView bruker2;
    private TextView bruker3;
    private TextView bruker4;
    private TextView bruker5;
    private TextView sessionId;

    // Innloggede bruker/ party leader
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String brukerid = user.getUid();

    // setter iden

    String sessionidDB = "SessionJoin #";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionjoin);
        getSupportActionBar().hide();

        // setter filedsene
        btn_finnSession = findViewById(R.id.btn_finnSession);
        btn_returnHome = findViewById(R.id.returnHome);
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
                // logge ut
                if(isInSession){
                    leaveSession();

                } else {
                    // Joine
                    Editable sessionTallet = SessionIDInput.getEditText().getText();

                    //Toast.makeText(SessionJoin.this, sessionTallet.toString(), Toast.LENGTH_SHORT).show();
                    joinSession(sessionTallet.toString());
                }

            }
        });
        btn_returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionJoin.this, MainSite.class);
                startActivity(intent);
                finish();
            }
        });

        final DocumentReference docRef = db.collection("Session").document(sessionidDB).collection("Users").document("AlleBrukere");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if(snapshot != null && snapshot.exists()){
                    System.out.println("Oppdatert");
                    finnAktiveSession(sessionidDB);
                }

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
                                            System.out.println("Task size " +task.getResult().size());

                                            int tallet = task.getResult().size()+1;

                                            String brukernr = "bruker"+ tallet;
                                            System.out.println("Brukernr+tall "+brukernr);

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
                                                    brukerAktiv.put("activeSession", sessionIden);

                                                    db.collection("Users").document(brukerid).update(brukerAktiv)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Lagt til
                                                                    isInSession = true;
                                                                    Toast.makeText(SessionJoin.this, "Session Joined", Toast.LENGTH_SHORT).show();
                                                                    aktiveSession = sessionIden;
                                                                    finnAktiveSession(sessionIden);

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
        bruker.whereEqualTo("email", user.getEmail()).whereEqualTo("active", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                // Har ingen aktiv session, kan opprette en ny session
                                isInSession = false;
                                Toast.makeText(SessionJoin.this, "You have no current session, join or create one", Toast.LENGTH_SHORT).show();
                                sjekkKnappNavn();
                               // btn_finnSession.setVisibility(View.VISIBLE);
                            } else {
                                // Er allerede medlem av en session, vises til den
                                String idenS = "null";
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    idenS = document.getString("activeSession");
                                }

                                finnAktiveSession(idenS);
                                Toast.makeText(SessionJoin.this, "You already have a session, you can only have 1 at a time", Toast.LENGTH_SHORT).show();
                                isInSession = true;
                                sjekkKnappNavn();
                              //  btn_finnSession.setVisibility(View.INVISIBLE);
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
                            aktiveSession = idSession;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                // Kjører for antall brukere som er i sessionen
                                int docSize = document.getData().size();
                                for(int i=0;i<docSize;i++){
                                    int valgtBruker = i+1;
                                    // Legger til brukere i listen
                                    brukereSession.set(i,(document.getString("bruker"+valgtBruker)));

                                }
                                visAktiveBrukere();
                                sjekkKnappNavn();
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

    public void sjekkKnappNavn(){
        if(isInSession){
            // sett vist knapp til leave
            btn_finnSession.setText("Leave session");


        } else {
            // sett knapp til finn session
            btn_finnSession.setText("Find session");
        }
    }

    // leave session
    public void leaveSession(){
        // Lager local liste med brukere
        ArrayList<String> brukere = new ArrayList<>();
        Map<String, Object> brukereH = new HashMap<>();
        System.out.println("AKTIVE "+aktiveSession);

        // finner session bruker er med i aktiveSession  bruker.whereEqualTo("email", user.getEmail()).whereEqualTo("active", true).get()
        db.collection("Session").document(aktiveSession)
                .collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resetBrukere();

                            for(QueryDocumentSnapshot document: task.getResult()){
                                int docSize = document.getData().size();
                                for(int i=0;i<docSize;i++){
                                    int valgtBruker = i+1;
                                    // Legger til brukere i listen
                                    brukereSession.set(i,(document.getString("bruker"+valgtBruker)));
                                }


                            }
                            Map mappen = hentBrukere();
                            resetBrukere();

                            // setter inn den oppdaterte listen
                            db.collection("Session").document(aktiveSession)
                                    .collection("Users").document("AlleBrukere").set(mappen)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // brukere oppdatert
                                            // oppdater active og activeSession i brukerdataen
                                            Map<String, Object> brukerOpp = new HashMap<>();
                                            brukerOpp.put("active", false);
                                            brukerOpp.put("activeSession", "Null");
                                            db.collection("Users").document(user.getUid()).update(brukerOpp)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // alt klart, session leavet
                                                            isInSession = false;
                                                            visAktiveBrukere();
                                                            sjekkKnappNavn();

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });




    }


    public void resetBrukere(){
        brukereSession.set(0, null);
        brukereSession.set(1, null);
        brukereSession.set(2, null);
        brukereSession.set(3, null);
        brukereSession.set(4, null);
    }

    // hent hashmap brukere
    public Map hentBrukere(){
        Map<String, Object> mappen = new HashMap<>();
        String brukeren = "bruker";
        for(int i=0; i<brukereSession.size();i++){
            int intbruker = i+1;

            mappen.put(brukeren+intbruker, brukereSession.get(i));
        }
        return mappen;
    }
}
