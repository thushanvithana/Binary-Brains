package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.view.View;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Display_image_Artist_activity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ImageView imageView;
    TextView title,description;
    StorageReference fbst;
    ImageButton dots;
    FirebaseFirestore fbfsupart;
    FirebaseAuth cauth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_artist);

        imageView = findViewById(R.id.artimg);
        title = findViewById(R.id.atitle);
        description = findViewById(R.id.adescript);
        dots = findViewById(R.id.dots3);
        fbfsupart = FirebaseFirestore.getInstance();
        cauth = FirebaseAuth.getInstance();
        uid = cauth.getCurrentUser().getUid();


        ProgressDialog progressmassage = new ProgressDialog(this);
        progressmassage.setTitle("processing.....");
        progressmassage.show();

        String atitle = getIntent().getStringExtra("ArtTitle");
        String adescription = getIntent().getStringExtra("ArtDescription");
        String image = getIntent().getStringExtra("ImageName");

        fbst = FirebaseStorage.getInstance().getReference("image/"+image);

        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            fbst.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
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

        title.setText(atitle);
        description.setText(adescription);

    }
    public void popmenu(android.view.View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.imgdropmenu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        String aid = getIntent().getStringExtra("Aid");

        switch (menuItem.getItemId()){
            case R.id.item01:

                String atitle2 = getIntent().getStringExtra("ArtTitle");
                String adescription2 = getIntent().getStringExtra("ArtDescription");
                String image2 = getIntent().getStringExtra("ImageName");

                Intent intent = new Intent(this,Artist_edit_art_Activity.class);

                intent.putExtra("Aid",aid);
                intent.putExtra("ArtTitle",atitle2);
                intent.putExtra("ArtDescription",adescription2);
                intent.putExtra("ImageName",image2);

                startActivity(intent);

                return true;
            case R.id.item02:
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to delete?")
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fbfsupart.collection("Users").
                                        document(uid).collection("Artwork").
                                        document(aid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Display_image_Artist_activity.this, "Artwork delete successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Display_image_Artist_activity.this,Home_to_Profile.class);
                                                intent.putExtra("dmassage","afterDelete");
                                                startActivity(intent);
                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {
                                                Toast.makeText(Display_image_Artist_activity.this, "Artwork delete unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            default:
                return false;
        }
    }


}