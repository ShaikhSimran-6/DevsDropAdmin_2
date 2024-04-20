package com.example.devsdropadmin.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.devsdropadmin.R;
import com.example.devsdropadmin.model.DashBoardModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class ProfilePostAdapter extends FirebaseRecyclerAdapter<
        DashBoardModel, ProfilePostAdapter.DashBoardModelsViewholder> {

    public ProfilePostAdapter(
            @NonNull FirebaseRecyclerOptions<DashBoardModel> options) {
        super(options);
    }


    @Override
    protected void
    onBindViewHolder(@NonNull DashBoardModelsViewholder holder,
                     int position, @NonNull DashBoardModel model) {


        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.post_image);


    }


    @NonNull
    @Override
    public DashBoardModelsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_post_row, parent, false);
        return new DashBoardModelsViewholder(view);
    }

    class DashBoardModelsViewholder
            extends RecyclerView.ViewHolder {

        ImageView post_image;


        public DashBoardModelsViewholder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.profile_postImage);

        }
    }
}

