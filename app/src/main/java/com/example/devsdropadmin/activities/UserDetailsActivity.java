package com.example.devsdropadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.adapter.ProfilePostAdapter;
import com.example.devsdropadmin.databinding.ActivityUserDetailsBinding;
import com.example.devsdropadmin.model.DashBoardModel;
import com.example.devsdropadmin.model.UserModel;
import com.example.devsdropadmin.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {
   ActivityUserDetailsBinding binding;
 String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");



        FirebaseUtil.getOtherUserDetails(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                binding.otherProfileUsername.setText(userModel.getUsername().toString());
                binding.otherProfileProffesion.setText(userModel.getProfession().toString());
                Picasso.get()
                        .load(userModel.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.otherProfileImageView);
                binding.followcount.setText(String.valueOf(userModel.getFollowersCount()));
                binding.followingcount.setText(String.valueOf(userModel.getFollowingCount()));
                binding.noOfPosts.setText(String.valueOf(userModel.getNumberOfPosts()));

                try {
                    if (userModel.getDeleted())
                    {
                        binding.deleteUserBtn.setActivated(false);
                    }
                }catch (Exception e){
                    Toast.makeText(UserDetailsActivity.this, "error defining deleted or not", Toast.LENGTH_SHORT).show();
                }

            }
        });






        binding.backbtnOtherProfile.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.deleteUserBtn.setOnClickListener(view -> {
            showYesNoDialog(UserDetailsActivity.this,"Confirmation","Are you sure you want to delete?");
        });


        GridLayoutManager layoutManager = new GridLayoutManager(UserDetailsActivity.this, 3, GridLayoutManager.VERTICAL, false);
        binding.profileRv.setLayoutManager(layoutManager);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts").orderByChild("postedBy").equalTo(FirebaseUtil.currentUserId()).limitToLast(50);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DashBoardModel> dataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DashBoardModel model = dataSnapshot.getValue(DashBoardModel.class);
                    dataList.add(model);
                }
                // Reverse the data list
                Collections.reverse(dataList);

                // Pass the reversed data to the adapter
                ProfilePostAdapter adapter = new ProfilePostAdapter(dataList,UserDetailsActivity.this);
                binding.profileRv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


    }

    private void showYesNoDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive (Yes) button click
                dialog.dismiss(); // Dismiss the dialog
                FirebaseUtil.getOtherUserDetails(userId).update("deleted",true);
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                FirebaseFirestore.getInstance().collection("deletedUsers").add(data);

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