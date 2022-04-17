package com.example.collegedocumentsharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

public class ClassFragment extends Fragment {

    TextView textclass;
    String className;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class,null);

        textclass = v.findViewById(R.id.text_class);

        Bundle b = this.getArguments();
        if (b != null) {
            className = b.getString("Class Name", "Class Name");
        }
//
////        String className = getActivity().getIntent().getStringExtra("Class Name");
        textclass.setText(className);


        return v;
    }
}
