package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddNewService extends AppCompatActivity {
    private ImageView imageView;
    private TextView sname,sprice,stime;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);
        imageView=findViewById(R.id.serviceimage);
        sname=findViewById(R.id.servicename);
        stime=findViewById(R.id.timings);
        sprice=findViewById(R.id.no);
        add=findViewById(R.id.submit);
        Picasso.get().load(R.drawable.flogo).into(imageView);

//        imageView.
        Toast.makeText(AddNewService.this,"If you entered the existing details then that services will get update",Toast.LENGTH_LONG).show();

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("Shop_details").child(user.getUid()).child("Services");
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> updates = new HashMap<String,Object>();
                        updates.put("Name", sname.getText().toString().trim());
                        updates.put("Price",sprice.getText().toString().trim());
                        updates.put("TimeRequird",stime.getText().toString().trim());
                        mref.child(sname.getText().toString().trim()).updateChildren(updates);
                        Toast.makeText(AddNewService.this,"New Service Added Succesfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddNewService.this,Odashboard.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
