package com.example.smartsocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    private MaterialEditText oldPsw, newPsw, confirmPsw;
    private Button changePsw;
    private FirebaseAuth firebaseAuth ;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        oldPsw = findViewById(R.id.oldPassword);
        newPsw = findViewById(R.id.newPassword);
        confirmPsw = findViewById(R.id.confirmPassword);
        changePsw = findViewById(R.id.changePassword);
        progressBar = findViewById(R.id.progressBar);
        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String old_psw = oldPsw.getText().toString();
                final String new_psw = newPsw.getText().toString();
                final String con_psw = confirmPsw.getText().toString();
                if(old_psw.isEmpty() || new_psw.isEmpty() || con_psw.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }else if(new_psw.length()<6){
                    Toast.makeText(ChangePasswordActivity.this, "New password length should be 6 characters or more!", Toast.LENGTH_SHORT).show();
                }else if(!new_psw.equals(con_psw)){
                    Toast.makeText(ChangePasswordActivity.this, "Confirm password does not match new password!", Toast.LENGTH_SHORT).show();
                }else{
                    changePass(old_psw,new_psw);
                }
            }
        });
    }

    private void changePass(String old_psw, String new_psw) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),old_psw);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updatePassword(new_psw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ChangePasswordActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangePasswordActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}