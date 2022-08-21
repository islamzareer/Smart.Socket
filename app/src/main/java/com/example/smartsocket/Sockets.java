package com.example.smartsocket;
import static android.app.PendingIntent.FLAG_IMMUTABLE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Sockets extends AppCompatActivity {
      private TextView userName, modee;
      private CircleImageView circleImageView;
      private FirebaseUser firebaseUser;
      private FirebaseAuth firebaseAuth;
      private DatabaseReference databaseReference;
      private DatabaseReference databaseReference2;
      private FirebaseDatabase firebase;
      private ActionMenuView mMenuView;
      private Button choose, add;
      private Spinner spin;
      private String selected = "", on = "false" , st;
      private List<String> list = new ArrayList<>();
      private Switch mode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sockets);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        FirebaseDatabase.getInstance().goOnline();

         ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
            userName = findViewById(R.id.username);
            circleImageView = findViewById(R.id.profileImage);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            choose = findViewById(R.id.viewDetails);
            add = findViewById(R.id.addSocket);
            mode = findViewById(R.id.OUTMode);
            modee = findViewById(R.id.statuss);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Sockets");

        databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UsersData usersData = snapshot.getValue(UsersData.class);
                    assert usersData!= null;
                    userName.setText(usersData.getUsername());
                    if (usersData.getImageURL().equals("default")){
                        circleImageView.setImageResource(R.drawable.ic_launcher_foreground);
                    }else{
                        Glide.with(getApplicationContext()).load(usersData.getImageURL()).into(circleImageView);
                    }
                    if(snapshot.child("OutMode").getValue().equals("true")){
                        st ="t";
                    }else{
                        st ="f";
                    }

                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> list2 = new ArrayList<>();
                            List<String> list3 = new ArrayList<>();
                            List<String> list4 = new ArrayList<>();

                            for (DataSnapshot child : snapshot.getChildren())
                            {
                                list.add(child.child("SocketIp").getValue().toString());
                                if(child.child("Status").getValue().toString().equals("true")){
                                    list2.add(child.child("SocketIp").getValue().toString());
                                    list2.add(child.getKey());
                                }
                                if(child.child("fire").getValue().toString().equals("true")){
                                    list3.add(child.child("SocketIp").getValue().toString());
                                    list3.add(child.getKey());
                                }
                                if(child.child("motion").getValue().toString().equals("true")){
                                    list4.add(child.child("SocketIp").getValue().toString());
                                    list4.add(child.getKey());
                                }
                            }
                            dataAdapter.notifyDataSetChanged();
                            notification(list2);
                            notification2(list3);
                            if(st.equals("t")) {
                                notification3(list4);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Sockets.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Sockets.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(Sockets.this, ChangePasswordActivity.class));
                }
            });

            if(on.equals("false")){
               mode.setChecked(false);
                modee.setText("OFF");
            }


        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode.isChecked()) {
                    on = "true";
                    databaseReference.child("OutMode").setValue("true");
                    modee.setText("ON");
                }
                else {
                    on = "false";
                    modee.setText("OFF");
                    databaseReference.child("OutMode").setValue("false");
                }
            }
        });

            spin = findViewById(R.id.spinner);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(dataAdapter);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selected = spin.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected.equals("")){
                    Toast.makeText(Sockets.this, "You don't choose any socket", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Sockets.this, Details.class);
                    String strName = selected;
                    intent.putExtra("socketIp", strName );
                    startActivity(intent);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sockets.this, AddSocketActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenuView = findViewById(R.id.menu_view);
        getMenuInflater().inflate(R.menu.profile_menu, mMenuView.getMenu());
        mMenuView.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.changePsw:
                    startActivity(new Intent(Sockets.this, ChangePasswordActivity.class));
                    break;
                case R.id.logout:
                    firebaseAuth.signOut();
                    startActivity(new Intent(Sockets.this, MainActivity.class));
                    finish();
                break;
            }
        return super.onOptionsItemSelected(item);
    }

    private void notification (List list) {
        String nss = Context.NOTIFICATION_SERVICE;
        Context ctx = this;

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(nss);
        notificationManager.cancelAll();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "str");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel("str", "ch", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel1);
        }
        builder.clearActions();
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("Socket is plugged")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false).setOngoing(true);

        for (int i = 0; i < list.size(); i+=2 ) {
            builder.setContentText(list.get(i).toString());
            Intent resultIntent = new Intent(Sockets.this, Details.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(Sockets.this);
            resultIntent.putExtra("socketIp", list.get(i+1).toString());
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(i,
                            PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);
            builder.setContentIntent(resultPendingIntent);
            notificationManager.notify(i+100 , builder.build());
        }
    }
    //*****************************
    private void notification2 (List list) {
        String ns = Context.NOTIFICATION_SERVICE;
        Context ctx = this;
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(ns);
       // notificationManager.cancelAll();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "strr");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("strr", "chh", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        builder.clearActions();
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("There's Smoke!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false).setOngoing(true);

        for (int i = 0; i < list.size(); i+=2 ) {
            builder.setContentText(list.get(i).toString());
            Intent resultIntent = new Intent(Sockets.this, Details.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(Sockets.this);

            resultIntent.putExtra("socketIp", list.get(i+1).toString());
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(i,
                            PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);
            builder.setContentIntent(resultPendingIntent);

            notificationManager.notify(i+10 , builder.build());
        }
    }
    //*******************************
    private void notification3 (List list) {
        String ns = Context.NOTIFICATION_SERVICE;
        Context ctx = this;
        NotificationManager notificationManager3 = (NotificationManager) ctx.getSystemService(ns);
        //notificationManager3.cancelAll();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "strrr");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel2 = new NotificationChannel("strrr", "chhh", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager3.createNotificationChannel(channel2);
        }
        builder.clearActions();
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("There's Motion!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false);
        for (int i = 0; i < list.size(); i+=2 ) {
            builder.setContentText(list.get(i).toString());
            Intent resultIntent = new Intent(Sockets.this, Details.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(Sockets.this);
            resultIntent.putExtra("socketIp", list.get(i+1).toString());
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(i,
                            PendingIntent.FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE);
            builder.setContentIntent(resultPendingIntent);
            notificationManager3.notify(i+40 , builder.build());
        }
    }









    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}