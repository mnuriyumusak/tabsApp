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
import android.os.Environment;
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
                        "(id integer primary key, university_name text ,nick_name text,bulut integer,external_storage integer);"
        );

        db.execSQL(
                "create table dersler " +
                        "(id integer primary key, ders_adi text ,ders_gunu text ,ders_baslangic text,ders_bitis text, ders_photo_no integer);"
        );

        db.execSQL(
                "create table drive_folders " +
                        "(id integer primary key, folder_name text,folder_id text);"
        );

        db.execSQL(
                "create table drive_files " +
                        "(id integer primary key, file_name text,course_name text,is_uploaded integer);"
        );

        insertFirstRow(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS infos");
        db.execSQL("DROP TABLE IF EXISTS dersler");
        onCreate(db);
    }

    public boolean increasePhotoNo(String dersAdi)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res =  db.rawQuery( "select ders_photo_no from dersler where ders_adi='"+dersAdi+"'", null );
        ContentValues contentValues = new ContentValues();
        int cureentPhotoNo = -1;
        res.moveToFirst();
        cureentPhotoNo = res.getInt(res.getColumnIndex("ders_photo_no"));
        cureentPhotoNo++;
        db.execSQL("update dersler set ders_photo_no="+cureentPhotoNo+" where ders_adi='"+dersAdi+"'");
        db.close();
        return true;
    }

    public int getPhotoNo(String dersAdi) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select ders_photo_no from dersler where ders_adi='"+dersAdi+"'", null );
        int cureentPhotoNo = -1;
        res.moveToFirst();
        cureentPhotoNo = res.getInt(res.getColumnIndex("ders_photo_no"));
        db.close();
        return cureentPhotoNo;
    }

    public void dersEkle(String ad, String gun, String baslangic, String bitis)
    {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put("ders_adi", ad);
        contentValues.put("ders_gunu", gun);
        contentValues.put("ders_baslangic", baslangic);
        contentValues.put("ders_bitis", bitis);
        contentValues.put("ders_photo_no", 0);
        db.insert("dersler", null, contentValues);
        db.close();
    }

    public void folderEkle(String folder_name, String folder_id)
    {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put("folder_name", folder_name);
        contentValues.put("folder_id", folder_id);
        db.insert("drive_folders", null, contentValues);
        db.close();
    }

    public String getFolderId(String folder_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select folder_id from drive_folders where folder_name='"+folder_name+"'", null );
        String id = "";
        if(res.getCount() != 0)
        {
            res.moveToFirst();
            id = res.getString(res.getColumnIndex("folder_id"));
        }
        db.close();
        return id;
    }

    public void insertFirstRow (SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("university_name", "Bilkent");
        boolean isStorageExist = checkStorage();
        if(isStorageExist)
            contentValues.put("external_storage", 1);
        else
            contentValues.put("external_storage", 0);
        db.insert("infos", null, contentValues);
    }

    public void addKayıtInfos(String uni, String nick, int bulut)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update infos set university_name='"+uni+"',nick_name='"+nick+"',bulut="+bulut+" where id=1");
        db.close();
    }

    public boolean getExternalStorageStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select external_storage from infos", null );
        res.moveToFirst();
        int result = res.getInt(res.getColumnIndex("external_storage"));
        db.close();
        if(result == 1)
            return true;
        else
            return false;
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

    public ArrayList<String[]> getTumDersler()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from dersler", null);
        ArrayList<String[]> all = new ArrayList<>();
        int howMany = 0;
        howMany = res.getCount();

        if(howMany != 0)
        {
            String[] dersAdi = new String[howMany];
            String[] gun = new String[howMany];
            String[] baslangic = new String[howMany];
            String[] bitis = new String[howMany];
            int index = 0;
            if (res.moveToFirst()) {
                do {
                    dersAdi[index] = res.getString(res.getColumnIndex("ders_adi"));
                    gun[index] = res.getString(res.getColumnIndex("ders_gunu"));
                    baslangic[index] = res.getString(res.getColumnIndex("ders_baslangic"));
                    bitis[index] = res.getString(res.getColumnIndex("ders_bitis"));
                    index++;
                } while (res.moveToNext());
            }
            all.add(dersAdi);
            all.add(gun);
            all.add(baslangic);
            all.add(bitis);
            db.close();
            return all;
        }
        else
        {
            db.close();
            return all;
        }
    }

    public String getCurrentDersAdi(String curDay,String bas, String son)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from dersler where ders_gunu='"+curDay+"' and ders_baslangic<='"+bas+"' and ders_bitis>='"+son+"'", null);

        String dersAdi = "";
        if(res.getCount() != 0)
        {
            if (res.moveToFirst())
            {
                dersAdi = res.getString(res.getColumnIndex("ders_adi"));
            }
        }
        db.close();
        return dersAdi;
    }

    private boolean checkStorage() {
        boolean externalStorageReadable, externalStorageWritable;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            externalStorageReadable = externalStorageWritable = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            externalStorageReadable = true;
            externalStorageWritable = false;
        } else {
            externalStorageReadable = externalStorageWritable = false;
        }
        return externalStorageWritable;
    }


}