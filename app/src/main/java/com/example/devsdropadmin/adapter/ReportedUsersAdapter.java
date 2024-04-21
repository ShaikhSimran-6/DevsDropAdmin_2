package com.example.devsdropadmin.adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.example.devsdropadmin.R;
import com.example.devsdropadmin.activities.UserDetailsActivity;
import com.example.devsdropadmin.databinding.ReportedUsersRowLayoutBinding;
import com.example.devsdropadmin.model.Report;
import com.example.devsdropadmin.model.UserModel;
import com.example.devsdropadmin.utils.FirebaseUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReportedUsersAdapter extends RecyclerView.Adapter<ReportedUsersAdapter.MyViewholder>{

    Context context ;
    List<Report> list;

    public ReportedUsersAdapter(Context context, List<Report> list) {
        this.context=context;

        this.list = list;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reported_users_row_layout, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Report reportmodel=list.get(position);

        FirebaseUtil.userDetails(reportmodel.getPostId()).addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        UserModel model =value.toObject(UserModel.class);

                        holder.binding.username.setText(model.getUsername());
                        holder.binding.email.setText(model.getEmail());
                        Picasso.get()
                                .load(model.getProfile())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.userprofile);

                        holder.binding.reportReason.setText("Reason : "+reportmodel.getReason());


                }
                }
        );

        holder.itemView.setOnClickListener(v -> {
            //chat activity
            Intent intent=new Intent(context, UserDetailsActivity.class);
//            AndroidUtil.passUsermodelIntent(intent,model);
            intent.putExtra("userId",reportmodel.getPostId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }





class MyViewholder extends RecyclerView.ViewHolder {

        ReportedUsersRowLayoutBinding binding;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            binding = ReportedUsersRowLayoutBinding.bind(itemView);

        }
    }
}

