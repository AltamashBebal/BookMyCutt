package com.example.bookmycutt;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class UsersFragment extends Fragment {
    private ListView ls;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    int i=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_users, container, false);
        ls=view.findViewById(R.id.listview);
        mAuth=FirebaseAuth.getInstance();
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        ls.setAdapter(adapter);
        final Employees emp=(Employees) getActivity().getApplicationContext();
        final FirebaseUser user=mAuth.getCurrentUser();
        mref= FirebaseDatabase.getInstance().getReference().child("Bookings").child("Users").child(user.getUid());
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(String.valueOf(i+1));
                String sname=dataSnapshot.child("ShopName").getValue(String.class);
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
        Toast.makeText(getActivity(),"i::"+emp.i,Toast.LENGTH_SHORT).show();
        return view;
    }
}
