package com.example.collegedocumentsharing;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.collegedocumentsharing.models.GroupsModel;
import com.example.collegedocumentsharing.models.MemberModel;
import com.example.collegedocumentsharing.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class HomeBottomSheet extends BottomSheetDialogFragment {

    AlertDialog alert;

    Button joinButton,createButton;
    TextView JoinCodeTextView,GroupNameTextView;
    ProgressBar JoinGroupProgressBar, CreateGroupProgressBar;

    String username, email, userId;
    private FirebaseUser user;
    private DatabaseReference reference;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference groupRef = FirebaseFirestore.getInstance().collection("groups").document();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_bottom_sheet_layout,null);

        TextView createGroup = v.findViewById(R.id.create_group_textview);
        TextView joinGroup = v.findViewById(R.id.join_group_textview);

        //Shows Crete Group Alert Dialog
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertLayout = new AlertDialog.Builder(v.getContext());
                v = LayoutInflater.from(v.getContext()).inflate(R.layout.create_group,null);
                alertLayout.setView(v);
                alert = alertLayout.create();
                alert.show();

                createButton = v.findViewById(R.id.create_group_button);
                GroupNameTextView = v.findViewById(R.id.group_name_edit_text);
                CreateGroupProgressBar = v.findViewById(R.id.create_group_progressbar);

                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createGroup();
                    }
                });
            }
        });

        //Shows Join Group Alert Dialog
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertLayout = new AlertDialog.Builder(v.getContext());
                v = LayoutInflater.from(v.getContext()).inflate(R.layout.join_group,null);
                alertLayout.setView(v);
                alert = alertLayout.create();
                alert.show();

                joinButton = v.findViewById(R.id.join_group_button);
                JoinCodeTextView = v.findViewById(R.id.join_code_edit_text);
                JoinGroupProgressBar = v.findViewById(R.id.join_group_progressbar);

                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        joinGroup();
                    }
                });
            }
        });

        //Gets user info
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference =  FirebaseDatabase
                .getInstance("https://collegedocumentsharing-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    username = userProfile.username;
                    email = userProfile.email;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }

    //Handles Create Group
    private void createGroup() {

        String GroupName = GroupNameTextView.getText().toString();

        // validations for input Group Name
        if (TextUtils.isEmpty(GroupName.trim())) {
            GroupNameTextView.setError("Group Name is required");
            GroupNameTextView.requestFocus();
            return;
        }
        CreateGroupProgressBar.setVisibility(View.VISIBLE);

        //Generate a random string
        String unique = UUID.randomUUID().toString();
        String JoinCode = unique.substring(28);

        //Gets autogenerated group id
        String groupId = groupRef.getId();

        //Sets group into database
        groupRef.set(new GroupsModel(GroupName, JoinCode, userId, Arrays.asList(userId)));

        DocumentReference memberRef = FirebaseFirestore.getInstance().collection("groups")
                .document(groupId).collection("members").document(userId);

        //Sets member into database
        memberRef.set(new MemberModel(username,true));

        CreateGroupProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Group Created", Toast.LENGTH_SHORT).show();
        alert.dismiss();
        dismiss();
    }

    //Handles Join Group
    private void joinGroup() {
        String JoinCode = JoinCodeTextView.getText().toString();

        // validations for input Group Join Code
        if (TextUtils.isEmpty(JoinCode)) {
            JoinCodeTextView.setError("Group Join Code is required");
            JoinCodeTextView.requestFocus();
            return;
        }
        JoinGroupProgressBar.setVisibility(View.VISIBLE);

        // Check if group with the code exists
        // If group exists update member field with current user and join group else throw error
        db.collection("groups").whereEqualTo("code",JoinCode).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size()!=0) {
                            for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                                addMember(snap);
                                JoinGroupProgressBar.setVisibility(View.GONE);
                            }
                        }else{
                            Toast.makeText(getActivity(),"Error Occurred!! Check Code and Try Again",Toast.LENGTH_LONG).show();
                            JoinGroupProgressBar.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Error Occurred!! Check Code and Try Again",Toast.LENGTH_LONG).show();
                        JoinGroupProgressBar.setVisibility(View.GONE);
                    }
                });
        alert.dismiss();

    }

    //Add a new Member
    private void addMember(QueryDocumentSnapshot snap) {
        String groupId = snap.getId();
        DocumentReference JoinGroupRef = FirebaseFirestore.getInstance().collection("groups")
                .document(groupId);


        DocumentReference memberRef = FirebaseFirestore.getInstance().collection("groups")
                .document(groupId).collection("members").document(userId);
        memberRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    //Checks if user already exists
                    if(document.exists()){
                        Toast.makeText(getActivity(),"Already Joined",Toast.LENGTH_LONG).show();
                    }else{
                        //Sets database with member info
                        JoinGroupRef.update("members", FieldValue.arrayUnion(userId));
                        memberRef.set(new MemberModel(username,false));
                        Toast.makeText(getActivity(), "Group Joined", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Error Occurred!! Check Code and Try Again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
