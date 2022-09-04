package com.example.collegedocumentsharing.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.collegedocumentsharing.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FileHostFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction ft;
    String resultName, resultCode, resultGroupId;
    FileFragment fragment;
    MemberFragment memberFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_file_host, null);

        //Gets Data from bundle From Home Fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resultName = bundle.getString("Class Name", "Class Name");
            resultCode = bundle.getString("Join Code", "Join Code");
            resultGroupId = bundle.getString("GroupId","GroupId");
        }

        fragment = new FileFragment();
        memberFragment = new MemberFragment();

        //Sends Data to FileFragment
        Bundle b = new Bundle();
        b.putString("Class Name", resultName);
        b.putString("Join Code", resultCode);
        b.putString("GroupId",resultGroupId);

        fragment.setArguments(b);
        memberFragment.setArguments(b);

        bottomNavigationView = v.findViewById(R.id.bottom_nav_view);

        //Sets Default Fragment
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_class_content,fragment);
        ft.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.bottom_nav_files){
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,fragment);
                    ft.commit();
                    return true;
                }
                if(id==R.id.bottom_nav_members){
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,memberFragment);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });

        return v;

    }
}