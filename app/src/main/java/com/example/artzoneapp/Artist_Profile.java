package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class Artist_Profile extends Fragment implements RecyclerViewInterface {

    View view;
    TextView uname;
    Button uploadbtn;
    FirebaseAuth uauth;
    FirebaseFirestore fbfsuser;
    String uid;
    RecyclerView recyclerView;
    ArtRecyclerViewAdapter myadapter;
    ArrayList<ArtDetails> adetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_artist__profile, container, false);
        uploadbtn = view.findViewById(R.id.img_upload_btn);
        uname = view.findViewById(R.id.username);
        uauth = FirebaseAuth.getInstance();
        fbfsuser = FirebaseFirestore.getInstance();
        uid = uauth.getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.myartrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adetails =new ArrayList<ArtDetails>();
        myadapter = new ArtRecyclerViewAdapter(getActivity(),adetails,this);
        recyclerView.setAdapter(myadapter);
        DocumentReference documentReference = fbfsuser.collection("Users").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                uname.setText(value.getString("userName"));
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotouplodpage();
            }
        });
        
        artShowRecycler();
        
        
        return view;
    }

    private void artShowRecycler() {

        ProgressDialog progressmassage = new ProgressDialog(getActivity());
        progressmassage.setTitle("processing.....");
        progressmassage.show();

        fbfsuser.collection("Users").document(uid).collection("Artwork").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    if(progressmassage.isShowing()){
                        progressmassage.dismiss();
                    }
                    Log.d(TAG,"FireStore error");
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){
                        adetails.add(dc.getDocument().toObject(ArtDetails.class));
                    }

                    myadapter.notifyDataSetChanged();

                    if(progressmassage.isShowing()){
                        progressmassage.dismiss();
                    }
                }
            }
        });
    }

    private void gotouplodpage(){
        Intent intent = new Intent(getActivity(),Artist_upload_art_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onclickItem(int position) {

        Intent intent = new Intent(getActivity(),Display_image_Artist_activity.class);


        intent.putExtra("Aid",adetails.get(position).getAID());
        intent.putExtra("ArtTitle",adetails.get(position).getArt_title());
        intent.putExtra("ArtDescription",adetails.get(position).getArt_description());
        intent.putExtra("ImageName",adetails.get(position).getImage_name());

        startActivity(intent);


    }
}