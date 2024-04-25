package com.example.devsdropadmin.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.adapter.ReportedPostsAdapter;
import com.example.devsdropadmin.databinding.ActivityReportedQuestionsBinding;
import com.example.devsdropadmin.model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportedQuestionsActivity extends AppCompatActivity {
   ActivityReportedQuestionsBinding binding;
    ReportedPostsAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Report> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityReportedQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        setUpRecyclerView();
        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }
    void setUpRecyclerView() {


        adapter = new ReportedPostsAdapter(this, list);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerview.setAdapter(adapter);

        database.getReference()
                .child("reported_queries").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Report report = dataSnapshot.getValue(Report.class);
                            list.add(report);
                        }

                        if (list.isEmpty()) {
                            // Show TextView instead of RecyclerView
                            binding.recyclerview.setVisibility(View.GONE);
                            binding.emptyTextView.setVisibility(View.VISIBLE);
                        } else {
                            // Show RecyclerView
                            binding.recyclerview.setVisibility(View.VISIBLE);
                            binding.emptyTextView.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }
}