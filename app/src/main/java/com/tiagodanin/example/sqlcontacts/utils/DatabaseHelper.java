package com.tiagodanin.example.sqlcontacts.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tiagodanin.example.sqlcontacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DatabaseVersion = 1;
    private static final String DatabaseName = "contacts_db";

    public DatabaseHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contact.CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TableName);
        onCreate(db);
    }

    public Contact insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.ColumnName, contact.getName());
        values.put(Contact.ColumnNumberPhoneNumber, contact.getPhoneNumber());
        values.put(Contact.ColumnAddress, contact.getAddress());

        long id = db.insert(Contact.TableName, null, values);
        db.close();

        contact.setId(Math.toIntExact(id));
        return contact;
    }

    public Contact updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.ColumnName, contact.getName());
        values.put(Contact.ColumnNumberPhoneNumber, contact.getPhoneNumber());
        values.put(Contact.ColumnAddress, contact.getAddress());

        db.update(Contact.TableName, values, Contact.ColumnId + " = ?", new String[]{
                String.valueOf(contact.getId())
        });
        db.close();

        return contact;
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contact.TableName, Contact.ColumnId + " = ?", new String[]{
                String.valueOf(contact.getId())
        });
        db.close();
    }

    @Nullable
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Contact.TableName,
                new String[]{
                        Contact.ColumnId, Contact.ColumnName, Contact.ColumnNumberPhoneNumber, Contact.ColumnAddress
                },
                Contact.ColumnId + " = ?",
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

        Contact contact = new Contact(
                cursor.getInt(cursor.getColumnIndex(Contact.ColumnId)),
                cursor.getString(cursor.getColumnIndex(Contact.ColumnName)),
                cursor.getString(cursor.getColumnIndex(Contact.ColumnNumberPhoneNumber)),
                cursor.getString(cursor.getColumnIndex(Contact.ColumnAddress))
        );

        cursor.close();
        db.close();

        return contact;
    }

    public int getContactCount() {
        String countQuery = "SELECT " + Contact.ColumnId + " FROM " + Contact.TableName;
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

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Contact.TableName + " ORDER BY " + Contact.ColumnName + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return contacts;
        }

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getInt(cursor.getColumnIndex(Contact.ColumnId)),
                        cursor.getString(cursor.getColumnIndex(Contact.ColumnName)),
                        cursor.getString(cursor.getColumnIndex(Contact.ColumnNumberPhoneNumber)),
                        cursor.getString(cursor.getColumnIndex(Contact.ColumnAddress))
                );

                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return  contacts;
    }
}
