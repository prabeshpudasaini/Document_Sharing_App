package com.example.collegedocumentsharing.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegedocumentsharing.adapters.FileRecyclerViewAdapter;
import com.example.collegedocumentsharing.R;
import com.example.collegedocumentsharing.UploadFileDialogScreen;
import com.example.collegedocumentsharing.models.UploadModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FileFragment extends Fragment{

    TextView textGroupTitle, textJoinCode,emptyView;

    ImageButton copyTextImageButton;
    String className,joinCode,groupId;
    FloatingActionButton uploadFab;

    UploadFileDialogScreen fragment;

    private RecyclerView recyclerView;
    private FileRecyclerViewAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference fileReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Gets Data from Bundle from FileHostFragment
        Bundle b = this.getArguments();
        if (b != null) {
            className = b.getString("Class Name", "Class Name");
            joinCode = b.getString("Join Code", "Class Name");
            groupId = b.getString("GroupId", "GroupId");
        }
        fileReference = db.collection("groups").document(groupId).collection("files");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file,null);

        emptyView = v.findViewById(R.id.empty_textview);

        textGroupTitle = v.findViewById(R.id.text_group_title);
        textJoinCode = v.findViewById(R.id.text_join_code);


        copyTextImageButton = v.findViewById(R.id.copy_join_code);

        uploadFab = v.findViewById(R.id.upload_file_fab);

        //Setup Recycler View
        recyclerView = v.findViewById(R.id.file_card);

        setUpRecyclerView();

        //Sets data to bundle and sends to Dialog Screen
        fragment = new UploadFileDialogScreen();
        Bundle bundle = new Bundle();
        bundle.putString("Class Name", className);
        bundle.putString("Join Code", joinCode);
        bundle.putString("GroupId", groupId);

        fragment.setArguments(bundle);

        textGroupTitle.setText(className);
        textJoinCode.setText(joinCode);

        //Copy the code to Clipboard
        copyTextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Sets data to Clipboard
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getContext().CLIPBOARD_SERVICE);
                String text = textJoinCode.getText().toString();
                ClipData clip = ClipData.newPlainText("Join Code", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Text Copied To Clipboard",
                        Toast.LENGTH_LONG).show();
            }
        });

        uploadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Shows Upload File Dialog Screen
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                fragment.show(ft,"dialog");
            }
        });

        return v;
    }

    //Setup RecyclerView
    private void setUpRecyclerView() {

        //Gets file and orders it by uploaded_date in descending order
        Query query = fileReference.orderBy("uploaded_date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<UploadModel> options = new FirestoreRecyclerOptions.Builder<UploadModel>()
                .setQuery(query, UploadModel.class)
                .build();

        adapter = new FileRecyclerViewAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        //Handles Empty RecyclerView
        adapter.setOnEmptyView(new FileRecyclerViewAdapter.EmptyViewInterface() {
            @Override
            public void onEmptyView(Boolean isEmpty) {
                if(isEmpty){
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append("No data!! Click ")
                            .append(" ", new ImageSpan(getActivity(), R.drawable.ic_baseline_add_circle_24), 0)
                            .append(" Button to Upload a New File");

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.card_view_delete){
            Toast.makeText(getActivity(), "Card Deleted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
