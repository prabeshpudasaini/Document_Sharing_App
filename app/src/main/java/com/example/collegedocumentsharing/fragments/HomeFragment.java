package com.example.collegedocumentsharing.fragments;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private RecyclerView recyclerView;
    TextView emptyView;
    private GroupRecyclerViewAdapter adapter;
    private FragmentTransaction ft;
    private ArrayList<GroupsModel> groupsArrayList;

    HomeFragment fragment;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference groupReference = db.collection("groups");
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

//    Query query;


//    private DocumentReference groupReference = FirebaseFirestore.getInstance().collection("groups")
//            .document();




//    private String[] data ={"Prabesh","Om","Albish","Saroj","Dipesh",
//            "Sunil","Sachin","Milan","Sanal","Sandesh",
//            "Prabesh","Om","Albish","Saroj","Dipesh",
//            "Sunil","Sachin","Milan","Sanal","Sandesh",
//            "Prabesh","Om","Albish","Saroj","Dipesh",
//            "Sunil","Sachin","Milan","Sanal","Sandesh",};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,null);

        emptyView = v.findViewById(R.id.empty_textview);

        firebaseAuth = FirebaseAuth.getInstance();

        //Setup Recycler View
        recyclerView = v.findViewById(R.id.home_card);
        groupsArrayList = new ArrayList<GroupsModel>();
        FloatingActionButton fab = v.findViewById(R.id.fab);

        setUpRecyclerView();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                HomeBottomSheet bottomSheet = new HomeBottomSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });


        return v;


    }

    private void setUpRecyclerView() {

//        db.collectionGroup("members").whereEqualTo("owner",
//                true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                query = queryDocumentSnapshots.getQuery();
//
//                FirestoreRecyclerOptions<GroupsModel> options = new FirestoreRecyclerOptions.Builder<GroupsModel>()
//                        .setQuery(query, GroupsModel.class)
//                        .build();
//
//                adapter = new GroupRecyclerViewAdapter(options);
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                recyclerView.setAdapter(adapter);
//
//                adapter.setOnCardViewClickListener(new GroupRecyclerViewAdapter.CardViewClickInterface() {
//                    @Override
//                    public void onCardViewClick(DocumentSnapshot documentSnapshot, int position) {
//                        ClassHostFragment fragment = new ClassHostFragment();
//
//                        GroupsModel group = documentSnapshot.toObject(GroupsModel.class);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("Class Name", group.getName());
//                        bundle.putString("Join Code", group.getCode());
//                        bundle.putString("GroupId", documentSnapshot.getId());
//                        fragment.setArguments(bundle);
//
//                        ft = getActivity().getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.main_content,fragment);
//                        ft.commit();
//                    }
//                });
//
//            }
//        });

//                query = db.collectionGroup("groups").whereEqualTo("owner",
//                firebaseAuth.getCurrentUser().toString());

//                Query query  = groupReference.orderBy("name", Query.Direction.DESCENDING);

//                Query query = groupReference.document().collection("members")
//                        .whereEqualTo("owner",true);

//                groupReference
//                .whereEqualTo("owner",firebaseAuth.getCurrentUser().getUid())
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if(task.isSuccessful()){
//                                    for(QueryDocumentSnapshot document : task.getResult()){
////                                        GroupsModel group = document.toObject(GroupsModel.class);
//                                        Log.d("Document", document.getId() + " => " + document.getReference().getParent());
//                                    }
//                                }else{
//                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });

        Query query = groupReference.whereArrayContains("members",firebaseAuth.getCurrentUser().getUid());

                FirestoreRecyclerOptions<GroupsModel> options = new FirestoreRecyclerOptions.Builder<GroupsModel>()
                        .setQuery(query, GroupsModel.class)
                        .build();

                adapter = new GroupRecyclerViewAdapter(options);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                adapter.setOnCardViewClickListener(new GroupRecyclerViewAdapter.CardViewClickInterface() {
                    @Override
                    public void onCardViewClick(DocumentSnapshot documentSnapshot, int position) {
                        ClassHostFragment fragment = new ClassHostFragment();

                        GroupsModel group = documentSnapshot.toObject(GroupsModel.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("Class Name", group.getName());
                        bundle.putString("Join Code", group.getCode());
                        bundle.putString("GroupId", documentSnapshot.getId());
                        fragment.setArguments(bundle);

                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_content,fragment);
                        ft.commit();
                    }
                });

                adapter.setOnEmptyView(new GroupRecyclerViewAdapter.EmptyViewInterface() {
                    @Override
                    public void onEmptyView(Boolean isEmpty) {
                        if(isEmpty){
                            Log.d("Document","is empty");
                            SpannableStringBuilder builder = new SpannableStringBuilder();
                            builder.append("No data!! Click ")
                                    .append(" ", new ImageSpan(getActivity(), R.drawable.ic_baseline_add_circle_24), 0)
                                    .append(" Button to Create a New Group \n or Join an Existing one");

                            emptyView.setText(builder);
                            emptyView.setVisibility(View.VISIBLE);

                        }else{
                            Log.d("Document","is not empty");
                            emptyView.setVisibility(View.GONE);

                        }
                    }
                });

    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

//        groupReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(value.exists()){
////                    ArrayList<GroupsModel> groups = value.toObject(GroupsModel.class);
////
////                    adapter = new MyRecyclerViewAdapter(getActivity(),groups,this);
////                    recyclerView.setAdapter(adapter);
//                }
//            }
//        });
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.card_view_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//        if(id==R.id.card_view_delete){
//            Toast.makeText(getActivity(), "Card Deleted", Toast.LENGTH_SHORT).show();
//        }
//        return true;
//    }

}

