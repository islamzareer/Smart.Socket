package com.example.smartsocket;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.rengwuxian.materialedittext.MaterialEditText;
public class ForgetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView resetState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this, MainActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        MaterialEditText emailAddress = findViewById(R.id.email);
        resetState = findViewById(R.id.resetText);
        Button resetPasswordBtn = findViewById(R.id.sendEmail);
        progressBar = findViewById(R.id.progressBar);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.fetchSignInMethodsForEmail(emailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            resetState.setText("This is not a registered email! you can create account");
                        } else {
                            mAuth.sendPasswordResetEmail(emailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        resetState.setText("An email to reset  has been sent to your email address");
                                    } else {
                                        resetState.setText(task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}