package com.example.smartsocket;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.Objects;

public class Details extends AppCompatActivity {
    private TextView temp, hum, socketname, status, otherinfo, energy, power;
    private MaterialEditText socketpower;
    private LottieAnimationView animationView;
    private Button  set;
    private Switch statusSwitch;
    private Boolean switchState = false ;
    private String txt_status;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Selected socket details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Details.this, Sockets.class));
            }
        });
        FirebaseDatabase.getInstance().goOnline();
        String socketIp;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                socketIp= null;
            } else {
                socketIp= extras.getString("socketIp");
            }
        } else {
            socketIp= (String) savedInstanceState.getSerializable("socketIp");
        }

        temp = findViewById(R.id.temp);
        hum = findViewById(R.id.hum);

        otherinfo = findViewById(R.id.socketinfo);
        energy = findViewById(R.id.power);
        power = findViewById(R.id.power7);
        status = findViewById(R.id.status);
        statusSwitch = findViewById(R.id.statusSwitch);
        socketname = findViewById(R.id.socketname);
        socketpower = findViewById(R.id.socketpower);
        animationView = findViewById(R.id.animationView);
        status = findViewById(R.id.status);
        set = findViewById(R.id.set);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Sockets").child(socketIp);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String po = socketpower.getText().toString();
                if(po.isEmpty()){
                    Toast.makeText(Details.this, Objects.requireNonNull("Set the connected device power!"), Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("Connected Power").setValue(po);
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                temp.setText(snapshot.child("Temperature").getValue().toString());
                hum.setText(snapshot.child("Humidity").getValue().toString());
                energy.setText(snapshot.child("Energy").getValue().toString());
                power.setText(snapshot.child("Power consumption").getValue().toString());
                socketname.setText(snapshot.child("SocketName").getValue().toString());
                txt_status= snapshot.child("Status").getValue().toString();
                status.setText(txt_status);
                otherinfo.setText(snapshot.child("otherInformation").getValue().toString());
                if(txt_status.equals("true")){
                    switchState = true;
                    status.setText("ON");
                    statusSwitch.setChecked(true);
                } else{
                    switchState = false;
                    status.setText("OFF");
                    statusSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        statusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusSwitch.isChecked()) {
                    switchState = true;
                    databaseReference.child("Status").setValue(true);
                    status.setText("ON");
                }
                else {
                    switchState = false;
                    status.setText("OFF");
                    databaseReference.child("Status").setValue(false);
                }
            }
        });

        animationView.playAnimation();
    }

}