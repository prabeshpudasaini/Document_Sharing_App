package com.example.collegedocumentsharing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.TextView;

import com.example.collegedocumentsharing.R;

import java.lang.ref.WeakReference;

public class SearchableActivity extends AppCompatActivity {


    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        txt = findViewById(R.id.textview);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            txt.setText("Searching by: Teachers"+ query);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

        }
    }
    public void updateText(String text){
        txt.setText(text);
    }


}