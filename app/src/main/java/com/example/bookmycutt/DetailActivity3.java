package com.example.bookmycutt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity3 extends AppCompatActivity {
DatabaseReference mref;
FirebaseAuth mAuth;
FirebaseDatabase firebaseDatabase;
private TextView uname,staffd,genderd,timedAndDate,dated,contactd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);


        uname=findViewById(R.id.topview);
        staffd=findViewById(R.id.textView3);
        genderd=findViewById(R.id.textView3);
        timedAndDate=findViewById(R.id.textView13);
//        contactd=findViewById()
//        final FirebaseUser user=mAuth.getCurrentUser();


//        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Owners").child(user.getUid()).child(emp.finalUserUid);

    }
}
