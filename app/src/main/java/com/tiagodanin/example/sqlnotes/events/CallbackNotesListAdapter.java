package com.tiagodanin.example.sqlnotes.events;

import com.tiagodanin.example.sqlnotes.model.Note;

public interface CallbackNotesListAdapter {
    void onEditSelected(Note note);

    void onDeleteSelected(Note note);

    void onViewSelected(Note note);
}
