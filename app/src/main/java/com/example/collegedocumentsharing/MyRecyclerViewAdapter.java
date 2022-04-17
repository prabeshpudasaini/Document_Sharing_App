package com.example.collegedocumentsharing;


import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends FirestoreRecyclerAdapter<GroupsModel, MyRecyclerViewAdapter.MyViewHolder> {

    private ClassCardViewInterface classCardViewInterface;

    Context c;
    private ArrayList<GroupsModel> groupsArrayList = new ArrayList<GroupsModel>();


//    public MyRecyclerViewAdapter(Context m, ArrayList<GroupsModel> groups){
//        c = m;
//        this.groupsArrayList = groups;
//        this.classCardViewInterface = classCardViewInterface;
//
//    }

    public MyRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<GroupsModel> options) {
        super(options);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(c).inflate(R.layout.single_card,null);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull GroupsModel model) {

        holder.tv.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return groupsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        View v;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            v = itemView;

            //Implementing Onclick Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Making sure the Interface isn't null
                    if(classCardViewInterface != null){

                        //Getting position for the interface
                        int pos = getAdapterPosition();

                        //Making sure the position is valid
                        if (pos != RecyclerView.NO_POSITION){
                            classCardViewInterface.OnClassItemClick(getSnapshots().getSnapshot(pos),pos);
                        }
                    }
                }
            });
        }
    }
}
