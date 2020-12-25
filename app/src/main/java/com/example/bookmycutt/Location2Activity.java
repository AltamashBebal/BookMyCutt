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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Location2Activity extends AppCompatActivity {
    private TextView selected;
    private Button next;
    private ListView ls;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location2);
        selected=findViewById(R.id.SelectedLocation);
        next=findViewById(R.id.button_continue);
        ls=findViewById(R.id.listview);
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(Location2Activity.this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);

        final Employees emp=(Employees) getApplicationContext();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emp.locPref==null){
                    selected.setError("Please Select Location First");
                    Toast.makeText(Location2Activity.this,"Please Select Location First",Toast.LENGTH_SHORT).show();
                }
                else
                startActivity(new Intent(Location2Activity.this,DashboardActivity.class));
            }
        });


        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("Shop_details");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String locality=dataSnapshot.child("Locality").getValue(String.class);
                if(!list.contains(locality)){
                    list.add(locality);
                    emp.LocationPrefrence[i]=locality;
                    adapter.notifyDataSetChanged();
                    i++;
                }
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
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emp.locPref=emp.LocationPrefrence[position];
                selected.setText(emp.locPref);

            }
        });


    }
}
