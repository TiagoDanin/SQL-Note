package com.tiagodanin.example.sqlcontacts.events;

import com.tiagodanin.example.sqlcontacts.model.Contact;

public interface CallbackContactsListAdapter {
    void onEditSelected(Contact contact);
    void onDeleteSelected(Contact contact);
    void onViewSelected(Contact contact);
}
