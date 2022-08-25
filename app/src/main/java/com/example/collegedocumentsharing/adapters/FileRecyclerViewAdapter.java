package com.example.collegedocumentsharing.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegedocumentsharing.R;
import com.example.collegedocumentsharing.models.UploadModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FileRecyclerViewAdapter extends FirestoreRecyclerAdapter<UploadModel, FileRecyclerViewAdapter.MyViewHolder>{

    private DatabaseReference reference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference fileDownloadUrl;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private EmptyViewInterface empty;



//    String username;

    private ArrayList<UploadModel> fileArrayList = new ArrayList<UploadModel>();

    public FileRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<UploadModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull UploadModel model) {

//        String uploaded_date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(model.getUploaded_date());

        if(!firebaseAuth.getCurrentUser().getUid().equals(model.getUploaded_by())){
            holder.imageButton.setVisibility(View.GONE);
        }
        holder.name.setText(model.getUsername());
        holder.uploaded_date.setText(model.getUploaded_date());
        holder.information.setText(model.getInformation());
        holder.fileName.setText(model.getFileName());
        holder.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Download")
                        .setMessage("Do You Want To Download "+model.getFileName())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fileDownloadUrl = storage.getReferenceFromUrl(model.getFileUrl());
                                downloadFile(view.getContext(), model.getFileName(), Environment.DIRECTORY_DOWNLOADS,model.getFileUrl());
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.imageButton);
                popupMenu.inflate(R.menu.card_view_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if(id==R.id.card_view_delete){
                            new MaterialAlertDialogBuilder(v.getContext())
                                    .setTitle("Delete")
                                    .setMessage("Do You Want To Delete "+model.getFileName())
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
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void deleteCard(int position,View v) {
        getSnapshots().getSnapshot(position).getReference().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(v.getContext(), "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(), "You Don't Have Permission to Delete this File", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_file_card
                ,parent,false);
        return new MyViewHolder(v);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,uploaded_date,information,fileName;
        ImageButton imageButton;
        ImageView profile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name_text_view);
            uploaded_date = itemView.findViewById(R.id.upload_date_text_view);
            information=itemView.findViewById(R.id.user_custom_text_view);
            imageButton = itemView.findViewById(R.id.menu_image_button);
            fileName = itemView.findViewById(R.id.file_name_text_view);
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

    private void downloadFile(Context context, String fileName, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);

        downloadmanager.enqueue(request);
    }

    public interface EmptyViewInterface{
        void onEmptyView(Boolean isEmpty);

    }
    public void setOnEmptyView(EmptyViewInterface empty){
        this.empty = empty;
    }


}
