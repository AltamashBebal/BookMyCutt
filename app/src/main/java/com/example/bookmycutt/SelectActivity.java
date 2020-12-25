package com.example.bookmycutt;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.MONDAY;

public class SelectActivity extends AppCompatActivity {
    private TextView datetext,timeView,selected,sname,location;
    private ListView staff;
    private Button next;
    String dateFinal,timefinal;
    int firstPart;
    Context mcontext=this;
    DatabaseReference mref;
    String[] secondPart;
    String s;
    String[] parts;
    String ampm;
    int newHour=0;


    int i=0,isSelected=0;
    private TextInputEditText datef,timef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        datetext=findViewById(R.id.textView14);
        sname=findViewById(R.id.textView7);
        location=findViewById(R.id.textView3);
        timeView=findViewById(R.id.textView15);
        staff=findViewById(R.id.radioGroup);
        selected=findViewById(R.id.textVieww);
        datef=findViewById(R.id.Selectdate);
        timef=findViewById(R.id.Selecttime);
        next=findViewById(R.id.next);


        final Employees emp=(Employees) getApplicationContext();
        sname.setText("Salon:"+emp.getShopname());
        location.setText("Locality:"+emp.getShopLocation());


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar startDate = Calendar.getInstance();

                if(datef.length()==0){
                    datef.setError("Please Select date");
                }
                else if(timef.length()==0){
                    timef.setError("Please select time");
                }
                else if(emp.SelectedEmployee==null){
                    selected.setError("Please Seleect Enployee preference");
                    Toast.makeText(SelectActivity.this, "Please Seleect Enployee preference", Toast.LENGTH_SHORT).show();

                }
                else{
                    startDate.set(emp.year,emp.month-1,emp.day);
                    int da=startDate.get(startDate.DAY_OF_WEEK);

                    s = emp.timeofBooking;
                    parts = s.split(":"); //returns an array with the 2 parts
                    firstPart = Integer.parseInt(parts[0]);
                    secondPart = parts[1].split(" ");
                    ampm = secondPart[1];
                    if(ampm.equals("PM")) {
                        if (firstPart== 12) {
                            newHour = firstPart;
                        }
                        else {
                            newHour=firstPart+12;
                        }
                    }
                    if(da==MONDAY){
                        Toast.makeText(SelectActivity.this, "Shop Is Closed On Monday", Toast.LENGTH_SHORT).show();
                        datef.setError("Shop Is Closed On Monday");
                    }
                    else if((firstPart<=9 && ampm.equals("AM")) || (newHour>=22 && ampm.equals("PM"))) {
//                        || ((Integer.parseInt(firstPart)>=9 &&ampm=="pm")))
                        Toast.makeText(SelectActivity.this,"Sorry Shop is closed at selected time", Toast.LENGTH_SHORT).show();
                        timef.setError("Sorry Shop is closed at selected time");
                    }
                    else {
                        startActivity(new Intent(SelectActivity.this, SummaryActivity.class));
                    }
                }
            }
        });
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectActivity.this,android.R.layout.simple_list_item_1,list);
        staff.setAdapter(adapter);
//        mref= FirebaseDatabase.getInstance().getReference().child("Shop_details");
        mref= FirebaseDatabase.getInstance().getReference().child("Shop_details").child(emp.OwnerUid).child("EmployeesDetails");
        list.add("No Preference");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String ename=dataSnapshot.child("EmployeeName").getValue(String.class);
                String ratings=dataSnapshot.child("Ratings").getValue(String.class);
                String details="Name:"+ename+" Ratings:"+ratings;
                emp.selectedEmployees[i]=ename;
                list.add(details);
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

        staff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isSelected++;
                if(position==0){
                    selected.setText("SELECTED EMPLOYEE : No Preferencce");
                    emp.SelectedEmployee="No Preferencce";
                }
                else{
                selected.setText("SELECTED EMPLOYEE : "+emp.selectedEmployees[position-1]);
                    emp.SelectedEmployee=emp.selectedEmployees[position-1];
//                    Toast.makeText(SelectActivity.this,"Selected::"+position,Toast.LENGTH_SHORT).show();
            }}
        });
    }

    public  void datePicker(View v){
        final Employees emp=(Employees) getApplicationContext();
        final Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        final int date=cal.get(Calendar.DATE);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                dateFinal=dayOfMonth+"/"+month+ "/"+year;
                datef.setText(dateFinal);
                emp.year=year;
                emp.month=month;
                emp.day=dayOfMonth;
                cal.set(emp.year, emp.month, emp.day);
                emp.date=dateFinal;

            }
        },year,month,date);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public void timePicker(View v) {
final Employees emp=(Employees) getApplicationContext();
        Calendar cal=Calendar.getInstance();
         final int  hour=cal.get(Calendar.HOUR_OF_DAY);
         int mMinute=cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String ampm;
                {
                    if(hourOfDay>=12){
                        ampm="PM";
                        if(hourOfDay==12){
                            hourOfDay=hourOfDay+12;
                            timefinal = hourOfDay+ ":" + minute + " " + ampm;
                        }
                        {
                            timefinal = hourOfDay - 12 + ":" + minute + " " + ampm;
                        }
                    }
                    else {
                        ampm="AM";
                        if(hourOfDay==12){
                            timefinal = hourOfDay +12+ ":" + minute + " " + ampm;
                        }
                        {
                            timefinal = hourOfDay  + ":" + minute + " " + ampm;
                        }
                    }
                    emp.timeofBooking=timefinal;
                    timef.setText(timefinal);
                }
            }
        },hour,mMinute,false);
        timePickerDialog.show();
    }
}
