package com.tiagodanin.example.sqlnotes.model;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Note {
    public static final String TableName = "notes";

    public static final String ColumnId = "id";
    public static final String ColumnTag = "tag";
    public static final String ColumnColor = "color";
    public static final String ColumnText = "note";
    public static final String ColumnDateCreated = "data_created";
    public static final String ColumnDateUpdated = "data_updated";

    public static final String CreateTable =
            "CREATE TABLE " + TableName + "("
                    + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ColumnTag + " TEXT,"
                    + ColumnColor + " INTEGER,"
                    + ColumnText + " TEXT,"
                    + ColumnDateCreated + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + ColumnDateUpdated + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM/dd HH:mm");

    private int id;
    private String tag;
    private int color;
    private String text;
    private Date dataCreated;
    private Date dateUpdated;

    public Note() {
        this.id = -1;
        this.tag = "";
        this.color = Color.BLUE;
        this.text = "";
        this.dataCreated = new Date();
        this.dateUpdated = new Date();

        dateFormat.setTimeZone(TimeZone.getDefault());
    }

    public Note(int id, String tag, int color, String text, Date dataCreated, Date dateUpdated) {
        this.id = id;
        this.tag = tag;
        this.color = color;
        this.text = text;
        this.dataCreated = dataCreated;
        this.dateUpdated = dateUpdated;

        dateFormat.setTimeZone(TimeZone.getDefault());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Date dataCreated) {
        this.dataCreated = dataCreated;
    }

    public String getTextDataCreated() {
        return dateFormat.format(dataCreated);
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getTextDateUpdated() {
        return dateFormat.format(dateUpdated);
    }
}
