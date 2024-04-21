package com.example.devsdropadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.devsdropadmin.activities.AllPostsActivity;
import com.example.devsdropadmin.activities.AllUsersActivity;
import com.example.devsdropadmin.activities.ReportedUsersActivity;
import com.example.devsdropadmin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Button allUsers,allPosts;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.card1Users.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AllUsersActivity.class));
        });
        binding.card4ReUser.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ReportedUsersActivity.class));
        });

    }
}