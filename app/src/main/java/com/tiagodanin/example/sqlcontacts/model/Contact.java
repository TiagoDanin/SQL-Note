package com.tiagodanin.example.sqlcontacts.model;

public class Contact {
    public static final String TableName = "contacts";

    public static final String ColumnId = "id";
    public static final String ColumnName = "name";
    public static final String ColumnNumberPhoneNumber = "phone_number";
    public static final String ColumnAddress = "address";

    public static final String CreateTable =
            "CREATE TABLE " + TableName + "("
                    + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ColumnName + " TEXT,"
                    + ColumnNumberPhoneNumber + " TETX,"
                    + ColumnAddress + " TEXT"
                    + ")";

    private int id;
    private String name;
    private String phoneNumber;
    private String address;

    public Contact() {
        this.id = 0;
        this.name = "";
        this.address = "";
        this.phoneNumber = "";
    }

    public Contact(int id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
