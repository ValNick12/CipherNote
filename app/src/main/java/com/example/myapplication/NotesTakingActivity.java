package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Notes;
import com.example.myapplication.Model.Profiles;

public class NotesTakingActivity extends AppCompatActivity {

    EditText editText_title, editText_notes;
    ImageView imageView_save;
    Notes note;
    boolean isOldNote = false;
    Profiles profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_taking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        profile = (Profiles) getIntent().getSerializableExtra("user");

        note = new Notes();
        try {
            note = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(note.getTitle());
            editText_notes.setText(note.getNote());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        imageView_save.setOnClickListener(v -> {
            String title = editText_title.getText().toString();
            String note_text = editText_notes.getText().toString();
            if (note_text.isEmpty()){
                Toast.makeText(NotesTakingActivity.this, "Please write a note!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isOldNote){
                note = new Notes();
            }

            note.setTitle(title);
            note.setNote(note_text);
            note.setUser(profile.getUsername());

            Intent intent = new Intent();
            intent.putExtra("note", note);
            setResult(NotesTakingActivity.RESULT_OK, intent);
            finish();
        });
    }
}