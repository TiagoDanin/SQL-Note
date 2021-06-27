package com.tiagodanin.example.sqlnotes.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tiagodanin.example.sqlnotes.R;
import com.tiagodanin.example.sqlnotes.events.CallbackNotesListAdapter;
import com.tiagodanin.example.sqlnotes.model.Note;

import java.util.ArrayList;

import io.noties.markwon.Markwon;

public class NotesListAdapter extends RecyclerView.Adapter {
    private final Markwon markdown;
    @SuppressWarnings("FieldCanBeLocal")
    private Context context;
    private ArrayList<Note> notesList;
    private CallbackNotesListAdapter callback;

    public NotesListAdapter(Context context, ArrayList<Note> notesList, CallbackNotesListAdapter callback) {
        this.context = context;
        this.notesList = notesList;
        this.callback = callback;

        markdown = Markwon.create(context);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Note note = notesList.get(position);
        ((NoteViewHolder) holder).bind(note);
    }

    @SuppressWarnings("CanBeFinal")
    private class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tagTextView;
        TextView noteTextView;
        MaterialButton optionsButton;
        View cardLayout;
        View colorView;

        NoteViewHolder(View itemView) {
            super(itemView);

            tagTextView = itemView.findViewById(R.id.tagText);
            noteTextView = itemView.findViewById(R.id.noteText);
            optionsButton = itemView.findViewById(R.id.optionsButton);
            cardLayout = itemView.findViewById(R.id.cardLayout);
            colorView = itemView.findViewById(R.id.colorView);
        }

        void bind(Note note) {
            markdown.setMarkdown(noteTextView, note.getText());

            tagTextView.setText(note.getTag());
            optionsButton.setOnClickListener(view -> showMenu(view, R.menu.options_note_menu, note));
            cardLayout.setOnClickListener(view -> callback.onViewSelected(note));
            colorView.setBackgroundColor(note.getColor());
        }

        @SuppressWarnings("SameParameterValue")
        @SuppressLint("RestrictedApi")
        void showMenu(View view, int menuId, Note note) {
            MenuBuilder menuBuilder = new MenuBuilder(context);
            MenuInflater inflater = new MenuInflater(context);
            inflater.inflate(menuId, menuBuilder);

            MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, view);
            optionsMenu.setForceShowIcon(true);
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.viewAction:
                            callback.onViewSelected(note);
                            break;
                        case R.id.editAction:
                            callback.onEditSelected(note);
                            break;
                        case R.id.deleteAction:
                            callback.onDeleteSelected(note);
                            break;
                    }

                    return true;
                }

                @Override
                public void onMenuModeChange(@NonNull MenuBuilder menu) {
                }
            });

            optionsMenu.show();
        }
    }
}