package com.tiagodanin.example.sqlnotes.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tiagodanin.example.sqlnotes.R;
import com.tiagodanin.example.sqlnotes.model.Note;
import com.tiagodanin.example.sqlnotes.utils.DatabaseHelper;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        setTitle(getString(R.string.titlePageNote));

        DatabaseHelper database = new DatabaseHelper(this);

        Intent intent = getIntent();
        Note note = database.getNote(intent.getIntExtra("id", 0));

        TextView tagText = findViewById(R.id.tagText);
        TextView noteText = findViewById(R.id.viewText);
        TextView createdText = findViewById(R.id.createdText);
        TextView updatedText = findViewById(R.id.updatedText);
        View colorView = findViewById(R.id.colorView);

        tagText.setText(note.getTag());
        colorView.setBackgroundColor(note.getColor());
        createdText.setText(String.format(getString(R.string.createdAtLabel), note.getTextDataCreated()));
        updatedText.setText(String.format(getString(R.string.updatedAtLabel), note.getTextDateUpdated()));
        noteText.setText(note.getText());
    }
}