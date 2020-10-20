package com.example.semesteroppgave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.btn_yes);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast toast_yes = Toast.makeText(getApplicationContext(), "It wørk", Integer.parseInt("5"));
                toast_yes.show();

            }
    });
    }
}