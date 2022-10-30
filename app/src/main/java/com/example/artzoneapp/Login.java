package com.example.artzoneapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView signupbtntxt,login_error;
    EditText userlemail,userlpass;
    Button loginbtn;
    FirebaseAuth lauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupbtntxt = findViewById(R.id.register_btn_txt);
        userlemail = findViewById(R.id.log_email);
        userlpass = findViewById(R.id.log_password);
        loginbtn = findViewById(R.id.login_btn);
        login_error = findViewById(R.id.login_error_txt);
        lauth = FirebaseAuth.getInstance();

        signupbtntxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotosignup();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressmassage = new ProgressDialog(Login.this);
                progressmassage.setTitle("Processing");
                progressmassage.show();

                String uemail = userlemail.getText().toString();
                String upass = userlpass.getText().toString();
                if(TextUtils.isEmpty(uemail)&&TextUtils.isEmpty(upass)){
                    login_error.setText("Enter your login details!");
                }else{
                    lauth.signInWithEmailAndPassword(uemail,upass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(progressmassage.isShowing()){
                                    progressmassage.dismiss();
                                }
                                Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,Home_to_Profile.class));
                                finish();
                            }else{
                                login_error.setText("Re-enter login details correctly and login again!");
                                Toast.makeText(Login.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = lauth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(Login.this,Home_to_Profile.class));

        }
    }

    private void gotosignup(){
        Intent intent =new Intent(this,Register.class);
        startActivity(intent);
    }
}