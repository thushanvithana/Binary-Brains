package com.example.artzoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Artist_upload_art_Activity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_upload_art);

        uploading_image = findViewById(R.id.uploaded_image_upa);
        upload_btn = findViewById(R.id.upload_btn_upa);
        title = findViewById(R.id.title_upa);
        art_title = findViewById(R.id.art_title_upa);
        art_description = findViewById(R.id.art_description_upa);
        fbfsupart = FirebaseFirestore.getInstance();
        cauth = FirebaseAuth.getInstance();

        //Onclick event for upload image from phone storage

        uploading_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });

        //Onclick event for upload artwork to the database

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadart();
            }
        });

    }





    private void uploadart() {

        ProgressDialog progressmassage = new ProgressDialog(this);
        progressmassage.setTitle("Uploading");
        progressmassage.show();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String filename = formatter.format(now);

        String title = art_title.getText().toString();
        String description = art_description.getText().toString();
        artId = title+"_"+ filename;


        fbstorageref = FirebaseStorage.getInstance().getReference("image/" + filename); //give the way to save the image(which directory with specific name)

        uid = cauth.getCurrentUser().getUid();
        DocumentReference documentReferenceReference = fbfsupart.collection("Users").document(uid).collection("Artwork").document(artId);
        Map<String,Object> adetails = new HashMap<>();
        adetails.put("AID",artId);
        adetails.put("art_title",title);
        adetails.put("art_description",description);
        adetails.put("image_name",filename);

        documentReferenceReference.set(adetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                fbstorageref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if(progressmassage.isShowing()){
                            progressmassage.dismiss();
                        }
                        Toast.makeText(Artist_upload_art_Activity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if(progressmassage.isShowing()){
                            progressmassage.dismiss();
                        }
                        Toast.makeText(Artist_upload_art_Activity.this, "Upload unsuccessful!", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                if(progressmassage.isShowing()){
                    progressmassage.dismiss();
                }
                Toast.makeText(Artist_upload_art_Activity.this, "Upload unsuccessful!", Toast.LENGTH_SHORT).show();


            }
        });



    }









    private void selectimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data!=null&&data.getData()!=null){
            imageuri = data.getData();
            uploading_image.setImageURI(imageuri);
        }
    }


}



            /*

           ------------------------ upload image to the database ------------------------------

            fbstorageref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    whensuccess();


                    }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    whenunsucess(e.toString());
                    }
                    });




            ---------------------------- upload data to the database -----------------------------

            firebasedatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    firebasedatabaseref.child(artId).setValue(atd);
                    whensuccess();
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    whenunsucess(error.toString());
                    }
                    });


                    */


