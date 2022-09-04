package com.example.collegedocumentsharing.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegedocumentsharing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button loginButton;
    private TextView createAccount, forgotPassword;
    private EditText emailTextView, passwordTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.login_button);
        createAccount = findViewById(R.id.create_account_textview);
        forgotPassword = findViewById(R.id.forgot_password_textview);

        emailTextView = findViewById(R.id.email_edit_text);
        passwordTextView = findViewById(R.id.password_edit_text);
        progressBar = findViewById(R.id.progressbar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

    }

    //Handles Login
    private void loginUserAccount() {

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            emailTextView.setError("Email is required");
            emailTextView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordTextView.setError("Password is required");
            passwordTextView.requestFocus();
            return;
        }

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        //Logins User
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),
                                            "Login successful!!",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);

                            // if sign-in is successful
                            // intent to home activity
                            Intent intent
                                    = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            // sign-in failed
                            Toast.makeText(getApplicationContext(),
                                            "Login failed!! Check Email or Password",
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
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }
    }
}