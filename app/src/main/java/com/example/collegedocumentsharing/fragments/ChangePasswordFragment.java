package com.example.collegedocumentsharing.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    EditText old_password_EditText, new_password_EditText;
    Button save;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password,null);

        old_password_EditText = v.findViewById(R.id.old_password_edit_text);
        new_password_EditText = v.findViewById(R.id.new_password_edit_text);
        save = v.findViewById(R.id.save);
        progressBar = v.findViewById(R.id.progressbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                String oldPassword = old_password_EditText.getText().toString();
                String newPassword = new_password_EditText.getText().toString();

                if (TextUtils.isEmpty(oldPassword)) {
                    old_password_EditText.setError("Old Password is required");
                    old_password_EditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(newPassword)) {

                    new_password_EditText.setError("Password is required");
                    new_password_EditText.requestFocus();
                    return;
                }
                if(newPassword.length()<6){
                    new_password_EditText.setError("Password must be at least 6 characters");
                    new_password_EditText.requestFocus();
                    return;
                }

                // Get auth credentials from the user for re-authentication.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldPassword);

                progressBar.setVisibility(View.VISIBLE);

                // Prompt the user to re-provide their sign-in credentials
                //Checks if old password is right
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    //Updates the password
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(),"Password updated Successfully",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"Error updating Password",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(getActivity(),"Wrong Password",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                progressBar.setVisibility(View.GONE);
            }
        });
        return v;
    }
}
