package com.example.collegedocumentsharing;


import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.collegedocumentsharing.models.UploadModel;
import com.example.collegedocumentsharing.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;

public class UploadFileDialogScreen extends DialogFragment {

    private static final int PICK_FILE_REQUEST = 1;

    Dialog dialog;
    UploadFileDialogScreen uploadDialog;

    MaterialToolbar toolbar;
    EditText information;
    TextView addFile;
    ProgressBar progressBar;

    Uri fileUri;

    ListView lv;
    String className,joinCode,groupId;
    String filename,username;
    Long filesize;

    StorageReference fileStorageRef;
    DocumentReference fileDocumentRef;
    StorageTask uploadTask;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userId;
    private FirebaseUser user;
    private DatabaseReference reference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_file_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        information = view.findViewById(R.id.information_edit_text);
        addFile = view.findViewById(R.id.add_file);
        progressBar = view.findViewById(R.id.progressbar);

        //Get user info
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference =  FirebaseDatabase.getInstance("https://collegedocumentsharing-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    username = userProfile.username;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Gets Data from FileFragment
        Bundle b = this.getArguments();
        if (b != null) {
            className = b.getString("Class Name", "Class Name");
            joinCode = b.getString("Join Code", "Class Name");
            groupId = b.getString("GroupId", "GroupId");
        }

        fileStorageRef = FirebaseStorage.getInstance().getReference(groupId);

        //Opens File Chooser
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setTitle("Upload a File");
        toolbar.inflateMenu(R.menu.full_screen_dialog_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //Handles Upload
        toolbar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getActivity(),"Upload in Progress",Toast.LENGTH_LONG).show();
                }else{
                    uploadFile();
                }
                return true;
            }
        });
    }

    //Sets Dialog Fragment
    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    //Opens File Chooser
    private void openFileChooser(){
        Intent intent = new Intent();
        //Accept any type of file
        intent.setType("*/*");
        //Select Multiple File
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //Select Single File
        intent.setAction(Intent.ACTION_GET_CONTENT);

//        Intent.createChooser(intent,"choose");

        //Starts Activity to open file chooser
        startActivityForResult(intent,PICK_FILE_REQUEST);
    }

    //gets result from file chooser
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data!=null
                && data.getData() != null){

            //Gets selected file URI
            fileUri = data.getData();


            //For multiple selection
//            if(data.getClipData() != null) { // checking multiple selection or not
//                for(int i = 0; i < data.getClipData().getItemCount(); i++) {
//                    fileUri = data.getClipData().getItemAt(i).getUri();
//                }
//            } else {
//                fileUri = data.getData();
//            }

            /*
             * Get the file's content URI from the incoming Intent,
             * then query the server app to get the file's display name
             */

            Cursor returnCursor =
                    getActivity().getContentResolver()
                            .query(fileUri, null, null, null, null);
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

            returnCursor.moveToFirst();

            // Gets File Size
            filesize = returnCursor.getLong(sizeIndex);
            Log.d("file","file size is "+Long.toString(returnCursor.getLong(sizeIndex)));

            //Check if file size is greater than 50MB
            if(filesize>50000000){
                Toast.makeText(getActivity(), "File size is greater than 50MB",Toast.LENGTH_LONG).show();
                dismiss();
                Log.d("file","file size is greater than 50MB "+filesize);
            }else{
                Log.d("file","file size is less than 50MB "+filesize);
            }
            filename = returnCursor.getString(nameIndex);

            addFile.setText(filename);

        }

    }

    //Find File Extension
    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }


    private void uploadFile(){

        //Checks if file is selected or not
        if(fileUri != null){


            StorageReference fileReference = fileStorageRef.child(System.currentTimeMillis() + "_" +filename);

            //Uploads file to database
            uploadTask = fileReference.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Gets File uploaded Date
                            String uploaded_date = DateFormat.getDateInstance(DateFormat.MEDIUM)
                                    .format(taskSnapshot.getMetadata().getCreationTimeMillis());

                            //Get file Download Url
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Upload Successful",Toast.LENGTH_LONG).show();
                                    UploadModel upload = new UploadModel(filename, information.getText().toString(),
                                            uri.toString(), userId, username, uploaded_date, true);

                                    fileDocumentRef = FirebaseFirestore.getInstance().collection("groups")
                                            .document(groupId).collection("files").document();

                                    //Uploads file to database
                                    fileDocumentRef.set(upload);
                                    dismiss();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
                            dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
        }else{
            Toast.makeText(getActivity(),"No file selected",Toast.LENGTH_LONG).show();
            dismiss();
        }
    }
}
