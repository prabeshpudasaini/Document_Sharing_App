package com.example.collegedocumentsharing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegedocumentsharing.models.MemberModel;
import com.example.collegedocumentsharing.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class MemberRecyclerViewAdapter extends FirestoreRecyclerAdapter<MemberModel, MemberRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<MemberModel> memberArrayList = new ArrayList<MemberModel>();


    public MemberRecyclerViewAdapter(FirestoreRecyclerOptions<MemberModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MemberModel model) {
        if(model.getOwner()==true){
            holder.tv.setText(model.getUsername()+"(Owner)");
        }else{
            holder.tv.setText(model.getUsername());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_member
                ,parent,false);
        return new MemberRecyclerViewAdapter.MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.member_name);

        }
    }
}
