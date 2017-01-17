package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

/**
 * Created by Nuri on 17.01.2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tabsApp.db";
    public static final String INFOS_TABLE_NAME = "infos";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table infos " +
                        "(id integer primary key, photo_no integer ,university_name text ,nick_name text,bulut integer);"
        );
        insertFirstRow(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS infos");
        onCreate(db);

    }

    public boolean increasePhotoNo()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res =  db.rawQuery( "select photo_no from infos", null );
        ContentValues contentValues = new ContentValues();
        int cureentPhotoNo = -1;
        res.moveToFirst();
        cureentPhotoNo = res.getInt(res.getColumnIndex("photo_no"));
        cureentPhotoNo++;
        db.execSQL("update infos set photo_no="+cureentPhotoNo+" where id=1");
        db.close();
        return true;
    }

    public int getPhotoNo() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select photo_no from infos", null );
        int cureentPhotoNo = -1;
        res.moveToFirst();
        cureentPhotoNo = res.getInt(res.getColumnIndex("photo_no"));
        db.close();
        return cureentPhotoNo;
    }

    public void insertFirstRow (SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("photo_no", 0);
        contentValues.put("university_name", "Bilkent");
        db.insert("infos", null, contentValues);

    }

    public void addKayıtInfos(String uni, String nick, int bulut)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update infos set university_name='"+uni+"',nick_name='"+nick+"',bulut="+bulut+" where id=1");
        db.close();
    }

    public String getUniversityName() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select university_name from infos", null );
        res.moveToFirst();
        String name = res.getString(res.getColumnIndex("university_name"));
        db.close();
        return name;
    }

    public String getNickName() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select nick_name from infos", null );
        res.moveToFirst();
        String name = res.getString(res.getColumnIndex("nick_name"));
        db.close();
        return name;
    }



}