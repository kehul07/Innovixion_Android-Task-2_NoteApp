package com.kehuldroid.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EnterNotesActivity extends AppCompatActivity {
    EditText title , content;
    TextView timeline,save;
    ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_notes);

        title = findViewById(R.id.note_title);
        content = findViewById(R.id.note_content);
        timeline = findViewById(R.id.note_timeline);
        save = findViewById(R.id.save);
        backbtn = findViewById(R.id.backbtn);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM  h:mm a", Locale.ENGLISH);
        String formattedDate = sdf.format(new Date());
        timeline.setText(formattedDate);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                String Title = title.getText().toString();
                String Content = content.getText().toString();
                String Timeline = timeline.getText().toString();
                NoteModel nm = new NoteModel(Title,Content,Timeline);
                db.addNote(nm);
                Toast.makeText(EnterNotesActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EnterNotesActivity.this,MainActivity.class);
                startActivityForResult(i,100);
                finish();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EnterNotesActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}