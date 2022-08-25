package com.example.collegedocumentsharing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collegedocumentsharing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EditUsernameFragment extends Fragment {

    String username;
    EditText username_EditText;
    Button save;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_username,null);

        username_EditText = v.findViewById(R.id.username_edit_text);
        progressBar = v.findViewById(R.id.progressbar);
        save = v.findViewById(R.id.save);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = username_EditText.getText().toString();
                if (TextUtils.isEmpty(username)) {

                    username_EditText.setError("Username is required");
                    username_EditText.requestFocus();
                    return;
                }

                //Calling Firebase Database object and provide name of collection
                FirebaseDatabase.getInstance("https://collegedocumentsharing-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("username").setValue(username_EditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(),
                                                    "Username Updated Successfully",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);

                                }else {
                                    Toast.makeText(getActivity(),
                                                    "Error Updating Username",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

            }
        });

        return v;
    }
}
