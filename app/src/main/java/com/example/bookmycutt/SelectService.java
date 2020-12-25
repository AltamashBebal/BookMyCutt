package com.example.bookmycutt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class SelectService extends AppCompatActivity {
    private TextView sname,stimng,slocation,details,fullAdd;
    private NotificationManagerCompat notificationManagerCompat;
    private ListView ls;
    private Button next;
    public final static String  CHANNEL_ID="personal_notification";
    public final int   NOTIFICATION_ID=001;
    TextView use,set;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    Double latitude,longitude;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference mref;
    int i=0;
    int tap=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        final Employees emp=(Employees) getApplicationContext();
        sname=findViewById(R.id.textView7);
        fullAdd=findViewById(R.id.textView3);
        stimng=findViewById(R.id.textView12);
        slocation=findViewById(R.id.textView3);
        ls=findViewById(R.id.listview);
        next=findViewById(R.id.next);
        details=findViewById(R.id.details);
        sname.setText("Salon::"+emp.getShopname());
        slocation.setText("Locality::"+emp.getShopLocation());
        emp.setTotal(0);
        emp.setItemCount(0);



        final String uid=emp.getPosition();
        Toast.makeText(SelectService.this, "Click on service to add", Toast.LENGTH_SHORT).show();
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectService.this,android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);

//        Toast.makeText(SelectService.this, "OwnerUid"+emp.OwnerUid, Toast.LENGTH_SHORT).show();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Owners").child(emp.OwnerUid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String add=dataSnapshot.child("shopaddress").getValue(String.class);
                fullAdd.setText("Shop Address:"+add);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mref= FirebaseDatabase.getInstance().getReference().child("Shop_details").child(String.valueOf(emp.OwnerUid)).child("Services");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name=dataSnapshot.child("Name").getValue(String.class);
                String price=dataSnapshot.child("Price").getValue(String.class);
                String time=dataSnapshot.child("TimeRequird").getValue(String.class);
                String cover=dataSnapshot.child("cover").getValue(String.class);

                emp.Images[i]=cover;
                //                emp.uid[i]=ui;
//                String value=dataSnapshot.getValue(Owners.class).toString();
//                String value2=dataSnapshot.getValue(Owners.class).getOwnername();
                String detail="Name:"+name+" Price"+price+" Time Required:"+time;
                emp.selectedServices[i]=name;
                emp.Cost[i]=price;
                emp.time[i]=time;
                emp.costChecked[i]="true";
                emp.itemCheck[i]="true";
                list.add(detail);
//                emp.(value2);
                adapter.notifyDataSetChanged();
                i++;
//                Toast.makeText(ChooseLocation.this, "List::"+list.get(0), Toast.LENGTH_SHORT).show();
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

//        final Employees emp=(Employees) getApplicationContext();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emp.finalService[position]=emp.selectedServices[position];
//                emp.setPosition(emp.uid[position]);uid[position], Toast.LENGTH_SHORT).show();
                if(emp.costChecked[position]=="true"){
                    emp.setTotal(emp.getTotal()+Integer.parseInt(emp.Cost[position]));
                    emp.totalCost=emp.getTotal();
                    emp.setPosition(emp.selectedServices[position],Integer.parseInt(emp.Cost[position]),emp.Images[position]);
                    emp.costChecked[position]="false";
                }
                else {
                    emp.setTotal(emp.getTotal()-Integer.parseInt(emp.Cost[position]));
                    emp.totalCost=emp.getTotal();

                    emp.costChecked[position]="true";
                }
                if(emp.itemCheck[position]=="true"){
                    emp.setItemCount(emp.getItemCount()+1);
                    emp.itemCheck[position]="false";
                }
                else {
                    emp.setItemCount(emp.getItemCount()-1);
                    emp.itemCheck[position]="true";

                }
                details.setText("Selected Services::"+emp.getItemCount()+" Total Amount::"+emp.getTotal());

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emp.getItemCount()  ==0)
                {
                    Toast.makeText(SelectService.this, "Please Select At least 1 service", Toast.LENGTH_SHORT).show();
                    details.setError("Please Select At least 1 service");
                }
                else
                    {
                        startActivity(new Intent(SelectService.this,SelectActivity.class));
                }
            }
        });



    use = findViewById(R.id.use);
    set = findViewById(R.id.address);
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        slocation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fetchlocation();
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                if(addresses!=null && addresses.size()>0){
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();
                    String knownname = addresses.get(0).getFeatureName();
                    slocation.setText("Locality::"+city);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    });

}


    private void fetchlocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(SelectService.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SelectService.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give permission to fetch your current location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SelectService.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SelectService.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    });
        }
    }

}
