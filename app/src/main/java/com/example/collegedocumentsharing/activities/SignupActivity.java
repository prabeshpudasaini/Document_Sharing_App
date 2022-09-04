package com.example.collegedocumentsharing.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegedocumentsharing.R;
import com.example.collegedocumentsharing.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;

    private EditText usernameTextView, emailTextView, passwordTextView;
    private ProgressBar progressBar;
    private Button signupButton;
    private TextView HaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        usernameTextView = findViewById(R.id.username_edit_text);
        emailTextView = findViewById(R.id.email_edit_text);
        passwordTextView = findViewById(R.id.password_edit_text);
        signupButton = findViewById(R.id.signup_button);
        HaveAccount = findViewById(R.id.have_account_textview);
        progressBar = findViewById(R.id.progressbar);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

        HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    //Register User
    private void registerNewUser() {

        // Take the value of two edit texts in Strings
        String username, email, password;

        username = usernameTextView.getText().toString().trim();
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // Validations for input username, email and password
        if (TextUtils.isEmpty(username)) {
            usernameTextView.setError("Username is required");
            usernameTextView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailTextView.setError("Email is required");
            emailTextView.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTextView.setError("Provide Valid Email");
            emailTextView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordTextView.setError("Password is required");
            passwordTextView.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordTextView.setError("Password must be at least 6 characters");
            passwordTextView.requestFocus();
            return;
        }

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        //Registers a new User
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //Creating User object
                            User user = new User(username,email);

                            //Calling Firebase Database object and provide name of collection
                            FirebaseDatabase.getInstance("https://collegedocumentsharing-default-rtdb.asia-southeast1.firebasedatabase.app")
                                    .getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),
                                                                "Registration successful!",
                                                                Toast.LENGTH_LONG)
                                                        .show();

                                                // hide the progress bar
                                                progressBar.setVisibility(View.GONE);

                                                // if the user created intent to login activity
                                                Intent intent
                                                        = new Intent(SignupActivity.this,
                                                        LoginActivity.class);
                                                startActivity(intent);

                                            }else {
                                                // If sign in fails, display a message to the user.
                                                // Registration failed
                                                Toast.makeText(
                                                                getApplicationContext(),
                                                                "Registration failed!!"
                                                                        + " Please try again later",
                                                                Toast.LENGTH_LONG)
                                                        .show();

                                                // hide the progress bar
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}