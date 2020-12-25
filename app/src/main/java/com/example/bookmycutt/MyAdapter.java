package com.example.bookmycutt;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Employees emp=new Employees();
    Context context;
    ArrayList<Shop> shop;
    OnItemClickListener onItemClickListener;


    public MyAdapter(Context c , ArrayList<Shop> s,OnItemClickListener onItemClickListener)

    {
        this.onItemClickListener=onItemClickListener;
        context = c;
        shop = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.list2,parent,false);
        return new MyViewHolder(view,onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(shop.get(position).getShopname());
        holder.locality.setText(shop.get(position).getLocality());
        holder.type.setText(shop.get(position).getType());
//        Toast.makeText(MyAdapter.this,"Image"+shop.get(position).getImage(),Toast.LENGTH_SHORT).show();
        try {
            //if image is recieved then set
            Picasso.get().load(shop.get(position).getImage()).into(holder.profilePic);
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
        return shop.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name,type,locality;
        ImageView profilePic;
        RelativeLayout layout;
        OnItemClickListener onItemClickListener;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
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
    void onItem(View v,int position);
    }

}