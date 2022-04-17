package com.example.collegedocumentsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class ClassActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private FragmentTransaction ft;
    private DrawerLayout drawerlayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle at;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);


        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_class_content,new ClassFragment());
        ft.commit();


        drawerlayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Navigation Project");

        at = new ActionBarDrawerToggle(this,drawerlayout, toolbar, R.string.open,R.string.close);
        drawerlayout.addDrawerListener(at);
        at.syncState();
        

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.bottom_nav_class){

                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,new ClassFragment());
                    ft.commit();

                }
                if(id==R.id.bottom_nav_dashboard){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,new DashboardFragment());
                    ft.commit();

                }
                if(id==R.id.bottom_nav_notifications){


                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,new NotificationFragment());
                    ft.commit();
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.nav_home){
                    Intent i = new Intent(ClassActivity.this, LoginActivity.class);
                    i.putExtra("fragment", "home");
                    startActivity(i);

                }
                if(id==R.id.nav_gallery){
                    Intent i = new Intent(ClassActivity.this, LoginActivity.class);
                    i.putExtra("fragment", "gallery");
                    startActivity(i);

                }
                if(id==R.id.nav_slideshow){
                    Intent i = new Intent(ClassActivity.this, LoginActivity.class);
                    i.putExtra("fragment", "slideshow");
                    startActivity(i);
                }
                if(id==R.id.logout){
                    Intent i = new Intent(ClassActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                drawerlayout.closeDrawers();
                return false;
            }

        });


    }
}