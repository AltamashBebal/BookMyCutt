package com.example.bookmycutt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ShopActivity extends AppCompatActivity{
    Button next;
    RadioButton radioButton;
    RadioGroup rgroup;
    EditText shopname,noEmp,timing,working,service,locality;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop);
        locality=findViewById(R.id.name);
        next = findViewById(R.id.submit);
        rgroup=findViewById(R.id.radioGroup);
        shopname=findViewById(R.id.name2);
        noEmp=findViewById(R.id.no);
        timing=findViewById(R.id.timings);
        service=findViewById(R.id.number);
        int radioId=rgroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);

        working=findViewById(R.id.days);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopname.length()==0){
                    shopname.setError("Enter Shop Name");
                }
                else if(noEmp.length()==0){
                    noEmp.setError("Enter No Employees");
                }
                else if(timing.length()==0){
                    timing.setError("Enter Contact no");
                }
                else if(working.length()==0){
                    working.setError("Enter Working Days");
                }
                else {

                    shop();
                }

                }
        });
    }


public void shop(){
Employees emp=(Employees) getApplicationContext();

emp.setCount(Integer.parseInt((noEmp.getText().toString().trim())));
emp.setService_count(Integer.parseInt(service.getText().toString().trim()));
mAuth = FirebaseAuth.getInstance();
FirebaseUser user = mAuth.getCurrentUser();
String uid=user.getUid();
if (emp.getCount()==0){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Shop_details");
    HashMap<Object,String> hashMap = new HashMap<>();
    hashMap.put("Shopname",shopname.getText().toString().trim());
    hashMap.put("Timing",timing.getText().toString().trim());
    hashMap.put("WorkingDays",working.getText().toString().trim());
    hashMap.put("Type",radioButton.getText().toString());
    hashMap.put("NoOfEmployees",String.valueOf(emp.getCount()));
    hashMap.put("NoOfServices",String.valueOf(emp.getService_count()));
    hashMap.put("Locality",locality.getText().toString().trim());
    hashMap.put("uid",user.getUid());


    reference.child(uid).setValue(hashMap);
    startActivity(new Intent(ShopActivity.this,Odashboard.class));
}
else {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Shop_details");
    HashMap<Object,String> hashMap = new HashMap<>();
    hashMap.put("Shopname",shopname.getText().toString().trim());
    hashMap.put("Timing",timing.getText().toString().trim());
    hashMap.put("WorkingDays",working.getText().toString().trim());
    hashMap.put("Type",radioButton.getText().toString());
    hashMap.put("NoOfEmployees",String.valueOf(emp.getCount()));
    hashMap.put("NoOfServices",String.valueOf(emp.getService_count()));
    hashMap.put("Locality",locality.getText().toString().trim());
    hashMap.put("uid",user.getUid());
    reference.child(uid).setValue(hashMap);
    Toast.makeText(ShopActivity.this,"Please Add Employees",Toast.LENGTH_SHORT).show();
    startActivity(new Intent(ShopActivity.this,EmployeeActivity.class));
}
    }


    public void checkButton(View v){
        int radioId=rgroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
    }
}
