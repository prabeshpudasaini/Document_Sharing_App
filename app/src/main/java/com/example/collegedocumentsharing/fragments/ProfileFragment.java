package com.example.collegedocumentsharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegedocumentsharing.R;
import com.example.collegedocumentsharing.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    EditText email_EditText,username_EditText,password_EditText;

    private FragmentTransaction ft;

    private String userId,username,email;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,null);

        email_EditText = v.findViewById(R.id.email_edit_text);
        username_EditText = v.findViewById(R.id.username_edit_text);
        password_EditText = v.findViewById(R.id.password_edit_text);

        //Gets users info
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference =  FirebaseDatabase.getInstance("https://collegedocumentsharing-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    username = userProfile.username;
                    email = userProfile.email;

                    username_EditText.setText(username);
                    email_EditText.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        username_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUsername();
            }
        });

        password_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        return v;
    }

    //Opens fragment
    private void changePassword() {
        ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,new ChangePasswordFragment());
        ft.commit();
    }

    //Opens fragment
    private void editUsername() {
        ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,new EditUsernameFragment());
        ft.commit();
    }

}
