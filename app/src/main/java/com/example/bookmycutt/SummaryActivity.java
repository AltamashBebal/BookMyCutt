package com.example.bookmycutt;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {
    private TextView sname,location,count;
   private FirebaseAuth firebaseAuth;

    public final String  CHANNEL_ID="personal_notification";
    public final int   NOTIFICATION_ID=001;
    private ListView ls;
    private Button next;
    int total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        sname=findViewById(R.id.salonsName);
        location=findViewById(R.id.location);
        final Employees emp=(Employees) getApplicationContext();
        sname.setText("Salon:"+emp.getShopname());
        location.setText("Locality:"+emp.getShopLocation());
        ls=findViewById(R.id.listview);
        next=findViewById(R.id.button_continue);
        count=findViewById(R.id.total);
        count.setText("Rs:"+emp.getTotal());
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        final String uid=firebaseAuth.getCurrentUser().getUid();
        MyCustomAdapter myadapter=new MyCustomAdapter();

        ls.setAdapter(myadapter);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(emp.totalCost <=0)){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference reference = database.getReference("Bookings").child("Users").child(user.getUid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("Userid",uid);
                            hashMap.put("Owneruid",emp.OwnerUid);
                            hashMap.put("noOfServiceSelected",String.valueOf(emp.getItemCount()));
                            hashMap.put("ShopName",emp.getShopname());
                            hashMap.put("ShopLocality",emp.getShopLocation());
                            hashMap.put("totalAmount",String.valueOf(emp.totalCost));
                            hashMap.put("SelectedEmployee",emp.SelectedEmployee);
                            hashMap.put("Date",emp.date);
                            hashMap.put("Time",emp.timeofBooking);
                            hashMap.put("Status","Active");
                            reference.child(String.valueOf(dataSnapshot.getChildrenCount()+1)).setValue(hashMap);
                            DatabaseReference reference2 = database.getReference("Bookings").child(user.getUid());
                            HashMap<Object,String> hashMap2 = new HashMap<>();
                            for (int i=0;i<emp.j;i++){
                                hashMap2.put("ServiceName",emp.finalService[i]);
                                hashMap2.put("Price",emp.finalCost[i]);
                                reference.child(String.valueOf(dataSnapshot.getChildrenCount()+1)).child("/Services/"+emp.finalService[i]).setValue(hashMap2);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                        final DatabaseReference reference3 = database.getReference("Bookings").child("Owners").child(emp.OwnerUid).child(user.getUid());
                              reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final HashMap<Object,String> hashMap3 = new HashMap<>();
                            hashMap3.put("Userid",uid);
                            hashMap3.put("Owneruid",emp.OwnerUid);
                            hashMap3.put("noOfServiceSelected",String.valueOf(emp.getItemCount()));
                            hashMap3.put("ShopName",emp.getShopname());
                            hashMap3.put("ShopLocality",emp.getShopLocation());
                            hashMap3.put("totalAmount",String.valueOf(emp.totalCost));
                            hashMap3.put("SelectedEmployee",emp.SelectedEmployee);
                            hashMap3.put("Date",emp.date);
                            hashMap3.put("Time",emp.timeofBooking);
                            hashMap3.put("Status","Active");
                            reference3.child(String.valueOf(dataSnapshot.getChildrenCount()+1)).setValue(hashMap3);
                            DatabaseReference reference4 = database.getReference("Bookings").child(user.getUid());
                            HashMap<Object,String> hashMap4 = new HashMap<>();
                            for (int i=0;i<emp.j;i++){
                                hashMap4.put("ServiceName",emp.finalService[i]);
                                hashMap4.put("Price",emp.finalCost[i]);
                                reference3.child(String.valueOf(dataSnapshot.getChildrenCount()+1)).child("/Services/"+emp.finalService[i]).setValue(hashMap4);
                            }
                            final DatabaseReference referenceForUser = database.getReference("Bookings").child("Owners").child(emp.OwnerUid).child(user.getUid()).child(String.valueOf(dataSnapshot.getChildrenCount()+1));


                            final DatabaseReference newRef2 = database.getReference("Users").child(user.getUid());
                            newRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String username=dataSnapshot.child("name").getValue(String.class);
                                    final String gender=dataSnapshot.child("gender").getValue(String.class);
                                    final String image=dataSnapshot.child("image").getValue(String.class);
                                    final String contact=dataSnapshot.child("phone").getValue(String.class);

                                    Map<String, Object> updates2 = new HashMap<String,Object>();
//                updates.put("

                                    updates2.put("UserName", username);
                                    updates2.put("UserGender", gender);
                                    updates2.put("UserProfile", image);
                                    referenceForUser.updateChildren(updates2);

                                    final DatabaseReference demo=database.getReference().child("BookingsForOwner").child(emp.OwnerUid).child(user.getUid());
                                    demo.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final HashMap<Object,String> hashMap9 = new HashMap<>();
                                            hashMap9.put("UserName",username);
                                            hashMap9.put("UserProfile",image);
                                            hashMap9.put("UserGender",gender);
                                            hashMap9.put("Date",emp.date);
                                            hashMap9.put("Time",emp.timeofBooking);
                                            hashMap9.put("Userid",user.getUid());
                                            hashMap9.put("Contact",contact);


                                            demo.setValue(hashMap9);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                            final DatabaseReference newRef = database.getReference("Notifications").child(emp.OwnerUid);
                            newRef.addListenerForSingleValueEvent(new ValueEventListener() {


                                                                      @Override
                                                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                          dataSnapshot.getChildrenCount();
                                                                          HashMap<Object,String> hashMap13 = new HashMap<>();

                                                                          hashMap13.put("Date",emp.date);
                                                                          hashMap13.put("Time",emp.timeofBooking);
                                                                          hashMap13.put("Name","Demo");
                                                                          hashMap13.put("Status","Active");
                                                                          final DatabaseReference newRef3 = database.getReference("Notifications").child(emp.OwnerUid).child(String.valueOf(dataSnapshot.getChildrenCount()+1));

                                                                          newRef.child(String.valueOf(dataSnapshot.getChildrenCount()+1)).setValue(hashMap13);
                                                                          final DatabaseReference newRef2 = database.getReference("Users").child(user.getUid());
                                                                          newRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                              @Override
                                                                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                  String username=dataSnapshot.child("name").getValue(String.class);
                                                                                  Map<String, Object> updates2 = new HashMap<String,Object>();
//                updates.put("
                                                                                  updates2.put("Name", username);
                                                                                  newRef3.updateChildren(updates2);

                                                                              }

                                                                              @Override
                                                                              public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                              }
                                                                          });

                                                                      }

                                                                      @Override
                                                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                      }
                                                                  });


                            startActivity(new Intent(SummaryActivity.this,BookingConfirmedActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                else
                {
                    next.setError("Total Cannot be zero");
                    Toast.makeText(SummaryActivity.this,"Total Cannot be zero",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }




    public void setCountText(int t){
        count.setText("Rs:"+t);
    }

    private class MyCustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            Employees emp=(Employees) getApplicationContext();
            return emp.getItemCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Button btn;
            final int maincost;
            final Employees emp=(Employees) getApplicationContext();
            convertView=getLayoutInflater().inflate(R.layout.list_items,null);
            TextView sname=(TextView)convertView.findViewById(R.id.tv1);
            final TextView cost=(TextView)convertView.findViewById(R.id.price1);
            final TextView count=(TextView)convertView.findViewById(R.id.count);
            ImageView plus=(ImageView) convertView.findViewById(R.id.plus);
            ImageView minus=(ImageView) convertView.findViewById(R.id.minus);
            final ImageView OwnerProfile=(ImageView) convertView.findViewById(R.id.imageForCover);

            sname.setText(emp.finalService[position]);
            cost.setText("Rs:"+emp.finalCost[position]);
            emp.mainCost[position]=Integer.parseInt(emp.finalCost[position]);
            if(position==0){
            emp.plusminus[position]= String.valueOf(position+1);}
            else {emp.plusminus[position]= String.valueOf(position);}
            plus.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    total=0;
                    emp.plusminus[position]= String.valueOf(Integer.parseInt(emp.plusminus[position])+1);
                    emp.mainCost[position]=emp.mainCost[position]+Integer.parseInt(emp.finalCost[position]);
//                    emp.setTotal(emp.mainCost);
                    cost.setText("Rs::"+String.valueOf(emp.mainCost[position]));
                    count.setText(""+String.valueOf(emp.plusminus[position]));
                    for (int i=0;i<emp.j;i++){
                        total=total+emp.mainCost[i];
                    }
                    emp.totalCost=total;
                    setCountText(emp.totalCost);
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    total=0;
                    emp.plusminus[position]= String.valueOf(Integer.parseInt(emp.plusminus[position])-1);
                    emp.mainCost[position]=emp.mainCost[position]-Integer.parseInt(emp.finalCost[position]);
//                    emp.setTotal(emp.mainCost);
                    cost.setText("Rs::"+ emp.mainCost[position]);
                    count.setText(""+emp.plusminus[position]);
                    for (int i=0;i<emp.j;i++){
                        total=total+emp.mainCost[i];
                    }
                    emp.totalCost=total;
                    setCountText(emp.totalCost);
                }
            });
            sname.setText(emp.finalService[position]);
//                                    Picasso.get().load(emp.Images[position]).into(OwnerProfile);
//

//            try {
//                        //if image is recieved then set
//                        Picasso.get().load(emp.Images[position]).into(OwnerProfile);
//                    }
//                    catch (Exception e){
//                        // if image is not loaded
//                        Picasso.get().load(R.drawable.noimage).into(OwnerProfile);
//                    }


        DatabaseReference    mref= FirebaseDatabase.getInstance().getReference().child("Owners").child(String.valueOf(emp.OwnerUid));

            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String image=dataSnapshot.child("cover").getValue(String.class);
                    if(image==null) {
                        Picasso.get().load(R.drawable.flogo).into(OwnerProfile);
                    }
                    else {
                        Picasso.get().load(image).into(OwnerProfile);
                    }



//                    try {
//                        //if image is recieved then set
//                        Picasso.get().load(image).into(OwnerProfile);
//                    }
//                    catch (Exception e){
//                        // if image is not loaded
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return convertView;
        }
    }
}