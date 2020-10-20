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

        final Button btn_yes = findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(v -> {
            Toast toast_yes = Toast.makeText(getApplicationContext(), "It wørk", Integer.parseInt("2"));
            toast_yes.show();

        });
        final Button btn_no = findViewById(R.id.btn_no);
        btn_no.setOnClickListener(v -> {
            Toast toast_no = Toast.makeText(getApplicationContext(), "It no wørk", Integer.parseInt("2"));
            toast_no.show();


        });
    }
}