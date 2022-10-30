package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Artist_edit_art_Activity extends AppCompatActivity {

    ImageView uploading_image;
    TextView title;
    EditText art_title,art_description;
    Button upload_btn,cancle_btn;
    Uri imageuri;
    StorageReference fbstorageref;
    String artId;
    String uid;
    ProgressDialog pgdialog;
    FirebaseFirestore fbfsupart;
    FirebaseAuth cauth;
    StorageReference fbst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_edit_art);

        ProgressDialog progressmassage = new ProgressDialog(this);
        progressmassage.setTitle("processing.....");
        progressmassage.show();

        uploading_image = findViewById(R.id.uploaded_image_upa);
        upload_btn = findViewById(R.id.upload_btn_upa);
        title = findViewById(R.id.title_upa);
        art_title = findViewById(R.id.art_title_upa);
        art_description = findViewById(R.id.art_description_upa);
        fbfsupart = FirebaseFirestore.getInstance();
        cauth = FirebaseAuth.getInstance();

        String atitle = getIntent().getStringExtra("ArtTitle");
        String adescription = getIntent().getStringExtra("ArtDescription");
        String image = getIntent().getStringExtra("ImageName");

        fbst = FirebaseStorage.getInstance().getReference("image/" + image);

        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            fbst.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                    uploading_image.setImageBitmap(bitmap);
                    if(progressmassage.isShowing()){
                        progressmassage.dismiss();
                    }
                    Log.d(TAG,"Successfully fetch the image");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(progressmassage.isShowing()){
                        progressmassage.dismiss();
                    }
                    Log.d(TAG,"Image fetch unsuccessful"+e);
                }
            });
        } catch (IOException e) {
            if(progressmassage.isShowing()){
                progressmassage.dismiss();
            }
            e.printStackTrace();
        }

        art_title.setText(atitle);
        art_description.setText(adescription);



        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editart();
            }
        });
    }


    private void editart(){

        String editedtitle = art_title.getText().toString();
        String editeddescription = art_description.getText().toString();
        String currentartid = getIntent().getStringExtra("Aid");
        uid = cauth.getCurrentUser().getUid();


        Map<String,Object> adetails = new HashMap<>();
        adetails.put("art_title",editedtitle);
        adetails.put("art_description",editeddescription);

        fbfsupart.collection("Users").
                document(uid).collection("Artwork").
                document(currentartid).
                update(adetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Artist_edit_art_Activity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Artist_edit_art_Activity.this,Home_to_Profile.class));
            }
        }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(Artist_edit_art_Activity.this, "upload unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });

    }



}