package com.example.devsdropadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.model.DashBoardModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllPostsActivity extends AppCompatActivity {
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    LinearLayout lnr_data_unavailable;

    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);

    }


}