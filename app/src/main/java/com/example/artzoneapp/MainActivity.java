package com.example.artzoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {

    Chip btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.getstarted_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchtoHometoprofile();

            }
        });

    }

    private void switchtoHometoprofile(){
        Intent gotohome = new Intent(this,Register.class);
        startActivity(gotohome);
    }


}