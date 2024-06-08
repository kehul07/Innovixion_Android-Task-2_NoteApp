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

public class EditNoteActivity extends AppCompatActivity {
    EditText title , content;
    TextView timeline,save;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        title = findViewById(R.id.note_title);
        content = findViewById(R.id.note_content);
        timeline = findViewById(R.id.note_timeline);
        save = findViewById(R.id.save);
        backbtn = findViewById(R.id.backbtn);

        int id = getIntent().getIntExtra("ID",-1);

        DatabaseHelper db = new DatabaseHelper(this);

        NoteModel nm = db.getNoteById(id);

        title.setText(nm.getTitle());
        content.setText(nm.getContent());

//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM h:mm a", Locale.ENGLISH);
        String formattedDate = sdf.format(new Date());
        timeline.setText(formattedDate);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EditNoteActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = title.getText().toString();
                String Content = content.getText().toString();
                String Timeline = timeline.getText().toString();
                NoteModel nm = new NoteModel(id,Title,Content,Timeline);
                boolean isUpdated = db.updateNote(nm);
                if (isUpdated) {
                    Toast.makeText(EditNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditNoteActivity.this,MainActivity.class);
                    startActivityForResult(i,100);
                    finish();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Note update failed", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}