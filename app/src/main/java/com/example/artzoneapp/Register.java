package com.example.artzoneapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView logintextbtn,errortxt;
    EditText email,password,re_ent_pass;
    Button regbtn;
    RadioButton guestsel,artistsel;
    FirebaseAuth regauth;
    FirebaseFirestore fbfstore;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        logintextbtn = findViewById(R.id.text_login_btn);
        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_password);
        re_ent_pass = findViewById(R.id.reg_reenter_ps);
        regbtn = findViewById(R.id.reg_btn);
        guestsel = findViewById(R.id.guest_select);
        artistsel = findViewById(R.id.artist_select);
        errortxt = findViewById(R.id.error_txt);
        regauth = FirebaseAuth.getInstance();
        fbfstore = FirebaseFirestore.getInstance();

        guestsel.setChecked(true);

        logintextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLoginpage();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressmassage = new ProgressDialog(Register.this);
                progressmassage.setTitle("Processing");
                progressmassage.show();

                String useremail = email.getText().toString();
                String userpass = password.getText().toString();
                String reuserpass = re_ent_pass.getText().toString();
                String usercat;
                if(guestsel.isChecked()){
                    usercat = "Guest";
                }else{
                    usercat = "Artist";
                }

                if(!userpass.equals(reuserpass)){
                    errortxt.setText("Passwords do not match!");
                }else if(TextUtils.isEmpty(useremail)&&TextUtils.isEmpty(userpass)&&TextUtils.isEmpty(reuserpass)){
                    errortxt.setText("Give your credentials correctly!");
                }else if(userpass.length()<8){
                    errortxt.setText("Password must contain 8 characters or above!");
                }else{
                    regauth.createUserWithEmailAndPassword(useremail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                userid = regauth.getCurrentUser().getUid();
                                DocumentReference documentReference = fbfstore.collection("Users").document(userid);
                                Map<String,Object> user = new HashMap<>();
                                user.put("UID",userid);
                                user.put("Email",useremail);
                                user.put("UserType",usercat);
                                user.put("userName","My name");

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"Details are added to firestore successfully UserID: "+userid);
                                        if(progressmassage.isShowing()){
                                            progressmassage.dismiss();
                                        }

                                    }
                                }).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        Log.d(TAG,"Details not added UserID: "+userid);
                                        if(progressmassage.isShowing()){
                                            progressmassage.dismiss();
                                        }
                                    }
                                });

                                switchtologin();
                                finish();

                            }
                        }
                    });
                }
            }
        });
    }




    private void switchtologin(){
        Toast.makeText(this,"Registered successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,Login.class));
    }












    private void gotoLoginpage(){
        Intent intent =new Intent(this,Login.class);
        startActivity(intent);
    }
}