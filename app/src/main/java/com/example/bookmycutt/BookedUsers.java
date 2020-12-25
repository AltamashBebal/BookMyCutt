package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookedUsers extends AppCompatActivity {
private ListView ls;
    private FirebaseAuth mAuth;
    int i=0;
    private DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_users);
        ls=findViewById(R.id.listview);
        mAuth= FirebaseAuth.getInstance();
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(BookedUsers.this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);
        final Employees emp=(Employees) getApplicationContext();
        Toast.makeText(BookedUsers.this,"Uid::"+emp.finalUserUid,Toast.LENGTH_SHORT).show();
        final FirebaseUser user=mAuth.getCurrentUser();
//        Toast.makeText(BookedUsers.this,"User::"+user.getEmail(),Toast.LENGTH_SHORT).show();

        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Owners").child(user.getUid()).child(emp.finalUserUid);
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String time=dataSnapshot.child("Time").getValue(String.class);
                String Date=dataSnapshot.child("Date").getValue(String.class);
                list.add("Date:"+Date+" Time:"+time);
                adapter.notifyDataSetChanged();
                emp.Keys[i]=dataSnapshot.getKey();
                i++;

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();

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


//
//        mref.child(emp.uidForBooked).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String sname=dataSnapshot.child("ShopName").getValue(String.class);
//                list.add(sname);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            emp.userkey=emp.Keys[position];
            startActivity(new Intent(BookedUsers.this, DetailActivity.class));

        }
    });
    }

}
