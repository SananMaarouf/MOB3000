package com.example.semesteroppgave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    // db
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button callSignIn;
    Button returnSignInButton;
    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        callSignIn = findViewById(R.id.signUpButton);
        returnSignInButton = findViewById(R.id.signin_screen);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.confirmPassword);

        // Returns user to login site
        returnSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        callSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Editable emailText = email.getEditText().getText();
                Editable passwordText = password.getEditText().getText();
                Editable password2Text = password2.getEditText().getText();

                // sjekker om passordene e like
                if(passwordText.toString().equals(password2Text.toString())){
                    System.out.println("Like");
                    regUser(emailText, passwordText);
                } else {
                    Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
                System.out.println(emailText);
                System.out.println(passwordText);
                System.out.println(password2Text);
            }
        });
    }
    // legger inn data
    public void regUser(Editable email, Editable password){
        Map<String, Object> user = new HashMap<>();
        user.put("password", password.toString());
        user.put("email", email.toString());

        db.collection("Users").document(email.toString()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Error, user not created!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}