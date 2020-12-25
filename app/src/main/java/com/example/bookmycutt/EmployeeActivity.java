package com.example.bookmycutt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class EmployeeActivity extends AppCompatActivity {
    public  static  final  String tag="EmployeeActivity";
    private TextView dob;
    private DatePickerDialog.OnDateSetListener mdate;
    int yearfinal;
    String date;
    Button nextb;
    private RadioButton radioButton;
    private RadioGroup rgroup;
    EditText ename,dobOFEmp,woking_hrs,exps;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ename=findViewById(R.id.name);
        dob=findViewById(R.id.number);
        woking_hrs=findViewById(R.id.timings);
        exps=findViewById(R.id.exp);
        rgroup=findViewById(R.id.radioGroup);
        int radioId=rgroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        nextb=findViewById(R.id.next);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                 int year1=cal.get(Calendar.YEAR);
                 int month1=cal.get(Calendar.MONTH);
                 int day1=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        EmployeeActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mdate,year1,month1,day1);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        mdate=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                date=dayOfMonth+"/"+month+"/"+year;
                yearfinal=year;
                dob.setText(date);
            }
        };

        nextb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ename.length()==0){
                    ename.setError("Enter Shop Name");
                }
                else if(woking_hrs.length()==0){
                    woking_hrs.setError("Enter No Employees");
                }
                else if(exps.length()==0){
                    exps.setError("Enter Contact no");
                }
                else if(yearfinal>=2003) {
                    dob.setError("Please Enter Valid Date Of Birth");
                    Toast.makeText(EmployeeActivity.this, "Employee's Age shoud be 18 Years Or more", Toast.LENGTH_SHORT).show();

                }
                else {
                    employee();
                }

            }
        });
    }

    public void employee(){
        Employees emp=(Employees) getApplicationContext();
        int count=emp.getCount();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid=user.getUid();
        String name=ename.getText().toString().trim();

        String exp=exps.getText().toString().trim();
        String workingHrs=woking_hrs.getText().toString().trim();
        count=count-1;
        emp.setCount(count);
        if (emp.getCount()==0){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Shop_details");
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("EmployeeName",name);
            hashMap.put("DateOFBirth",date);
            hashMap.put("Experiance(InYears)",exp);
            hashMap.put("WorkingHours",workingHrs);
            hashMap.put("Ratings",radioButton.getText().toString());
            reference.child(uid+"/EmployeesDetails/"+name).setValue(hashMap);
            startActivity(new Intent(EmployeeActivity.this,ServiceActivity.class));
            Toast.makeText(EmployeeActivity.this,"Please Add Service",Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Shop_details");
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("EmployeeName",name);
            hashMap.put("DateOFBirth",date);
            hashMap.put("Experiance(InYears)",exp);
            hashMap.put("WorkingHours",workingHrs);
            hashMap.put("Ratings",radioButton.getText().toString());
            reference.child(uid+"/EmployeesDetails/"+name).setValue(hashMap);
            startActivity(new Intent(EmployeeActivity.this,EmployeeActivity.class));
        }
    }
    public void checkButton(View v){
        int radioId=rgroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
    }
}
