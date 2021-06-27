package com.tiagodanin.example.sqlnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;
import com.tiagodanin.example.sqlnotes.events.CallbackNotesListAdapter;
import com.tiagodanin.example.sqlnotes.model.Note;
import com.tiagodanin.example.sqlnotes.pages.EditNoteActivity;
import com.tiagodanin.example.sqlnotes.pages.ViewNoteActivity;
import com.tiagodanin.example.sqlnotes.utils.DatabaseHelper;
import com.tiagodanin.example.sqlnotes.view.NotesListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CallbackNotesListAdapter {
    private BackgroundThreadPoster backgroundThread;
    private UiThreadPoster uiThread;

    private ArrayList<Note> notesList;
    private NotesListAdapter notesListAdapter;
    private RecyclerView recyclerview;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.notesTitle));

        uiThread = new UiThreadPoster();
        backgroundThread = new BackgroundThreadPoster();

        FloatingActionButton fab = findViewById(R.id.addNote);
        fab.setOnClickListener(view -> {
            Log.i("Event", "Select Create option");
            Intent intent = new Intent(this, EditNoteActivity.class);
            startActivity(intent);
        });

        notesList = new ArrayList<>();
        database = new DatabaseHelper(this);

        recyclerview = findViewById(R.id.recyclerNotes);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        notesListAdapter = new NotesListAdapter(this, notesList, this);
        recyclerview.setAdapter(notesListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundThread.post(this::getNotesList);
    }

    @WorkerThread
    private void getNotesList() {
        notesList.clear();
        notesList.addAll(database.getAllNotes());
        uiThread.post(this::showNotesList);
    }

    @UiThread
    private void showNotesList() {
        View notFoundNotes = findViewById(R.id.notFoundNotesText);
        notFoundNotes.setVisibility(notesList.size() <= 0 ? View.VISIBLE : View.GONE);
        notesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewSelected(Note note) {
        Log.i("Event", "Select View option");
        Intent intent = new Intent(this, ViewNoteActivity.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }

    @Override
    public void onEditSelected(Note note) {
        Log.i("Event", "Select Edit option");
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected(Note note) {
        Log.i("Event", "Select Delete option");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.titleAlertDeleteNote);
        builder.setMessage(R.string.descriptionAlertDeleteNote);

        builder.setPositiveButton(R.string.yesLabel, (dialog, which) -> {
            database.deleteNote(note);
            dialog.dismiss();
            backgroundThread.post(this::getNotesList);
        });

        builder.setNegativeButton(R.string.noLabel, (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }
}