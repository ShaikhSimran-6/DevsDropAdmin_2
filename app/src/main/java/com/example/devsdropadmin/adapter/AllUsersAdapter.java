package com.example.devsdropadmin.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.devsdropadmin.R;
import com.example.devsdropadmin.databinding.AllUsersRowLayoutBinding;
import com.example.devsdropadmin.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.MyViewholder>{

    Context context ;
    List<UserModel> list;

    public AllUsersAdapter( List<UserModel> list,Context context) {
        this.context=context;

        this.list = list;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_users_row_layout, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        UserModel model=list.get(position);

holder.binding.username.setText(model.getUsername());
holder.binding.email.setText(model.getEmail());
        Picasso.get()
                .load(model.getProfile())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.userprofile);
    }





    @Override
    public int getItemCount() {
        return list.size();
    }





class MyViewholder extends RecyclerView.ViewHolder {

        AllUsersRowLayoutBinding binding;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            binding = AllUsersRowLayoutBinding.bind(itemView);

        }
    }
}

