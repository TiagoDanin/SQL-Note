package com.tiagodanin.example.sqlnotes.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.skydoves.colorpickerview.ColorPickerView;
import com.tiagodanin.example.sqlnotes.R;
import com.tiagodanin.example.sqlnotes.model.Note;
import com.tiagodanin.example.sqlnotes.utils.DatabaseHelper;

import java.util.Date;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;

public class EditNoteActivity extends AppCompatActivity {
    private Note note;
    private boolean isEdit = false;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        setTitle(getString(R.string.titlePageNote));

        database = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            isEdit = true;
            note = database.getNote(intent.getIntExtra("id", 0));
        } else {
            note = new Note();
        }

        if (isEdit) {
            EditText tagText = findViewById(R.id.tagEditText);
            EditText editText = findViewById(R.id.textEditText);
            ColorPickerView colorPicker = findViewById(R.id.colorPicker);

            tagText.setText(note.getTag());
            editText.setText(note.getText());
            colorPicker.setInitialColor(note.getColor());
        }

        Markwon markdown = Markwon.create(this);
        MarkwonEditor editor = MarkwonEditor.create(markdown);
        EditText editText = findViewById(R.id.textEditText);
        editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));

        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener((view) -> applyModification());
    }

    private void applyModification() {
        EditText tagText = findViewById(R.id.tagEditText);
        EditText editText = findViewById(R.id.textEditText);
        ColorPickerView colorPicker = findViewById(R.id.colorPicker);
        View main = findViewById(R.id.mainView);

        if (TextUtils.isEmpty(tagText.getText()) || TextUtils.isEmpty(editText.getText())) {
            Snackbar.make(main, "Has empty value!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        note.setTag(tagText.getText().toString());
        note.setText(editText.getText().toString());
        note.setDateUpdated(new Date());
        note.setColor(colorPicker.getColor());

        if (isEdit) {
            database.updateNote(note);
        } else {
            database.insertNote(note);
        }

        onBackPressed();
    }
}