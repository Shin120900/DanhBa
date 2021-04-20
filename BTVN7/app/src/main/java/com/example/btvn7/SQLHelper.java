package com.example.btvn7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelper";
    static final String DB_NAME = "Phonebooks.db";
    static final String DB_TABLE = "Contact";
    static final int DB_VERSION = 1;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;

    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "CREATE TABLE Contact(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name Text," +
                "phoneNumber Text)";
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
            onCreate(db);
        }
    }

    public void InsertContact(Contact contact) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("phoneNumber", contact.getPhoneNumber());
        sqLiteDatabase.insert(DB_TABLE, null, contentValues);
    }

    public int deleteContact(String id) {
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(DB_TABLE, "id=?", new String[]{id});
    }
    public void updateContact(String id,Contact contact){
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("phoneNumber", contact.getPhoneNumber());
        sqLiteDatabase.update(DB_TABLE,contentValues,"id=?",new String[]{id});
    }
    public List<Contact> getAllContact(){
        List<Contact> list = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(false,DB_TABLE,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name =cursor.getString(cursor.getColumnIndex("name"));
            String phone =cursor.getString(cursor.getColumnIndex("phoneNumber"));
            list.add(new Contact(id,name,phone));
        }
        return list;
    }
}
