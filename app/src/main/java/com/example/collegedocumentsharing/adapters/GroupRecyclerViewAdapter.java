package com.example.collegedocumentsharing.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegedocumentsharing.models.GroupsModel;
import com.example.collegedocumentsharing.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class GroupRecyclerViewAdapter extends FirestoreRecyclerAdapter<GroupsModel, GroupRecyclerViewAdapter.MyViewHolder> {

    private CardViewClickInterface listener;
    private EmptyViewInterface empty;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    Context c;
    private ArrayList<GroupsModel> groupsArrayList = new ArrayList<GroupsModel>();


    public GroupRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<GroupsModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull GroupsModel model) {


        holder.tv.setText(model.getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.imageButton);
                popupMenu.inflate(R.menu.card_view_menu);

                if(!firebaseAuth.getCurrentUser().getUid().equals(model.getOwner())){
                    popupMenu.getMenu().findItem(R.id.card_view_leave).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.card_view_delete).setVisible(false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if(id==R.id.card_view_delete){
                            new MaterialAlertDialogBuilder(v.getContext())
                                    .setTitle("Delete")
                                    .setMessage("Do You Want To Delete "+model.getName())
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            deleteCard(holder.getAdapterPosition(),v);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).show();
                        }
                        if(id==R.id.card_view_leave){
                            new MaterialAlertDialogBuilder(v.getContext())
                                    .setTitle("Leave Group")
                                    .setMessage("Do You Want To Leave "+model.getName())
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            leaveGroup(holder.getAdapterPosition(),v);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void leaveGroup(int position, View v) {
        getSnapshots().getSnapshot(position).getReference().update("members", FieldValue
                .arrayRemove(firebaseAuth.getCurrentUser().getUid()));
        getSnapshots().getSnapshot(position).getReference().collection("members")
                .document(firebaseAuth.getCurrentUser().getUid()).delete();
        Toast.makeText(v.getContext(), "Group Left Successfully", Toast.LENGTH_SHORT).show();

    }

    private void deleteCard(int position,View v) {
        getSnapshots().getSnapshot(position).getReference().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(v.getContext(), "Group Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(v.getContext(), "You Don't Have Permission to Delete this Group",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card
        ,parent,false);

        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv,emptyView;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.group_name_text_view);
            imageButton = itemView.findViewById(R.id.menu_image_button);
            emptyView = itemView.findViewById(R.id.empty_textview);

            //Implementing Onclick Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Getting position for the interface
                    int position = getAdapterPosition();

//                    Making sure the position is valid and Making sure the Interface isn't null
                    if (position != RecyclerView.NO_POSITION && listener != null){
                            listener.onCardViewClick(getSnapshots().getSnapshot(position),position);
                        }
                }
            });
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getSnapshots().isEmpty()){
//            Log.d("Document","is empty");
            empty.onEmptyView(true);
        }else{
//            Log.d("Document","is not empty");
            empty.onEmptyView(false);

        }
    }

    public interface CardViewClickInterface{
        void onCardViewClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnCardViewClickListener(CardViewClickInterface listener){
        this.listener = listener;

    }

    public interface EmptyViewInterface{
        void onEmptyView(Boolean isEmpty);

    }
    public void setOnEmptyView(EmptyViewInterface empty){
        this.empty = empty;
    }

}
