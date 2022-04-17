package com.example.collegedocumentsharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ClassCardViewInterface {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private FragmentTransaction ft;
    private ArrayList<GroupsModel> groupsArrayList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference groupReference = db.collection("groups");

//    private DocumentReference groupReference = FirebaseFirestore.getInstance().document("groups/group");




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



        //Setup Recycler View
        recyclerView = v.findViewById(R.id.home_card);

        setUpRecyclerView();

        groupsArrayList = new ArrayList<GroupsModel>();

        FloatingActionButton fab = v.findViewById(R.id.fab);

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
        Query query = groupReference.orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<GroupsModel> options = new FirestoreRecyclerOptions.Builder<GroupsModel>()
                .setQuery(query, GroupsModel.class)
                .build();


        adapter = new MyRecyclerViewAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

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

    @Override
    public void OnClassItemClick(DocumentSnapshot documentSnapshot, int position) {
        //        //Start the Intent
//        Intent i = new Intent(this.getActivity(), ClassActivity.class);
//        i.putExtra("Class Name",data[position]);
//        startActivity(i);



        ClassHostFragment fragment = new ClassHostFragment();
        GroupsModel group = documentSnapshot.toObject(GroupsModel.class);

        Bundle bundle = new Bundle();

        bundle.putString("Class Name", group.getName());
        fragment.setArguments(bundle);

        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ft.commit();

    }
}

