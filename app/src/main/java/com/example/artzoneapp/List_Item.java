package com.example.artzoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class List_Item extends AppCompatActivity {

    Button editbtn,deletebtn;
    ConstraintLayout inputsec;
    EditText linstname;
    TextView okbtn,canclebtn;
    FirebaseFirestore fbfsupart;
    FirebaseAuth cauth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        editbtn = findViewById(R.id.listeditbtn);
        deletebtn = findViewById(R.id.deletebtn);
        inputsec = findViewById(R.id.inputsec);
        linstname = findViewById(R.id.listnameinput);
        okbtn = findViewById(R.id.okbtn);
        canclebtn = findViewById(R.id.canclebtn);
        fbfsupart = FirebaseFirestore.getInstance();
        cauth = FirebaseAuth.getInstance();
        uid = cauth.getCurrentUser().getUid();

        String listname = getIntent().getStringExtra("listname");
        String id = getIntent().getStringExtra("listid");

                linstname.setText(listname);

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputsec.setVisibility(View.VISIBLE);

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(List_Item.this)
                        .setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                fbfsupart.collection("Users").
                                        document(uid).collection("Lists").
                                        document(id).
                                        delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(List_Item.this, "Delete successfully list: "+getIntent().getStringExtra("listname"), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(List_Item.this,Home_to_Profile.class));
                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {
                                                Toast.makeText(List_Item.this, "Delete unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }).setNegativeButton("No",null).show();

            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Map<String,Object> adetails = new HashMap<>();
                adetails.put("name",linstname.getText().toString());


                fbfsupart.collection("Users").
                        document(uid).collection("Lists").
                        document(id).
                        update(adetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(List_Item.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(List_Item.this,Home_to_Profile.class));
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Toast.makeText(List_Item.this, "upload unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputsec.setVisibility(View.INVISIBLE);
            }
        });
    }
}