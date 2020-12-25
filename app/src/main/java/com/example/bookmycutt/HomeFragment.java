package com.example.bookmycutt;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.core.app.NotificationCompat;
        import androidx.core.app.NotificationManagerCompat;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;

public class HomeFragment extends Fragment   implements MyAdapter.OnItemClickListener {
    private ListView listView;
    private TextView location,shop,selectLoc;
    private Button select;
    DatabaseReference mref;
    int i=0,j=0;

    private Button loc;
    MyAdapter.OnItemClickListener onItemClickListener;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Shop> list;
    MyAdapter adapter;
    String demo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        loc=view.findViewById(R.id.button2);
//        startActivity(new Intent(getActivity(),TestCustomeListView.class));
        recyclerView = (RecyclerView) view.findViewById(R.id.myrecycle);
        selectLoc=(TextView) view.findViewById(R.id.selectLocation);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Location2Activity.class));

            }
        });
        final Employees emp = (Employees)getActivity().getApplicationContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        super.onCreate(savedInstanceState);
//        emp.OwnerUid=null;

        reference = FirebaseDatabase.getInstance().getReference().child("Shop_details");
        emp.resetForBooking();
        emp.totalCost=0;
        emp.total=0;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Shop>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if((emp.locPref ==null)){
                    Shop p = dataSnapshot1.getValue(Shop.class);
                    String Locality = dataSnapshot1.child("Locality").getValue(String.class);
                    String uid = dataSnapshot1.child("uid").getValue(String.class);
                    list.add(p);
                    emp.shopnames[i] = p.getShopname();
                    emp.locations[i] = Locality;
                    emp.uid[i] = uid;//
                    emp.mainCost[i] = i;
                    i++;

                    }
                    else {
                        String Locality = dataSnapshot1.child("Locality").getValue(String.class);
                        if(Locality.equals(emp.locPref)) {
                            Shop p = dataSnapshot1.getValue(Shop.class);
                            String uid = dataSnapshot1.child("uid").getValue(String.class);

                            list.add(p);
                            emp.shopnames[i] = p.getShopname();
                            emp.locations[i] = Locality;
                            emp.uid[i] = uid;//
                            emp.mainCost[i] = i;
                            i++;

                        }
                    }
                    //          Toast.makeText(TestCustomeListView.this, "Image::"+dataSnapshot1, Toast.LENGTH_SHORT).show();

//                    recyclerView.setAdapter(adapter);

//                    recyclerView.hasFixedSize();

                }
                adapter = new MyAdapter(getActivity(), list,HomeFragment.this);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final ArrayList<String> list=new ArrayList<>();
//        mref= FirebaseDatabase.getInstance().getReference().child("Shop_details");

//                     Toast.makeText(ChooseLocation.this, ""+ list., Toast.LENGTH_SHORT).show();
        return view;
    }


    @Override
    public void onItem(View v, int position) {
        Employees emp=(Employees) getActivity().getApplicationContext();
        emp.OwnerUid =(emp.uid[position]);
        emp.setShopname(emp.shopnames[position]);
        emp.setShopLocation(emp.locations[position]);
        startActivity(new Intent(getActivity(),SelectService.class));
    }
}



