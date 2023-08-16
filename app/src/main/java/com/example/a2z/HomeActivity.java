package com.example.a2z;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {
    TabLayout tab;
    ViewPaggerAdapter viewPaggerAdapter;
    ViewPager2 viewPager;
    ImageView refine;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toast.makeText(this, "LOGGED IN", Toast.LENGTH_SHORT).show();
        tab=findViewById(R.id.tabLayout);
        refine=findViewById(R.id.refine);
        //adapter setting
        viewPager=findViewById(R.id.viewPager);
        viewPaggerAdapter=new ViewPaggerAdapter(HomeActivity.this);
        viewPager.setAdapter(viewPaggerAdapter);
        //Tab Title


        String [] tabTtiles={"Mark Attendance","Attendance History"};
        new TabLayoutMediator(tab, viewPager,(tab, position) ->
                tab.setText(tabTtiles[position])).attach();
        // tab.setupWithViewPager(viewPager);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        refine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent myIntent = new Intent(HomeActivity.this, refine_page.class);
                //Optional parameters
                //startActivity(myIntent);
            }
        });
    }
}