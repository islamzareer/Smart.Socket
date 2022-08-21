package com.example.smartsocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private MaterialEditText username, email, password, mobile;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        registerBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = username.getText().toString();
                final String txt_password = password.getText().toString();
                final String txt_mobile = mobile.getText().toString();
                final String txt_email = email.getText().toString();
                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)
                        || TextUtils.isEmpty(txt_mobile)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    register(user_name, txt_email, txt_password, txt_mobile);
                }
            }

            private void register(String user_name, String txt_email, String txt_password, String txt_mobile) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser rUser = firebaseAuth.getCurrentUser();
                            assert rUser != null;
                            String userID = rUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("userid",userID);
                            hashMap.put("username",user_name);
                            hashMap.put("password",txt_password);
                            hashMap.put("email",txt_email);
                            hashMap.put("mobile",txt_mobile);
                            hashMap.put("imageURL", "default");
                            hashMap.put("OutMode",false);
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}