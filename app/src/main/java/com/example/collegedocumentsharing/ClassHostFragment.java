package com.example.collegedocumentsharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ClassHostFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction ft;
    String result;
    ClassFragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_class_host, null);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            result = bundle.getString("Class Name", "Class Name");
        }

        fragment = new ClassFragment();
        Bundle b = new Bundle();
        b.putString("Class Name", result);
        fragment.setArguments(b);


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

                }
                if(id==R.id.bottom_nav_dashboard){
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,new DashboardFragment());
                    ft.commit();

                }
                if(id==R.id.bottom_nav_notifications){


                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_class_content,new NotificationFragment());
                    ft.commit();
                }
                return false;
            }
        });

        return v;


    }
}