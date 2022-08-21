package com.example.smartsocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class AddSocketActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MaterialEditText Socket_name, Socket_ip, other_information;
    private ProgressBar progressBar;
    private Button addSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_socket);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSocketActivity.this, Sockets.class));
            }
        });
        Socket_name = findViewById(R.id.sName);
        Socket_ip = findViewById(R.id.socketIp);
        other_information = findViewById(R.id.otherInfo);
        progressBar = findViewById(R.id.progressBar);
        addSocket = findViewById(R.id.addsocket2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Sockets");
        addSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("SocketName",Socket_name.getText().toString());
                hashMap.put("SocketIp",Socket_ip.getText().toString());
                hashMap.put("otherInformation",other_information.getText().toString());
                hashMap.put("Temperature",0);
                hashMap.put("Humidity",0);
                hashMap.put("Power consumption",0);
                hashMap.put("Status",false);
                hashMap.put("fire",false);
                hashMap.put("motion",false);
                hashMap.put("Connected Power",0);
                hashMap.put("Energy",0);

                databaseReference.child(Socket_ip.getText().toString()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(AddSocketActivity.this, Sockets.class);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds : snapshot.getChildren()) {
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(AddSocketActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

}}