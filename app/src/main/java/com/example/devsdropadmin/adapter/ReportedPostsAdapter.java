package com.example.devsdropadmin.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.activities.UserDetailsActivity;
import com.example.devsdropadmin.databinding.ReportedPostsLayoutBinding;
import com.example.devsdropadmin.databinding.ReportedUsersRowLayoutBinding;
import com.example.devsdropadmin.model.DashBoardModel;
import com.example.devsdropadmin.model.Report;
import com.example.devsdropadmin.model.UserModel;
import com.example.devsdropadmin.utils.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ReportedPostsAdapter extends RecyclerView.Adapter<ReportedPostsAdapter.MyViewholder>{

    Context context ;
    List<Report> list;

    public ReportedPostsAdapter(Context context, List<Report> list) {
        this.context=context;

        this.list = list;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reported_posts_layout, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Report reportmodel = list.get(position);

        holder.binding.reportReason.setText("Reason : "+reportmodel.getReason());
        FirebaseDatabase.getInstance().getReference("posts").child(reportmodel.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Assuming Post is a class representing your post data
                DashBoardModel post = dataSnapshot.getValue(DashBoardModel.class);
                if (post.getPostDescription()!=null){
                    holder.binding.postDescription.setText(post.getPostDescription());
                }
                else{
                    holder.binding.postDescription.setText(" ");
                }

                Picasso.get()
                        .load(post.getPostImage())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.binding.dashBoardPostImage);
                holder.binding.like.setText(String.valueOf(post.getPostLike()));

                FirebaseUtil.userDetails(post.getPostedBy()).get().addOnCompleteListener(task -> {
                    UserModel model1 = task.getResult().toObject(UserModel.class);
                    holder.binding.dashBoardUserName.setText(model1.getUsername().toString());

                    if (model1.getProfile()!=null)
                    {
                        Picasso.get()
                                .load(model1.getProfile())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profileImage);
                    }
                });

                long timestamp = post.getPostedAt();

                // Convert the timestamp to Date
                Date date = new Date(timestamp);

                // Format the Date to a human-readable format
                SimpleDateFormat sdf = new SimpleDateFormat("d MMM h:mma", Locale.ENGLISH);
                String formattedDate = sdf.format(date);
                holder.binding.time.setText(formattedDate.toString());

                holder.binding.postMenu.setOnClickListener(view -> {
                    showYesNoDialog(context,"Delete Posts","Are you sure want to delete?", reportmodel.getPostId());
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(v -> {
            //chat activity
//            Intent intent=new Intent(context, UserDetailsActivity.class);
////            AndroidUtil.passUsermodelIntent(intent,model);
//            intent.putExtra("userId",reportmodel.getPostId());
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);

        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }





class MyViewholder extends RecyclerView.ViewHolder {

        ReportedPostsLayoutBinding binding;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            binding = ReportedPostsLayoutBinding.bind(itemView);

        }
    }

    private void showYesNoDialog(Context context, String title, String message,String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive (Yes) button click
                // Dismiss the dialog
                FirebaseUtil.markPostsAsDeleted(id);
                Map<String, Object> data = new HashMap<>();
                data.put("userId", id);
                FirebaseFirestore.getInstance().collection("deletedPosts").add(data);
                dialog.dismiss();
//
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle negative (No) button click
                dialog.dismiss(); // Dismiss the dialog
                // Add your action here if user clicks No
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}

