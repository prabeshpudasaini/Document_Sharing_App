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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeBottomSheet extends BottomSheetDialogFragment {

    AlertDialog alert;

    Button joinButton,createButton;
    TextView JoinCodeTextView,GroupNameTextView;
    ProgressBar JoinGroupProgressBar, CreateGroupProgressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference groupReference = FirebaseFirestore.getInstance().document("groups/group");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_bottom_sheet_layout,null);



        TextView createGroup = v.findViewById(R.id.create_group_textview);
        TextView joinGroup = v.findViewById(R.id.join_group_textview);


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

        return v;
    }

    private void createGroup() {

        String GroupName = GroupNameTextView.getText().toString();

        // validations for input Group Name
        if (TextUtils.isEmpty(GroupName.trim())) {
            GroupNameTextView.setError("Group Name is required");
            GroupNameTextView.requestFocus();
            return;
        }
        CreateGroupProgressBar.setVisibility(View.VISIBLE);

//        Map<String, Object> group = new HashMap<>();
//        group.put("name",GroupName);
//
//        groupReference.set(group).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(getActivity(), "Group Created", Toast.LENGTH_SHORT).show();
//                    alert.dismiss();
//
//                    CreateGroupProgressBar.setVisibility(View.GONE);
//                }else{
//                    Toast.makeText(getActivity(), "Error Creating Group", Toast.LENGTH_SHORT).show();
//                    alert.dismiss();
//
//                    CreateGroupProgressBar.setVisibility(View.GONE);
//
//                }
//            }
//        });
//
        CollectionReference groupRef = FirebaseFirestore.getInstance()
                .collection("groups");
        groupRef.add(new GroupsModel(GroupName));

        CreateGroupProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Group Created", Toast.LENGTH_SHORT).show();
        alert.dismiss();









    }

    private void joinGroup() {
        String JoinCode = JoinCodeTextView.getText().toString();

        // validations for input Group Join Code
        if (TextUtils.isEmpty(JoinCode)) {
            JoinCodeTextView.setError("Group Join Code is required");
            JoinCodeTextView.requestFocus();
            return;
        }
        JoinGroupProgressBar.setVisibility(View.VISIBLE);

        JoinGroupProgressBar.setVisibility(View.GONE);

        Toast.makeText(getActivity(), "Group Joined", Toast.LENGTH_SHORT).show();
        alert.dismiss();

    }


}
