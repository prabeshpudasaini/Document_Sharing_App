package com.example.collegedocumentsharing.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegedocumentsharing.fragments.HomeFragment;
import com.example.collegedocumentsharing.fragments.ProfileFragment;
import com.example.collegedocumentsharing.R;
import com.example.collegedocumentsharing.fragments.SettingsFragment;
import com.example.collegedocumentsharing.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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

        //Sets Default Fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,new HomeFragment());
        ft.commit();

        firebaseAuth = FirebaseAuth.getInstance();

        drawerlayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        //gets Header view
        View navHeader = navigationView.getHeaderView(0);

        UserPhoto = navHeader.findViewById(R.id.user_photo);
        UsernameTextView = navHeader.findViewById(R.id.text_username);
        UserEmailTextView = navHeader.findViewById(R.id.text_useremail);

        //Checks App Theme and Sets Selected Theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("themes_switch","");
        if(theme.equals("Light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if(theme.equals("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if(theme.equals("System Default")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        //Check Auth state
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

        //Reads User Data
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

        UsernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content,new ProfileFragment());
                ft.commit();
                drawerlayout.closeDrawers();
            }
        });

        //Setup navigation Drawer
        setSupportActionBar(toolbar);

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
                    drawerlayout.closeDrawers();
                    return true;


                }
                if(id==R.id.nav_profile){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_content,new ProfileFragment());
                    ft.commit();
                    drawerlayout.closeDrawers();
                    return true;

                }
                if(id==R.id.nav_settings){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_content,new SettingsFragment());
                    ft.commit();
                    drawerlayout.closeDrawers();
                    return true;

                }
                if(id==R.id.logout){
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Logout")
                            .setMessage("Do You Want To Logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Sign Out
                                    firebaseAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                    drawerlayout.closeDrawers();
                    return true;
                }

                drawerlayout.closeDrawers();
                return false;
            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchableActivity.class)));
        searchView.setQueryRefinementEnabled(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String word = newText;
                if (word.length() == 0){
                    return true;
                }
                String [] closestWord = returnSuggestions(word, 3);
                if (closestWord != null){
//                    firstSuggestion.setText(closestWord[0]);
//                    secondSuggestion.setText(closestWord[1]);
//                    thirdSuggestion.setText(closestWord[2]);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Checks Auth State
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

        //Sets Default Fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,new HomeFragment());
        ft.commit();
    }

    //Algorithm
    int levenshtein(String token1, String token2) {
        int[] distances = new int[token1.length() + 1];

        for (int t1 = 1; t1 <= token1.length(); t1++) {
            if (token1.charAt(t1 - 1) == token2.charAt(0)) {
                distances[t1] = calcMin(distances[t1 - 1], t1 - 1, t1);
            } else {
                distances[t1] = calcMin(distances[t1 - 1], t1 - 1, t1) + 1;
            }
        }

        int dist = 0;
        for (int t2 = 1; t2 < token2.length(); t2++) {
            dist = t2 + 1;
            for (int t1 = 1; t1 <= token1.length(); t1++) {
                int tempDist;
                if (token1.charAt(t1 - 1) == token2.charAt(t2)) {
                    tempDist = calcMin(dist, distances[t1 - 1], distances[t1]);
                } else {
                    tempDist = calcMin(dist, distances[t1 - 1], distances[t1]) + 1;
                }
                distances[t1 - 1] = dist;
                dist = tempDist;
            }
            distances[token1.length()] = dist;
        }
        return dist;
    }

    static int calcMin(int a, int b, int c) {
        if (a <= b && a <= c) {
            return a;
        } else if (b <= a && b <= c) {
            return b;
        } else {
            return c;
        }
    }

    String [] returnSuggestions(String word, int numWords){
        String[] dictWordDist = new String[20000];
        BufferedReader reader;
        int wordIdx = 0;
        try {
            int wordDistance;
            reader = new BufferedReader(new InputStreamReader(getAssets().open("20k.txt")));
            String line = reader.readLine();
            while (line != null) {
                wordDistance = levenshtein(line.trim(), word);
                dictWordDist[wordIdx] = wordDistance + "-" + line.trim();
                line = reader.readLine();
                wordIdx++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Failed to read the file.");
            e.printStackTrace();
            return null;
        }
        Arrays.sort(dictWordDist);
        String[] closestWords = new String[numWords];
        String currWordDist;
        for (int i = 0; i < numWords; i++) {
            currWordDist = dictWordDist[i];
            String[] wordDetails = currWordDist.split("-");
            closestWords[i] = wordDetails[1];
            System.out.println(wordDetails[0] + " " + wordDetails[1]);
        }
        return closestWords;
    }

//    public void selectWord(View view){
//        Button button = (Button) view;
//        enteredWord.setText(button.getText() + " ");
//        enteredWord.setSelection(enteredWord.getText().length());
//    }

}