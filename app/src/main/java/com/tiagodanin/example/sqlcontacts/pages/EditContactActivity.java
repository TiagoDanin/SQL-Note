package com.tiagodanin.example.sqlcontacts.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tiagodanin.example.sqlcontacts.R;
import com.tiagodanin.example.sqlcontacts.model.Contact;
import com.tiagodanin.example.sqlcontacts.utils.DatabaseHelper;

public class EditContactActivity extends AppCompatActivity {
    private Contact contact;
    private boolean isEdit = false;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        setTitle(getString(R.string.titlePageContact));

        database = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            isEdit = true;
            contact = database.getContact(intent.getIntExtra("id", 0));
        } else {
            contact = new Contact();
        }

        if (isEdit) {
            EditText nameText = findViewById(R.id.nameEditText);
            EditText phoneNumberText = findViewById(R.id.phoneNumberEditText);
            EditText addressText = findViewById(R.id.addressEditText);

            nameText.setText(contact.getName());
            phoneNumberText.setText(contact.getPhoneNumber());
            addressText.setText(contact.getAddress());
        }

        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener((view) -> applyModification());
    }

    private void applyModification() {
        EditText nameText = findViewById(R.id.nameEditText);
        EditText phoneNumberText = findViewById(R.id.phoneNumberEditText);
        EditText addressText = findViewById(R.id.addressEditText);

        if (TextUtils.isEmpty(nameText.getText()) || TextUtils.isEmpty(phoneNumberText.getText())) {
             return;
        }

        contact.setName(nameText.getText().toString());
        contact.setPhoneNumber(phoneNumberText.getText().toString());

        if (!TextUtils.isEmpty(addressText.getText().toString())) {
            contact.setAddress(addressText.getText().toString());
        }

        if (isEdit) {
            database.updateContact(contact);
        } else {
            database.insertContact(contact);
        }

        onBackPressed();
    }
}