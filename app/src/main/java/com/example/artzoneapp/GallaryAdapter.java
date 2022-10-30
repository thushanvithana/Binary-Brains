package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.Gadapter> {

    Context context;
    ArrayList<ArtDetails> artDetails;
    StorageReference fbst;

    public GallaryAdapter(Context context, ArrayList<ArtDetails> artDetails) {
        this.context = context;
        this.artDetails = artDetails;
    }

    @NonNull
    @Override
    public GallaryAdapter.Gadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item,parent,false);
        return new Gadapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GallaryAdapter.Gadapter holder, int position) {

        ArtDetails art = artDetails.get(position);
        fbst = FirebaseStorage.getInstance().getReference("image/"+art.getImage_name());
        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            fbst.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    holder.img.setImageBitmap(bitmap);
                    Log.d(TAG,"Successfully fetch the image");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Image fetch unsuccessful"+e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.title.setText(art.getArt_title());

    }

    @Override
    public int getItemCount() {
        return artDetails.size();
    }

    public static class Gadapter extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title;
        public Gadapter(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.galimage);
            title = itemView.findViewById(R.id.galtitle);
        }
    }

}
