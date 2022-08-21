package com.example.smartsocket;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MaterialEditText email, password;
    private TextView forgetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().goOnline();
        Intent intentt = new Intent("com.android.ServiceStopped");
        sendBroadcast(intentt);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar);
        Button registerBtn = findViewById(R.id.register);
        Button logBtn = findViewById(R.id.login);
        forgetPassword = findViewById(R.id.forget);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, Sockets.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String txt_password = password.getText().toString();
                 String txt_email = email.getText().toString();
                if (TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(MainActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    login(txt_email, txt_password);
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
            }
        });
    }

    private void login(String txt_email, String txt_password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, Sockets.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{

                    Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}