package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class ArtRecyclerViewAdapter extends RecyclerView.Adapter<ArtRecyclerViewAdapter.ArtAdapter> {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<ArtDetails> artDetails;
    StorageReference fbst;

    public ArtRecyclerViewAdapter(Context context, ArrayList<ArtDetails> artDetails,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.artDetails = artDetails;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ArtRecyclerViewAdapter.ArtAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.artwork_item,parent,false);
        return new ArtAdapter(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtRecyclerViewAdapter.ArtAdapter holder, int position) {

        ArtDetails art = artDetails.get(position);
        fbst = FirebaseStorage.getInstance().getReference("image/"+art.getImage_name());
        try {
            File localfile = File.createTempFile("tempfile",".jpeg");
            fbst.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    holder.art.setImageBitmap(bitmap);
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
    }

    @Override
    public int getItemCount() {
        return artDetails.size();
    }

    public static class ArtAdapter extends RecyclerView.ViewHolder{
        ImageView art;

        public ArtAdapter(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            art = itemView.findViewById(R.id.artwork);

            art.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null){
                        int pos = getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onclickItem(pos);
                        }
                    }
                }
            });
        }
    }

}
