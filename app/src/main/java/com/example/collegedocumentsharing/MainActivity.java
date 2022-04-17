package com.example.collegedocumentsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentTransaction ft;
    private ActionBarDrawerToggle at;


    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    TextView UsernameTextView, UserEmailTextView;
    ImageView UserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,new HomeFragment());
        ft.commit();

        firebaseAuth = FirebaseAuth.getInstance();

        drawerlayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);

        UserPhoto = navHeader.findViewById(R.id.user_photo);
        UsernameTextView = navHeader.findViewById(R.id.text_username);
        UserEmailTextView = navHeader.findViewById(R.id.text_useremail);

//        FloatingActionButton fab = findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference =  FirebaseDatabase.getInstance("https://collegedocumentsharing-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String username = userProfile.username;
                    String email = userProfile.email;

                    UsernameTextView.setText(username);
                    UserEmailTextView.setText(email);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something wrong Happened!"
                        ,Toast.LENGTH_LONG).show();
            }
        });

//        if (user != null) {
//            // Name, email address, and profile photo Url
////            String name = user.getDisplayName();
//            String email = user.getEmail();
////            Uri photoUrl = user.getPhotoUrl();
//
////            UserPhoto.setImageURI(photoUrl);
////            UsernameTextView.setText(name);
////            UserEmailTextView.setText(email);
//        }else{
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            finish();
//        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Navigation Project");

        at = new ActionBarDrawerToggle(this,drawerlayout, toolbar, R.string.open,R.string.close);
        drawerlayout.addDrawerListener(at);
        at.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.nav_home){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_content,new HomeFragment());
                    ft.commit();

                }
                if(id==R.id.nav_gallery){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_content,new ProfileFragment());
                    ft.commit();

                }
                if(id==R.id.nav_slideshow){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_content,new SettingsFragment());
                    ft.commit();
                }
                if(id==R.id.logout){
//                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(i);

                    //Sign Out
                    firebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }

                drawerlayout.closeDrawers();
                return false;
            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,new HomeFragment());
        ft.commit();

//        String fragment = getIntent().getExtras().getString("fragment");
//
//
//        switch(fragment){
//            case "home":
//                ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.main_content,new HomeFragment());
//                ft.commit();
//                break;
//            case "gallery":
//                ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.main_content,new ProfileFragment());
//                ft.commit();
//                break;
//            case "slideshow":
//                ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.main_content,new SettingsFragment());
//                ft.commit();
//                break;
//        }
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//        if(id==R.id.logout){
//
//            Intent i = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(i);
//        }
//        return true;
//    }


}