package com.java.lichenghao.newsrefactored.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.java.lichenghao.newsrefactored.MyApplication;
import com.java.lichenghao.newsrefactored.data.News;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DBManager {

    private static MySQLiteOpenHelper helper;
    private static SQLiteDatabase db;
    private static Map<Long, News> news_already = new HashMap<>();
    public DBManager(){
        helper = new MySQLiteOpenHelper(MyApplication.getContext());

        db = helper.getWritableDatabase();
        helper.onCreate(db);
    }

    public static void add(Map<Long, News> news){
        Long k = Long.valueOf(news.size());
        Long k0 = Long.valueOf(news_already.size());
        Log.d("DBManager", "Trying to add" + k + "reords");
        int counter  = 0;
        db.beginTransaction();

        try {
            for (Long i = k0; i < k; i++) {
                News item = news.get(i);
                news_already.put(i,item);
                counter++;
                db.execSQL("insert OR IGNORE into myNews VALUES(?,?)", new Object[]{(int)((long)i), item.toString()});
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            Log.d("DBManager", "Actually added" + counter + "reords");
        }
    }

    public static List<News> query(){

        Gson gson = new Gson();
        ArrayList<News> ans = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM myNews", null);
        while(c.moveToNext()){
            News item =  gson.fromJson(c.getString(1), News.class) ;
            ans.add(item);
        }
        Log.d("DBManager", "Read" + ans.toArray().length + "records from data base");
        return ans;
    }

    public static void closeDB(){
        db.close();
    }





}
