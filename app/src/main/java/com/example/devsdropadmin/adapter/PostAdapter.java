package com.example.devsdropadmin.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.activities.UserDetailsActivity;
import com.example.devsdropadmin.databinding.AllpostsRowLayoutBinding;
import com.example.devsdropadmin.databinding.ReportedPostsLayoutBinding;
import com.example.devsdropadmin.model.DashBoardModel;
import com.example.devsdropadmin.model.UserModel;
import com.example.devsdropadmin.utils.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends FirebaseRecyclerAdapter<
        DashBoardModel, PostAdapter.DashBoardModelsViewholder> {
    Context context;

    public PostAdapter(
            @NonNull FirebaseRecyclerOptions<DashBoardModel> options, Context context) {
        super(options);
     this.context=context;
    }


    @Override
    protected void
    onBindViewHolder(@NonNull DashBoardModelsViewholder holder,
                     int position, @NonNull DashBoardModel model) {

        FirebaseUtil.getOtherUserDetails(model.getPostedBy()).get().addOnCompleteListener(task -> {
            UserModel model1 = task.getResult().toObject(UserModel.class);
            holder.username.setText(model1.getUsername().toString());

            try{
                if (model.isDeleted())
                {
                    holder.binding.mainlayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
                }
            }
            catch (Exception e){

            }

if (model1.getProfile()!=null)
{
            Picasso.get()
                    .load(model1.getProfile())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.profileImage);
}
        });

        long timestamp = model.getPostedAt();

        // Convert the timestamp to Date
        Date date = new Date(timestamp);

        // Format the Date to a human-readable format
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM h:mma", Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        holder.time.setText(formattedDate.toString());

        holder.like.setText(model.getPostLike() + "");
//        holder.comment.setText(model.getCommentCount()+"");

        String description = model.getPostDescription();
        if (description.equals("")) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setText(model.getPostDescription());
            holder.description.setVisibility(View.VISIBLE);
        }

        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.post_image);






        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, CommentActivity.class);
//
//                    intent.putExtra("postId", model.getPostId());
//                    intent.putExtra("postedBy", model.getPostedBy());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);

            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("otherUser", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.binding.postMenu.setOnClickListener(view -> {
            showYesNoDialog(context,"Delete Post","Are you sure you want to delete?",model.getPostId());
        });




    }



    @NonNull
    @Override
    public DashBoardModelsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.allposts_row_layout, parent, false);
        return new DashBoardModelsViewholder(view);
    }


    class DashBoardModelsViewholder
            extends RecyclerView.ViewHolder {

        ImageView post_image, menu, comment,profileImage;
        TextView username, description, like, time;
        AllpostsRowLayoutBinding binding;


        public DashBoardModelsViewholder(@NonNull View itemView) {
            super(itemView);
            binding=AllpostsRowLayoutBinding.bind(itemView);

            post_image = itemView.findViewById(R.id.dashBoard_postImage);
            username = itemView.findViewById(R.id.dashBoard_userName);
            description = itemView.findViewById(R.id.postDescription);
            time = itemView.findViewById(R.id.time);
            menu = itemView.findViewById(R.id.post_menu);
            profileImage=itemView.findViewById(R.id.profileImage);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
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

