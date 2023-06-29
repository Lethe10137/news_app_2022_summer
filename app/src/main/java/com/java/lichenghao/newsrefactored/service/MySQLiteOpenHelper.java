package com.java.lichenghao.newsrefactored.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
//    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }


    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, "myNews.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("createtable","mynews");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS myNews" + " (_id INTEGER PRIMARY KEY UNIQUE, info TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER TABLE myNews ADD COLUMN other STRING");
    }
}
