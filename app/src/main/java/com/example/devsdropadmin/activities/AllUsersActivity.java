package com.example.devsdropadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.adapter.GetAllUsersAdapter;
import com.example.devsdropadmin.model.UserModel;
import com.example.devsdropadmin.utils.FirebaseUtil;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.List;

public class AllUsersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    //    AllUsersAdapter adapter;
    GetAllUsersAdapter adapter;
    ImageButton searchUser;
    List<UserModel> userList;
    ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        recyclerView = findViewById(R.id.all_users_Rv);
        searchUser = findViewById(R.id.search_user);
        back_btn = findViewById(R.id.back_btn);
        setUpRecyclerView();
        searchUser.setOnClickListener(view -> {
            Intent intent = new Intent(AllUsersActivity.this, SearchUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        });

//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//
//        recyclerView.setLayoutManager(layoutManager);
//       userList = new ArrayList<>();
//        adapter = new AllUsersAdapter(userList,AllUsersActivity.this);
//
//FirebaseUtil.allUsersCollectionReference().get().
//        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//    @Override
//    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        if (task.isSuccessful()) {
//
//            for (QueryDocumentSnapshot document : task.getResult()) {
//                UserModel userModel = document.toObject(UserModel.class);
//                userList.add(userModel);
//            }
//            // Set up RecyclerView adapter
//
//            recyclerView.setAdapter(adapter);
//        } else {
//            Log.d(TAG, "Error getting documents: ", task.getException());
//        }
//    }
//});

        back_btn.setOnClickListener(view -> {
            onBackPressed();
        });


    }

    void setUpRecyclerView() {
        Query query = FirebaseUtil.allUsersCollectionReference();

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        adapter = new GetAllUsersAdapter(options, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
        adapter.notifyDataSetChanged();
    }


}