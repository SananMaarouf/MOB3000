package com.example.semesteroppgave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        callSignIn = findViewById(R.id.signUpButton);
        returnSignInButton = findViewById(R.id.signin_screen);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.confirmPassword);

        mAuth = FirebaseAuth.getInstance();

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
            public void onClick(View view) {
                Editable emailText = email.getEditText().getText();
                Editable passwordText = password.getEditText().getText();
                Editable password2Text = password2.getEditText().getText();

                // sjekker om passordene e like
                if (passwordText.toString().equals(password2Text.toString())) {
                    System.out.println("Like");
                    //CREATE NEW USER BASED ON EMAIL AND PASSWORD
                    mAuth.createUserWithEmailAndPassword(emailText.toString(), passwordText.toString())
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT).show();

                                        // Legger til i db
                                        Map<String, Object> bruker = new HashMap<>();
                                        bruker.put("active", false);
                                        bruker.put("activeSession", "null");
                                        bruker.put("email", user.getEmail());

                                        db.collection("Users").document(user.getUid()).set(bruker).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignUp.this, MainSite.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });


                                    }
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Error: user not created", Toast.LENGTH_SHORT).show();
                                        /* failed */
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


