package com.tiagodanin.example.sqlcontacts;

import android.content.Intent;
import android.os.Bundle;
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
import com.tiagodanin.example.sqlcontacts.events.CallbackContactsListAdapter;
import com.tiagodanin.example.sqlcontacts.model.Contact;
import com.tiagodanin.example.sqlcontacts.pages.EditContactActivity;
import com.tiagodanin.example.sqlcontacts.pages.ViewContactActivity;
import com.tiagodanin.example.sqlcontacts.utils.DatabaseHelper;
import com.tiagodanin.example.sqlcontacts.view.ContactsListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CallbackContactsListAdapter {
    private BackgroundThreadPoster backgroundThread;
    private UiThreadPoster uiThread;

    private ArrayList<Contact> contactsList;
    private ContactsListAdapter contactsListAdapter;
    private RecyclerView recyclerview;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.contactsTitle));

        uiThread = new UiThreadPoster();
        backgroundThread = new BackgroundThreadPoster();

        FloatingActionButton fab = findViewById(R.id.addContact);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditContactActivity.class);
            startActivity(intent);
        });

        contactsList = new ArrayList<>();
        database = new DatabaseHelper(this);

        recyclerview = findViewById(R.id.recyclerContacts);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        contactsListAdapter = new ContactsListAdapter(this, contactsList, this);
        recyclerview.setAdapter(contactsListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundThread.post(this::getContactsList);
    }

    @WorkerThread
    private void getContactsList() {
        contactsList.clear();
        contactsList.addAll(database.getAllContacts());
        uiThread.post(this::showContactsList);
    }

    @UiThread
    private void showContactsList() {
        View notFoundContacts = findViewById(R.id.notFoundContactsText);
        notFoundContacts.setVisibility(contactsList.size() <= 0 ? View.VISIBLE : View.GONE);
        contactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewSelected(Contact contact) {
        Intent intent = new Intent(this, ViewContactActivity.class);
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    @Override
    public void onEditSelected(Contact contact) {
        Intent intent = new Intent(this, EditContactActivity.class);
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.titleAlertDeletePeople);
        builder.setMessage(R.string.descriptionAlertDeletePeople);

        builder.setPositiveButton(R.string.yesLabel, (dialog, which) -> {
            database.deleteContact(contact);
            dialog.dismiss();
            backgroundThread.post(this::getContactsList);
        });

        builder.setNegativeButton(R.string.noLabel, (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }
}