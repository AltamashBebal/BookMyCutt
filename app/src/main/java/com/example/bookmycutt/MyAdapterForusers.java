package com.example.bookmycutt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapterForusers  extends RecyclerView.Adapter<MyAdapterForusers.MyViewHolder>{

    Employees emp=new Employees();
    Context context;
    ArrayList<UserBookingDetails> userBookingDetails ;
    OnItemClickListener onItemClickListener;

    public MyAdapterForusers(Context c , ArrayList<UserBookingDetails> s, OnItemClickListener onItemClickListener)

    {
        this.onItemClickListener=onItemClickListener;
        context = c;
        userBookingDetails = s;
    }
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list2,parent,false);
        return new MyViewHolder(view,onItemClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            //if image is recieved then set
            holder.name.setText(userBookingDetails.get(position).getUserName());
        }
        catch (Exception e){
            // if image is not loaded
            holder.name.setText("Demo");

//            Picasso.get().load(R.drawable.logo).into(holder.profilePic);
        }

        try {
            //if image is recieved then set
            holder.type.setText(userBookingDetails.get(position).getUserGender());
        }
        catch (Exception e){
            // if image is not loaded
            holder.type.setText("Not Set");
//            Picasso.get().load(R.drawable.logo).into(holder.profilePic);
        }

        try {
            //if image is recieved then set
            holder.locality.setText("Contact:"+userBookingDetails.get(position).getContact());
        }
        catch (Exception e){
            // if image is not loaded
            holder.locality.setText("Demo");

//            Picasso.get().load(R.drawable.logo).into(holder.profilePic);
        }
//        Toast.makeText(MyAdapter.this,"Image"+shop.get(position).getImage(),Toast.LENGTH_SHORT).show();
        try {
            //if image is recieved then set
            Picasso.get().load(userBookingDetails.get(position).getUserProfile()).into(holder.profilePic);
        }
        catch (Exception e){
            // if image is not loaded
//            Picasso.get().load(R.drawable.logo).into(holder.profilePic);
        }

//        Picasso.get().load(shop.get(position).getProfilePic()).into(holder.profilePic);
//        if(profiles.get(position).getPermission()) {
//            holder.btn.setVisibility(View.VISIBLE);
//            holder.onClick(position);
//        }

    }

    @Override
    public int getItemCount() {
        return userBookingDetails.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name,type,locality;
        ImageView profilePic;
        RelativeLayout layout;
        OnItemClickListener onItemClickListener;
        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
//            layout = (RelativeLayout) itemView.findViewById(R.id.listview);
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            locality = (TextView) itemView.findViewById(R.id.locality);
            profilePic = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
            this.onItemClickListener=onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItem(v,getAdapterPosition());
        }
    }



    public  interface OnItemClickListener{
        void onItem(View v, int position);
    }
}
