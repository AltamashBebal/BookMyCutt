package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChooseLocation extends AppCompatActivity {
    private ListView listView;
    private TextView location,shop;
    private Button select;
    DatabaseReference mref;
    int i=0,j=0;
    String demo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Employees emp=(Employees) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        listView=findViewById(R.id.list_item);
//         Toast.makeText(ChooseLocation.this, "Shop After::"+emp.shopnames[0] ,Toast.LENGTH_SHORT).show();
        emp.resetForBooking();

        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(ChooseLocation.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
//        mref= FirebaseDatabase.getInstance().getReference().child("Shop_details");
        mref= FirebaseDatabase.getInstance().getReference().child("Shop_details");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String vs=dataSnapshot.child("Shopname").getValue(String.class);
                String vs2=dataSnapshot.child("Locality").getValue(String.class);
                String ui=dataSnapshot.child("uid").getValue(String.class);
                emp.shopnames[i]=vs;
                emp.locations[i]=vs2;
                emp.uid[i]=ui;
//                String value=dataSnapshot.getValue(Owners.class).toString();
//                String value2=dataSnapshot.getValue(Owners.class).getOwnername();
                String vs3="Haircut Price:150 Time=30 min ";
                list.add(vs);
//                emp.(value2);
                adapter.notifyDataSetChanged();
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
//                     Toast.makeText(ChooseLocation.this, ""+ list., Toast.LENGTH_SHORT).show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emp.OwnerUid =(emp.uid[position]);
                emp.setShopname(emp.shopnames[position]);
                emp.setShopLocation(emp.locations[position]);
                startActivity(new Intent(ChooseLocation.this,SelectService.class));

            }
        });

    }


}


