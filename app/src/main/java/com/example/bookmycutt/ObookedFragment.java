package com.example.bookmycutt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ObookedFragment extends Fragment {
    private ListView ls;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    int i=0;
    public ObookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_users, container, false);
        ls=view.findViewById(R.id.listview);
        mAuth= FirebaseAuth.getInstance();
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);
        final Employees emp=(Employees) getActivity().getApplicationContext();
        final FirebaseUser user=mAuth.getCurrentUser();
        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Owners");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String status="hello";
                String sname=dataSnapshot.child("ShopName").getValue(String.class);
                String  total=dataSnapshot.child("totalAmount").getValue(String.class);
                String  ownerUid=dataSnapshot.child("Owneruid").getValue(String.class);
                String status2=dataSnapshot.child("Status").getValue(String.class);
                String noOfServices=dataSnapshot.child("noOfServiceSelected").getValue(String.class);
                status=status2;
                emp.Keys[i]=String.valueOf(dataSnapshot.getKey());
                emp.ServiceNameForCancel[i]=noOfServices;
                emp.ShopNameForCancel[i]=sname;
                emp.TotalForCancel[i]=total;
                emp.OwnerUidForCancel[i]=ownerUid;
//                if(status==null) {
////                    list.add("No bookings Found");
////                }else if(status=="Active"){
////                    list.add(sname);
////                }
////                else
////                {
////                    list.add(sname+"            This Booking is Canceled");
////                }
                if(status.equals("Active")){
                    list.add(sname);
                }
                else {
                    list.add(sname+"            This Booking is Canceled");
                }
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
        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Users").child(user.getUid());
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid())){
                    emp.i=1;
                }
                else {
                    if(emp.i==0){
//                        Toast.makeText(getActivity(),"Children::"+emp.i,Toast.LENGTH_SHORT).show();
                        list.add("No bookings Found");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(emp.i==0)
            list.add("No bookings Found");

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emp.ServiceCalcel=emp.ServiceNameForCancel[position];
                emp.ShopCancel=emp.ShopNameForCancel[position];
                emp.TotalCalcel=emp.TotalForCancel[position];
                emp.OwnerCalncel=emp.OwnerUidForCancel[position];
                emp.key=emp.Keys[position];
//                Toast.makeText(getActivity(),"OwnerUid::"+emp.OwnerCalncel,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(),"Sname::"+emp.ShopCancel,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(),"Total::"+emp.TotalCalcel,Toast.LENGTH_SHORT).show();



                startActivity(new Intent(getActivity(),CancelActivity.class));
            }
        });
        return view;
    }
}
