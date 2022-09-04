package com.example.collegedocumentsharing.fragments;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegedocumentsharing.adapters.GroupRecyclerViewAdapter;
import com.example.collegedocumentsharing.models.GroupsModel;
import com.example.collegedocumentsharing.HomeBottomSheet;
import com.example.collegedocumentsharing.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private RecyclerView recyclerView;
    TextView emptyView;
    private GroupRecyclerViewAdapter adapter;
    private FragmentTransaction ft;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference groupReference = db.collection("groups");
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,null);

        emptyView = v.findViewById(R.id.empty_textview);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = v.findViewById(R.id.home_card);
        FloatingActionButton fab = v.findViewById(R.id.fab);

        //Setup Recycler View
        setUpRecyclerView();

        //Opens BottomSheet
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shows BottomSheet
                HomeBottomSheet bottomSheet = new HomeBottomSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

        return v;


    }

    private void setUpRecyclerView() {


        //Gets groups where current user is registered as a member
        Query query = groupReference.whereArrayContains("members",firebaseAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<GroupsModel> options = new FirestoreRecyclerOptions.Builder<GroupsModel>()
                .setQuery(query, GroupsModel.class)
                .build();

        adapter = new GroupRecyclerViewAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //Handles Clicking the Group Card
        adapter.setOnCardViewClickListener(new GroupRecyclerViewAdapter.CardViewClickInterface() {
            @Override
            public void onCardViewClick(DocumentSnapshot documentSnapshot, int position) {
                FileHostFragment fragment = new FileHostFragment();

                GroupsModel group = documentSnapshot.toObject(GroupsModel.class);

                //Sets a Bundle and sends to FileHostFragment
                Bundle bundle = new Bundle();
                bundle.putString("Class Name", group.getName());
                bundle.putString("Join Code", group.getCode());
                bundle.putString("GroupId", documentSnapshot.getId());
                fragment.setArguments(bundle);

                //Opens Fragment
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content,fragment);
                ft.commit();
            }
        });


        //Handles Empty View
        adapter.setOnEmptyView(new GroupRecyclerViewAdapter.EmptyViewInterface() {
            @Override
            public void onEmptyView(Boolean isEmpty) {
                if(isEmpty){

                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append("No data!! Click ")
                            .append(" ", new ImageSpan(getActivity(),
                                    R.drawable.ic_baseline_add_circle_24), 0)
                            .append(" Button to Create a New Group \n or Join an Existing one");

                    emptyView.setText(builder);
                    emptyView.setVisibility(View.VISIBLE);

                }else{
                    emptyView.setVisibility(View.GONE);

                }
            }
        });
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.card_view_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

