package com.example.collegedocumentsharing.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


import com.example.collegedocumentsharing.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    ListPreference theme;
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings,rootKey);
        theme = findPreference("themes_switch");

        theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

//                if(newValue.equals("Light")){
//                    Toast.makeText(getActivity(),"The selected value is "+newValue,Toast.LENGTH_LONG).show();
//                }
//                if(newValue.equals("Dark")){
//                    Toast.makeText(getActivity(),"The selected value is "+newValue,Toast.LENGTH_LONG).show();
//                }
//                if(newValue.equals("System Default")){
//                    Toast.makeText(getActivity(),"The selected value is "+newValue,Toast.LENGTH_LONG).show();
//                }
                if(newValue.equals("Light")){
                    Toast.makeText(getActivity(),"The selected value is "+newValue,Toast.LENGTH_LONG).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                if(newValue.equals("Dark")){
                    Toast.makeText(getActivity(),"The selected value is "+newValue,Toast.LENGTH_LONG).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                if(newValue.equals("System Default")){
                    Toast.makeText(getActivity(),"The selected value is "+newValue,Toast.LENGTH_LONG).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
//                Toast.makeText(getActivity(),"The selected value is "+preference,Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

}
