package com.example.devsdropadmin.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.adapter.ProfilePostAdapter;
import com.example.devsdropadmin.model.DashBoardModel;
import com.example.devsdropadmin.model.UserModel;
import com.example.devsdropadmin.utils.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProfilePostAdapter adapter; // Create Object of the Adapter class
ImageView back_btn;
    TextView profile_username,followcount,following;
    TextView profile_Proffesion;
    ImageView profile_imageView;
    FirebaseAuth mAuth ;
    Button deleteUserBtn;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        recyclerView = findViewById(R.id.profileRv);
        followcount=findViewById(R.id.followcount);
        following=findViewById(R.id.followingcount);
        profile_username =findViewById(R.id.profile_username);
        profile_Proffesion =findViewById(R.id.profile_Proffesion);
        profile_imageView=findViewById(R.id.profile_imageView);
        back_btn=findViewById(R.id.backbtn);
        deleteUserBtn=findViewById(R.id.delete_user_btn);
        mAuth = FirebaseAuth.getInstance();

        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        Toast.makeText(UserDetailsActivity.this, "hiiii", Toast.LENGTH_SHORT).show();


        FirebaseUtil.getOtherUserDetails(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                profile_username.setText(userModel.getUsername().toString());
                profile_Proffesion.setText(userModel.getProfession().toString());
                Picasso.get()
                        .load(userModel.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(profile_imageView);
                followcount.setText(String.valueOf(userModel.getFollowersCount()));
                following.setText(String.valueOf(userModel.getFollowingCount()));
                try {
                    if (userModel.getDeleted())
                    {
                        deleteUserBtn.setActivated(false);
                    }
                }catch (Exception e){
                    Toast.makeText(UserDetailsActivity.this, "error defining deleted or not", Toast.LENGTH_SHORT).show();
                }


            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(UserDetailsActivity.this, 3, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts").orderByChild("postedBy").equalTo(userId);

        FirebaseRecyclerOptions<DashBoardModel> options
                = new FirebaseRecyclerOptions.Builder<DashBoardModel>()
                .setQuery(query, DashBoardModel.class)
                .build();

        // the Adapter class itself
        adapter = new ProfilePostAdapter(options);


        recyclerView.setAdapter(adapter);

        back_btn.setOnClickListener(view -> {
            onBackPressed();

        });
        deleteUserBtn.setOnClickListener(view -> {
            showYesNoDialog(UserDetailsActivity.this,"Confirmation","Are you sure you want to delete?");
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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

//                FirebaseUtil.getOtherUserDetails(userId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Document successfully deleted
//                                Log.d(TAG, "User successfully deleted!");
//                                onBackPressed();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Handle any errors
//                                Log.w(TAG, "Error deleting User", e);
//                            }
//                        });;
                // Add your action here if user clicks Yes
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