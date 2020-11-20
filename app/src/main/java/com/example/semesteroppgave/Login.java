package com.example.semesteroppgave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;

public class Login extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button callSignUp;
    Button loginButton;
    Button forgotPasswordButton;
    private TextInputLayout username;
    private TextInputLayout password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup_screen);
        loginButton = findViewById(R.id.signInButton);
        forgotPasswordButton = findViewById(R.id.forgotPassword);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Editable usernameText = username.getEditText().getText();
                Editable passwordText = password.getEditText().getText();

                CollectionReference brukerene = db.collection("Users");
                brukerene.whereEqualTo("email", usernameText.toString()).whereEqualTo("password", passwordText.toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                System.out.println(document.getId() + " " + document.getData());
                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                            if(task.getResult().isEmpty()){
                                Toast.makeText(Login.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });

            }
        });
        forgotPasswordButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Toast.makeText(Login.this, "Function not ready", Toast.LENGTH_SHORT).show();
            }
        });
    }
}