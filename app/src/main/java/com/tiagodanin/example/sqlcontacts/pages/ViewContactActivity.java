package com.tiagodanin.example.sqlcontacts.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tiagodanin.example.sqlcontacts.R;
import com.tiagodanin.example.sqlcontacts.model.Contact;
import com.tiagodanin.example.sqlcontacts.utils.DatabaseHelper;

public class ViewContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        setTitle(getString(R.string.titlePageContact));

        DatabaseHelper database = new DatabaseHelper(this);

        Intent intent = getIntent();
        Contact contact = database.getContact(intent.getIntExtra("id", 0));

        TextView nameText = findViewById(R.id.nameText);
        TextView phoneNumberText = findViewById(R.id.phoneNumberText);
        TextView addressText = findViewById(R.id.addressText);

        nameText.setText(String.format(getString(R.string.contactNameView), contact.getName()));
        phoneNumberText.setText(String.format(getString(R.string.contactPhoneNumberView), contact.getPhoneNumber()));
        addressText.setText(String.format(getString(R.string.contactAddressView), contact.getAddress()));
    }
}