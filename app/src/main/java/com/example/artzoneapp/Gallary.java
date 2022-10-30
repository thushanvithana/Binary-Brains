package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Gallary extends Fragment {

    View view;
    FirebaseAuth uauth;
    FirebaseFirestore fbfsuser;
    String uid;
    RecyclerView recyclerView;
    GallaryAdapter myadapter;
    ArrayList<ArtDetails> adetails;
    ArrayList<AppUsers> appUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallary, container, false);
        uauth = FirebaseAuth.getInstance();
        fbfsuser = FirebaseFirestore.getInstance();
        uid = uauth.getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.gallaryrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adetails =new ArrayList<ArtDetails>();
        appUsers = new ArrayList<AppUsers>();
        myadapter = new GallaryAdapter (getActivity(),adetails);
        recyclerView.setAdapter(myadapter);

        getUsers();



        return view;
    }

    private void getUsers(){

        fbfsuser.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.d(TAG,"FireStore error");
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){
                        appUsers.add(dc.getDocument().toObject(AppUsers.class));

                        //Toast.makeText(getActivity(), "user"+appUsers.size(), Toast.LENGTH_SHORT).show();
                    }


                }

                for(AppUsers a: appUsers){
                    getAllArt(a.getUID());
                }
            }
        });
    }


    private void getAllArt(String id){




            fbfsuser.collection("Users").document(id).collection("Artwork").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.d(TAG,"FireStore error");
                        return;
                    }

                    for(DocumentChange dc : value.getDocumentChanges()){

                        if(dc.getType() == DocumentChange.Type.ADDED){
                            adetails.add(dc.getDocument().toObject(ArtDetails.class));
                        }

                        myadapter.notifyDataSetChanged();
                    }
                }
            });
        }

}