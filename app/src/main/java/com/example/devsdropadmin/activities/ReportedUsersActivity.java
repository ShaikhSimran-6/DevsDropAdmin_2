package com.example.devsdropadmin.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.devsdropadmin.adapter.ReportedUsersAdapter;
import com.example.devsdropadmin.databinding.ActivityReportedUsersBinding;
import com.example.devsdropadmin.model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportedUsersActivity extends AppCompatActivity {
    ActivityReportedUsersBinding binding;
  ReportedUsersAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Report> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding=ActivityReportedUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        setUpRecyclerView();
        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });


    }

    void setUpRecyclerView() {


        adapter = new ReportedUsersAdapter(this, list);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(adapter);

        database.getReference()
                .child("reported_users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Report report = dataSnapshot.getValue(Report.class);
                            list.add(report);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }




}