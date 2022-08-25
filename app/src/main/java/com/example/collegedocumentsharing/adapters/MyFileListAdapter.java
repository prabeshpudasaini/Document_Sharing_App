//package com.example.collegedocumentsharing;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentActivity;
//
//public class MyFileListAdapter extends ArrayAdapter {
//
//    Context c;
//    String filenames;
//
//    public MyFileListAdapter(FragmentActivity activity, String filename) {
//        super(activity,0,filename);  //Pass object
//        c=activity;
//        filenames = filename;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        convertView = LayoutInflater.from(c).inflate(R.layout.single_item,null);
//        TextView tv = convertView.findViewById(R.id.tv);
//        tv.setText(array[position]);
//        return convertView;
//
//    }
//}
