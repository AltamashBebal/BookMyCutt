package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestCustomeListView extends AppCompatActivity implements MyAdapterForusers.OnItemClickListener {
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<UserBookingDetails> list;
    MyAdapterForusers adapter;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custome_list_view);
//        Toast.makeText(TestCustomeListView.this, "This is Activity/", Toast.LENGTH_SHORT).show();
//        System.out.println("This is My Testing");

        final Employees emp = (Employees) getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.myrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(TestCustomeListView.this));
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("BookingsForOwner").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    int k = 0;
                    list = new ArrayList<UserBookingDetails>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    String data=dataSnapshot;
                        k++;
                        System.out.println(dataSnapshot1);
                        Toast.makeText(TestCustomeListView.this, "Keys::" + dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();

                        UserBookingDetails p = dataSnapshot1.getValue(UserBookingDetails.class);
                        list.add(p);
                    }
                    adapter = new MyAdapterForusers(TestCustomeListView.this, list, TestCustomeListView.this);
                    recyclerView.setAdapter(adapter);
                }
                else
                    Toast.makeText(TestCustomeListView.this,"NO bookings Found",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onItem(View v,int position) {
        Employees emp=(Employees) getApplicationContext();
        Toast.makeText(TestCustomeListView.this, "Location::" + emp.locations[position], Toast.LENGTH_SHORT).show();
        //        System.out.println("Po:"+position);
        emp.OwnerUid =(emp.uid[position]);
        emp.setShopname(emp.shopnames[position]);
        emp.setShopLocation(emp.locations[position]);
        startActivity(new Intent(TestCustomeListView.this,SelectService.class));
    }

}
