package com.example.devsdropadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.devsdropadmin.activities.AllPostsActivity;
import com.example.devsdropadmin.activities.AllQuestionsActivity;
import com.example.devsdropadmin.activities.AllUsersActivity;
import com.example.devsdropadmin.activities.ReportedPostsActivity;
import com.example.devsdropadmin.activities.ReportedQuestionsActivity;
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
        binding.card2Post.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AllPostsActivity.class));
        });
        binding.card3Question.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AllQuestionsActivity.class));
        });
        binding.card4ReUser.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ReportedUsersActivity.class));
        });
        binding.card5.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ReportedPostsActivity.class));
        });
        binding.card6.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ReportedQuestionsActivity.class));
        });
    }
}