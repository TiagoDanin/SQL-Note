package com.tiagodanin.example.sqlcontacts.view;

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
import com.tiagodanin.example.sqlcontacts.R;
import com.tiagodanin.example.sqlcontacts.events.CallbackContactsListAdapter;
import com.tiagodanin.example.sqlcontacts.model.Contact;

import java.util.ArrayList;

public class ContactsListAdapter extends RecyclerView.Adapter {
    @SuppressWarnings("FieldCanBeLocal")
    private Context context;
    private ArrayList<Contact> contactsList;
    private CallbackContactsListAdapter callback;

    public ContactsListAdapter(Context context, ArrayList<Contact> contactsList, CallbackContactsListAdapter callback) {
        this.context = context;
        this.contactsList = contactsList;
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        ((ContactViewHolder) holder).bind(contact);
    }

    @SuppressWarnings("CanBeFinal")
    private class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneNumberTextView;
        MaterialButton optionsButton;

        ContactViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameText);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberText);
            optionsButton = itemView.findViewById(R.id.optionsButton);
        }

        void bind(Contact contact) {
            nameTextView.setText(contact.getName());
            phoneNumberTextView.setText(String.format("+%s", contact.getPhoneNumber()));
            optionsButton.setOnClickListener(view -> showMenu(view, R.menu.options_contact_menu, contact));
        }

        @SuppressWarnings("SameParameterValue")
        @SuppressLint("RestrictedApi")
        void showMenu(View view, int menuId, Contact contact) {
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
                            callback.onViewSelected(contact);
                            break;
                        case R.id.editAction:
                            callback.onEditSelected(contact);
                            break;
                        case R.id.deleteAction:
                            callback.onDeleteSelected(contact);
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