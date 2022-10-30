package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class List_fragment extends Fragment implements ListRinterface{

    View view ;
    RecyclerView recyclerView ;
    ArrayList<ItemRow> itemRowTittleArray;
    FirebaseFirestore fbfs;
    ItemRowAdapter adapter;
    ImageButton listcreate;
    String listname1;
    ConstraintLayout inputsec;
    TextView ok,cancle;
    EditText lnam;
    String uid;
    FirebaseAuth uauth;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.myRecyclerView);
        listcreate = view.findViewById(R.id.listcreate);
        fbfs = FirebaseFirestore.getInstance();
        itemRowTittleArray = new ArrayList<>();
        inputsec = view.findViewById(R.id.inputsec);
        ok = view.findViewById(R.id.okbtn);
        cancle = view.findViewById(R.id.canclebtn);
        lnam =  view.findViewById(R.id.listnameinput);
        uauth = FirebaseAuth.getInstance();
        uid = uauth.getCurrentUser().getUid();


        adapter = new ItemRowAdapter(getActivity() , itemRowTittleArray,this);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fbfs.collection("Users").document(uid).collection("Lists").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.d(TAG,"Firstore error");
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        itemRowTittleArray.add(dc.getDocument().toObject(ItemRow.class));
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });


        listcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputsec.setVisibility(View.VISIBLE);

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listname1 = lnam.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
                Date now = new Date();
                String filename = formatter.format(now);

                id = listname1+"_"+filename;

                if(listname1.isEmpty()){

                    Toast.makeText(getActivity(),"Give a list name",Toast.LENGTH_SHORT).show();


                }else{
                    DocumentReference documentReference = fbfs.collection("Users").document(uid).collection("Lists").document(id);
                    Map<String,Object> list = new HashMap<>();
                    list.put("id",id);
                    list.put("image","demoimagename");
                    list.put("name",listname1);
                    list.put("userid",uid);

                    documentReference.set(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(),"List created Successfully",Toast.LENGTH_SHORT).show();
                            inputsec.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"List not Created",Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }
        });


        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputsec.setVisibility(View.INVISIBLE);
            }
        });



        return view ;
    }

    @Override
    public void onlistitemClick(int position) {
        Intent intent = new Intent(getActivity(),List_Item.class);
        intent.putExtra("listname",itemRowTittleArray.get(position).getName());
        intent.putExtra("listid",itemRowTittleArray.get(position).getId());
        startActivity(intent);
    }


}