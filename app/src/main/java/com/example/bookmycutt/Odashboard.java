package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.util.Calendar.*;

public class Odashboard extends AppCompatActivity {
    private com.google.firebase.auth.FirebaseAuth FirebaseAuth;
    private NotificationManagerCompat notificationManagerCompat;
    public final static String  CHANNEL_ID="personal_notification";
    public final int   NOTIFICATION_ID=001;
    int i=0;
    public final static long[]  pattern={100,300,300,300};
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odashboard);
//            startActivity(new Intent(Odashboard.this,TestCustomeListView.class));
            FirebaseAuth=FirebaseAuth.getInstance();
            final FirebaseUser user=FirebaseAuth.getCurrentUser();
            Toolbar toolbar =findViewById(R.id.profiletoolbar);
            setSupportActionBar(toolbar);
            BottomNavigationView navigationView = findViewById(R.id.navigation);
            navigationView.setOnNavigationItemSelectedListener(selectedListener);

            final Employees emp=(Employees) getApplicationContext();


            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Channel_1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Hello");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            notificationManagerCompat=NotificationManagerCompat.from(this);
            final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Notifications").child(user.getUid());

            mref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String status=dataSnapshot.child("Status").getValue(String.class);
                    String name=dataSnapshot.child("Name").getValue(String.class);
                    String date=dataSnapshot.child("Date").getValue(String.class);
                    String time=dataSnapshot.child("Time").getValue(String.class);

                    if(status.equals("Active")){
                        Notification notification=new NotificationCompat.Builder(Odashboard.this,CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_event_available)
                                .setContentTitle(""+name+" Has Booked Appontment")
                                .setContentText("On: "+date+" At: "+time)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setVibrate(pattern)
                                .build();
                        notificationManagerCompat.notify(i+1,notification);
                        Map<String, Object> updates2 = new HashMap<String,Object>();
//                updates.put("

                        updates2.put("Status", "Deactive");

                        mref.child(dataSnapshot.getKey()).updateChildren(updates2);

                    }
                    i++;
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


                OhomeFragment fragment1 = new OhomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();

    }

                private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.Ohome:
                    //home fragment transaction
                    OhomeFragment fragment1 = new OhomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.content,fragment1,"");
                    ft1.commit();
                    return true;
                case R.id.Oprofile:
                    //profile fragment transaction
                    //home fragment transaction
                    OprofileFragment fragment3 = new OprofileFragment();
                    FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                    ft3.replace(R.id.content,fragment3,"");
                    ft3.commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }
    private void checkuserstatus() {
        FirebaseUser user=FirebaseAuth.getCurrentUser();
        if (user!=null){
            //display.setText(user.getEmail());
            //  reference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            String uid=FirebaseAuth.getCurrentUser().getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Shop_details").child(uid);
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue()==null) {
                        // The child doesn't exist
                        Toast.makeText(Odashboard.this, "Please Complete Shop Details", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Odashboard.this,ShopActivity.class));
                    }
                    else{
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            startActivity(new Intent(Odashboard.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            FirebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
