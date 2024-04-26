package com.example.devsdropadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.example.devsdropadmin.R;
import com.example.devsdropadmin.adapter.PostAdapter;
import com.example.devsdropadmin.model.DashBoardModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllPostsActivity extends AppCompatActivity {
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    PostAdapter adapter;
    DatabaseReference mbase;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView = findViewById(R.id.dashboardRV);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);

        mbase = FirebaseDatabase.getInstance().getReference().child("posts");
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllPostsActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        // Set the LayoutManager to the RecyclerView
        recyclerView.setLayoutManager(layoutManager);


        // query in the database to fetch appropriate data
        Query query = mbase.orderByChild("postedAt").limitToLast(50);
        FirebaseRecyclerOptions<DashBoardModel> options
                = new FirebaseRecyclerOptions.Builder<DashBoardModel>()
                .setQuery(query, DashBoardModel.class)
                .build();


        adapter = new PostAdapter(options,AllPostsActivity.this);

        startFetching();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform data refreshing operation
                // Example: Call a method to fetch new data from a data source
                swipeRefreshLayout.setRefreshing(true);
                startFetching();


                // Simulate data fetching for 2 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop SwipeRefreshLayout progress bar
                        swipeRefreshLayout.setRefreshing(false);

                        // Update RecyclerView with new data
                        // Here you would typically notify your adapter that the data set has changed
                    }
                }, 2000);
            }

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

    public void startFetching() {
        //start Shimmer layout animation
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//check whether internet connection available or not


                //stop shimmer layout animation
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);


            }
        }, 2000);
    }


}