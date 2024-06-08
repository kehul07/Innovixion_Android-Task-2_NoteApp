package com.kehuldroid.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ImageView addButton , searchButton;
    NoteAdapter noteAdapter;
    List<NoteModel> noteList;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        searchView = findViewById(R.id.searchView);
        recyclerView=findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addBtn);
        searchButton = findViewById(R.id.searchBtn);


        searchButton.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();

                List<NoteModel> filteredList = new ArrayList<>();
                for (NoteModel model : noteList) {
                    String text = model.getTitle().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(model);
                    }
                }
                noteAdapter = new NoteAdapter(filteredList);
                recyclerView.setAdapter(noteAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();

                List<NoteModel> filteredList = new ArrayList<>();
                for (NoteModel model : noteList) {
                    String text = model.getTitle().toLowerCase();
                    if (text.contains(newText)) {
                        filteredList.add(model);
                    }
                }
                noteAdapter = new NoteAdapter(filteredList);
                recyclerView.setAdapter(noteAdapter);
                return true;
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.getVisibility() == View.GONE) {
                    searchView.setVisibility(View.VISIBLE);
                    searchButton.setVisibility(View.GONE);
                } else {
                    searchView.setVisibility(View.GONE);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,EnterNotesActivity.class);
                startActivity(i);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
         noteList = new ArrayList<>();
         db = new DatabaseHelper(getApplicationContext());
         noteList = db.getAllNotes();
         Collections.reverse(noteList);
         noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            noteList.clear();
            noteList.addAll(db.getAllNotes());
            Collections.reverse(noteList);
            noteAdapter.notifyDataSetChanged();
        }
    }
}