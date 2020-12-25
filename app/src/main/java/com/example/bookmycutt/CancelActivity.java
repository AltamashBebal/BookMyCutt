package com.example.bookmycutt;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CancelActivity extends AppCompatActivity {
    DatabaseReference mref;
    FirebaseAuth mAuth;

    private TextView total,sname;
    private Button calcel;
    private ListView ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        total=findViewById(R.id.total);
        sname=findViewById(R.id.salonsName);
        calcel=findViewById(R.id.button_cancel);
        final Employees emp=(Employees) getApplicationContext();
        ls=findViewById(R.id.listview);
        total.setText("Rs:"+emp.TotalCalcel);
        sname.setText(emp.ShopCancel);
        mAuth=FirebaseAuth.getInstance();

        final FirebaseUser user= mAuth.getCurrentUser();
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(CancelActivity.this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);
        list.add("No Of Servicec Selected"+emp.ServiceCalcel);
        mref=FirebaseDatabase.getInstance().getReference().child(user.getUid());

//        calcel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference reference = database.getReference("Bookings").child("Users").child(user.getUid());
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        HashMap<Object,String> hashMap = new HashMap<>();
//                        hashMap.put("Status","Canceled");
//                        reference.child(emp.key).setValue(hashMap);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                final DatabaseReference reference3 = database.getReference("Bookings").child("Owners").child(emp.OwnerCalncel).child(user.getUid());
//                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        HashMap<Object,String> hashMap4 = new HashMap<>();
//                        hashMap4.put("Status","Canceled");
//                        reference3.child(emp.key).setValue(hashMap4);
//                        startActivity(new Intent(CancelActivity.this,BookingCancelledActivity.class));
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });

        calcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Bookings").child("Users").child(user.getUid()).child(emp.key);
                Map<String, Object> updates = new HashMap<String,Object>();
//                updates.put("email", );
                updates.put("Status", "Canceled");
                ref.updateChildren(updates);

                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Bookings").child("Owners").child(emp.OwnerCalncel).child(user.getUid()).child(emp.key);
                Map<String, Object> updates2 = new HashMap<String,Object>();
//                updates.put("email", );
                updates2.put("Status", "Canceled");
                ref2.updateChildren(updates2);
                startActivity(new Intent(CancelActivity.this,BookingCancelledActivity.class));

            }
        });
    }
}
