package com.example.collegedocumentsharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegedocumentsharing.models.MemberModel;
import com.example.collegedocumentsharing.adapters.MemberRecyclerViewAdapter;
import com.example.collegedocumentsharing.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MemberFragment extends Fragment {


    private RecyclerView recyclerView;
    private MemberRecyclerViewAdapter adapter;
    private ArrayList<MemberModel> membersArrayList;

    String groupId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference memberReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null) {
            groupId = b.getString("GroupId", "GroupId");
        }
        memberReference = db.collection("groups").document(groupId).collection("members");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard,null);

        //Setup Recycler View
        recyclerView = v.findViewById(R.id.members);
        membersArrayList = new ArrayList<MemberModel>();

        setUpRecyclerView();

        return v;
    }

    private void setUpRecyclerView() {
        Query query = memberReference.orderBy("username", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MemberModel> options = new FirestoreRecyclerOptions.Builder<MemberModel>()
                .setQuery(query, MemberModel.class)
                .build();


        adapter = new MemberRecyclerViewAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
