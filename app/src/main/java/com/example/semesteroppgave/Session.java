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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class Session extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        sessionut.put("active", true);
        sessionut.put("sessionId", sessionidDB);
        sessionut.put("admin", brukerid);

        Map<String, Object> sessionBrukere = new HashMap<>();
        sessionBrukere.put("bruker1", brukerid);
        // legger inn i db
        db.collection("Session").document(sessionidDB).set(sessionut)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("Session").document(sessionidDB)
                        .collection("Users").document("AlleBrukere")
                        .set(sessionBrukere).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Session.this, "Session created", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    // Sjekk for om bruker har en session som er aktiv, en bruker kan bare ha en aktiv session om gangen
    public void hasActiveSession(){
        db.collection("Session").whereEqualTo("bruker1", brukerid)
                .whereEqualTo("active", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            // Finner ingen aktive sessions, kan legges til i den nye
                           // joinSession(sessionidDB);
                            //Toast.makeText(Session.this, "Session created", Toast.LENGTH_LONG).show();
                            // Ingen aktive sessions, en blir opprettet
                            createSession();


                        } else {
                            // Allerede aktiv i en session, vis den aktive sessionen
                           // Toast.makeText(Session.this, "Already active in an session", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Session.this, SessionJoin.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }


}
