package com.example.bookmycutt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class OhomeFragment extends Fragment implements MyAdapterForusers.OnItemClickListener {
    private ListView ls;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    int i=0;
    private NotificationManagerCompat notificationManagerCompat;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<UserBookingDetails> list;
    MyAdapterForusers adapter;
    public final static String  CHANNEL_ID="personal_notification";
    public OhomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ohome, container, false);
        super.onCreate(savedInstanceState);

        final Employees emp = (Employees) getActivity().getApplicationContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.myrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("BookingsForOwner").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    list = new ArrayList<UserBookingDetails>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        System.out.println(dataSnapshot1);
                        String sname=dataSnapshot1.child("Userid").getValue(String.class);
                        emp.userUids[i]=sname;
                        UserBookingDetails p = dataSnapshot1.getValue(UserBookingDetails.class);
                        list.add(p);
                        i++;
                    }
                    adapter = new MyAdapterForusers(getActivity(), list, OhomeFragment.this);
                    recyclerView.setAdapter(adapter);
                }
                else
                    Toast.makeText(getActivity(),"NO bookings Found",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


    @Override
    public void onItem(View v, int position) {
        final Employees emp=(Employees) getActivity().getApplicationContext();

            emp.uidForBooked=emp.uid[position];
            emp.finalUserUid=emp.userUids[position];
                startActivity(new Intent(getActivity(),BookedUsers.class));
//            }
    }
}