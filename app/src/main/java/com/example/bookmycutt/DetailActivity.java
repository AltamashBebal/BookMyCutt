package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    DatabaseReference mref;
    FirebaseAuth mAuth;
    Button next;
    ListView ls;
    FirebaseDatabase firebaseDatabase;
    private TextView uname,staffd,timedAndDate,contactd,genderd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        uname=findViewById(R.id.textView7);
        genderd=findViewById(R.id.textView12);
        ls=findViewById(R.id.listview);
        next=findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this,Odashboard.class));
            }
        });

        mAuth= FirebaseAuth.getInstance();
        staffd=findViewById(R.id.textView3);
        timedAndDate=findViewById(R.id.textView13);
        contactd=findViewById(R.id.textView14);
        final FirebaseUser user=mAuth.getCurrentUser();
        final Employees emp=(Employees) getApplicationContext();

        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(DetailActivity.this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);

        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Owners").child(user.getUid()).child(emp.finalUserUid).child(emp.userkey).child("Services");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String sname=dataSnapshot.child("ServiceName").getValue(String.class);
//                String time=dataSnapshot.child("Time").getValue(String.class);
                list.add(sname);
                adapter.notifyDataSetChanged();
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




        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Owners").child(user.getUid()).child(emp.finalUserUid).child(emp.userkey);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String date=dataSnapshot.child("Status").getValue(String.class);

                String date=dataSnapshot.child("Date").getValue(String.class);
                String time=dataSnapshot.child("Time").getValue(String.class);
                String  employee=dataSnapshot.child("SelectedEmployee").getValue(String.class);
                staffd.setText("Selected Staff:"+employee);
                timedAndDate.setText("Time:"+time+" Date:"+date);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mref=FirebaseDatabase.getInstance().getReference().child("Users").child(emp.finalUserUid);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue(String.class);
                String contact=dataSnapshot.child("phone").getValue(String.class);
                String gender=dataSnapshot.child("gender").getValue(String.class);

                genderd.setText("Gender:"+gender);
                uname.setText("Customer's Name:"+name);
                contactd.setText("Contact No:"+contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
