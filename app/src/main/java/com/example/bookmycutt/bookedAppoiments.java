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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class bookedAppoiments extends AppCompatActivity {
private ListView ls;
private FirebaseAuth mAuth;
private DatabaseReference mref;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_appoiments);
        ls=findViewById(R.id.listview);
        mAuth=FirebaseAuth.getInstance();
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(bookedAppoiments.this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);
        final Employees emp=(Employees) getApplicationContext();
        final FirebaseUser user=mAuth.getCurrentUser();
        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Users").child(user.getUid());
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String sname=dataSnapshot.child("ShopName").getValue(String.class);
                Toast.makeText(bookedAppoiments.this,""+sname,Toast.LENGTH_SHORT).show();
                String  total=dataSnapshot.child("totalAmount").getValue(String.class);
                String noOfServices=dataSnapshot.child("noOfServiceSelected").getValue(String.class);
                emp.ServiceNameForCancel[i]=noOfServices;
                emp.ShopNameForCancel[i]=sname;
                emp.TotalForCancel[i]=total;
                list.add(sname);
                emp.i++;
                i++;
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
        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Users");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid())){
                    emp.i=1;
                }
                else {
                    if(emp.i==0){
                        list.add("No bookings Found");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emp.ServiceCalcel=emp.ServiceNameForCancel[position];
                emp.ServiceCalcel=emp.ServiceNameForCancel[position];
                emp.TotalCalcel=emp.TotalForCancel[position];
                startActivity(new Intent(bookedAppoiments.this,ChooseLocation.class));
            }
        });
    }
}
