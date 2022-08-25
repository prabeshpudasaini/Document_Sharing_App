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

public class ClassHostFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction ft;
    String resultName, resultCode, resultGroupId;
    ClassFragment fragment;
    MemberFragment memberFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_class_host, null);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resultName = bundle.getString("Class Name", "Class Name");
            resultCode = bundle.getString("Join Code", "Join Code");
            resultGroupId = bundle.getString("GroupId","GroupId");

        }

        fragment = new ClassFragment();
        memberFragment = new MemberFragment();
        Bundle b = new Bundle();
        b.putString("Class Name", resultName);
        b.putString("Join Code", resultCode);
        b.putString("GroupId",resultGroupId);

        fragment.setArguments(b);
        memberFragment.setArguments(b);


        bottomNavigationView = v.findViewById(R.id.bottom_nav_view);


        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_class_content,fragment);
        ft.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.bottom_nav_class){
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,fragment);
                    ft.commit();
                    return true;

                }
                if(id==R.id.bottom_nav_dashboard){
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