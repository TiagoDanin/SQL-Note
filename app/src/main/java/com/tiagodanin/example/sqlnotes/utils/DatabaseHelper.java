package com.tiagodanin.example.sqlnotes.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tiagodanin.example.sqlnotes.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DatabaseVersion = 1;
    private static final String DatabaseName = "notes_db";
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormatSql = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    public DatabaseHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Note.TableName);
        onCreate(db);
    }

    public Note insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.ColumnTag, note.getTag());
        values.put(Note.ColumnColor, note.getColor());
        values.put(Note.ColumnText, note.getText());

        long id = db.insert(Note.TableName, null, values);
        db.close();

        note.setId(Math.toIntExact(id));
        return note;
    }

    public Note updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.ColumnTag, note.getTag());
        values.put(Note.ColumnColor, note.getColor());
        values.put(Note.ColumnText, note.getText());
        values.put(Note.ColumnDateUpdated, dateFormatSql.format(note.getDateUpdated()));

        db.update(Note.TableName, values, Note.ColumnId + " = ?", new String[]{
                String.valueOf(note.getId())
        });
        db.close();

        return note;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TableName, Note.ColumnId + " = ?", new String[]{
                String.valueOf(note.getId())
        });
        db.close();
    }

    @Nullable
    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Note.TableName,
                new String[]{
                        Note.ColumnId, Note.ColumnTag, Note.ColumnColor, Note.ColumnText, Note.ColumnDateCreated, Note.ColumnDateUpdated
                },
                Note.ColumnId + " = ?",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null,
                "1"
        );

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();

        Note note = null;
        try {
            note = new Note(
                    cursor.getInt(cursor.getColumnIndex(Note.ColumnId)),
                    cursor.getString(cursor.getColumnIndex(Note.ColumnTag)),
                    cursor.getInt(cursor.getColumnIndex(Note.ColumnColor)),
                    cursor.getString(cursor.getColumnIndex(Note.ColumnText)),
                    dateFormatSql.parse(cursor.getString(cursor.getColumnIndex(Note.ColumnDateCreated))),
                    dateFormatSql.parse(cursor.getString(cursor.getColumnIndex(Note.ColumnDateUpdated)))
            );
        } catch (ParseException exception) {
            exception.printStackTrace();
            Log.e("Database", "Falid load!");
        }

        cursor.close();
        db.close();

        return note;
    }

    public int getNoteCount() {
        String countQuery = "SELECT " + Note.ColumnId + " FROM " + Note.TableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor == null) {
            return 0;
        }

        int total = cursor.getCount();

        cursor.close();
        db.close();

        return total;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Note.TableName + " ORDER BY " + Note.ColumnDateUpdated + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return notes;
        }

        if (cursor.moveToFirst()) {
            do {
                try {
                    Note note = new Note(
                            cursor.getInt(cursor.getColumnIndex(Note.ColumnId)),
                            cursor.getString(cursor.getColumnIndex(Note.ColumnTag)),
                            cursor.getInt(cursor.getColumnIndex(Note.ColumnColor)),
                            cursor.getString(cursor.getColumnIndex(Note.ColumnText)),
                            dateFormatSql.parse(cursor.getString(cursor.getColumnIndex(Note.ColumnDateCreated))),
                            dateFormatSql.parse(cursor.getString(cursor.getColumnIndex(Note.ColumnDateUpdated)))
                    );
                    notes.add(note);
                } catch (ParseException exception) {
                    exception.printStackTrace();
                    Log.e("Database", "Falid load!");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return notes;
    }
}
