package com.example.test;

import static androidx.navigation.ui.NavigationUI.setupWithNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class FunctionActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        //切換nav頁面
        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_Fragment);
        NavController navController=navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        setupWithNavController(bottomNavigationView,navController);
        //bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            int userId = bundle.getInt("UserId");
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("UserId", userId);
            editor.apply();
        }
    }

}