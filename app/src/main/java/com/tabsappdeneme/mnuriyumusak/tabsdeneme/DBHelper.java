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
                        "(id integer primary key, ders_adi text ,ders_gunu text ,ders_baslangic text,ders_bitis text, tahta_photo_no integer,not_photo_no integer);"
        );

        db.execSQL(
                "create table drive_folders " +
                        "(id integer primary key, folder_name text,folder_id text);"
        );

        db.execSQL(
                "create table drive_files " +
                        "(id integer primary key, file_name text,course_name text,is_uploaded integer,is_ders_notu integer);"
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

    public void addNewPhoto(String photoName, String courseName, boolean isTahtaFotosu)
    {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put("file_name", photoName);
        contentValues.put("course_name", courseName);
        contentValues.put("is_uploaded", 0);
        if(isTahtaFotosu)
            contentValues.put("is_ders_notu", 0);
        else
            contentValues.put("is_ders_notu", 1);
        db.insert("drive_files", null, contentValues);
        db.close();
    }

    public void photoDriveaYuklendi(String photoName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update drive_files set is_uploaded=1 where file_name='"+photoName+"'");
        db.close();
    }

    public int getYuklenmemisResimSayisi()
    {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from drive_files where is_uploaded=0", null );
        result = res.getCount();
        db.close();
        return  result;
    }

    public ArrayList<String[]> getYuklenmemisResimler()
    {
        int boyut = getYuklenmemisResimSayisi();
        String[] fotoAdi = new String[boyut];
        String[] dersAdi = new String[boyut];
        String[] is_ders_notu = new String [boyut];

        ArrayList<String[]> all = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from drive_files where is_uploaded=0", null );
        int index = 0;
        if(res.getCount() != 0)
        {
            res.moveToFirst();
            do {
                fotoAdi[index] = res.getString(res.getColumnIndex("file_name"));
                dersAdi[index] = res.getString(res.getColumnIndex("course_name"));
                is_ders_notu[index] = ""+res.getInt(res.getColumnIndex("is_ders_notu"));
                index++;
            } while (res.moveToNext());
            all.add(fotoAdi);
            all.add(dersAdi);
            all.add(is_ders_notu);
        }
        else
            all = null;
        db.close();
        return  all;
    }

    public boolean increasePhotoNo(String dersAdi, boolean isTahtaFotosu)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(isTahtaFotosu)
            res =  db.rawQuery( "select tahta_photo_no from dersler where ders_adi='"+dersAdi+"'", null );
        else
            res =  db.rawQuery( "select not_photo_no from dersler where ders_adi='"+dersAdi+"'", null );

        ContentValues contentValues = new ContentValues();
        int cureentPhotoNo = -1;
        if(res.getCount() != 0)
        {
            res.moveToFirst();
            if(isTahtaFotosu)
                cureentPhotoNo = res.getInt(res.getColumnIndex("tahta_photo_no"));
            else
                cureentPhotoNo = res.getInt(res.getColumnIndex("not_photo_no"));
            cureentPhotoNo++;
            if(isTahtaFotosu)
                db.execSQL("update dersler set tahta_photo_no="+cureentPhotoNo+" where ders_adi='"+dersAdi+"'");
            else
                db.execSQL("update dersler set not_photo_no="+cureentPhotoNo+" where ders_adi='"+dersAdi+"'");
        }
        db.close();
        return true;
    }

    public int getPhotoNo(String dersAdi, boolean isTahtaFotosu) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(isTahtaFotosu)
            res =  db.rawQuery( "select tahta_photo_no from dersler where ders_adi='"+dersAdi+"'", null );
        else
            res =  db.rawQuery( "select not_photo_no from dersler where ders_adi='"+dersAdi+"'", null );
        int cureentPhotoNo = 0;
        if(res.getCount() != 0)
        {
            res.moveToFirst();
            if(isTahtaFotosu)
                cureentPhotoNo = res.getInt(res.getColumnIndex("tahta_photo_no"));
            else
                cureentPhotoNo = res.getInt(res.getColumnIndex("not_photo_no"));
        }
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
        contentValues.put("tahta_photo_no", 0);
        contentValues.put("not_photo_no", 0);
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
        contentValues.put("university_name", "Girilmemiş...");
        contentValues.put("nick_name", "Girilmemiş...");
        contentValues.put("bulut", 0);
        boolean isStorageExist = checkStorage();
        if(isStorageExist)
            contentValues.put("external_storage", 1);
        else
            contentValues.put("external_storage", 0);
        db.insert("infos", null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("ders_adi", "Tanimlanamayanlar");
        contentValues2.put("ders_gunu", "");
        contentValues2.put("ders_baslangic", "");
        contentValues2.put("ders_bitis", "");
        contentValues2.put("tahta_photo_no", 0);
        contentValues2.put("not_photo_no", 0);
        db.insert("dersler", null, contentValues2);
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
        String name = "Girilmemiş...";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select university_name from infos", null );
        if(res.getCount() != 0)
        {
            res.moveToFirst();
            name = res.getString(res.getColumnIndex("university_name"));
        }
        db.close();
        return name;
    }

    public String getNickName() {
        String name = "Girilmemiş...";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select nick_name from infos", null );
        if(res.getCount() != 0)
        {
            res.moveToFirst();
            name = res.getString(res.getColumnIndex("nick_name"));
        }
        db.close();
        return name;
    }

    public ArrayList<String[]> getTumDersler(String bugun)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(bugun.equals(""))
            res =  db.rawQuery( "select * from dersler ORDER BY ders_baslangic ASC", null);
        else
            res =  db.rawQuery( "select * from dersler where ders_gunu='"+bugun+"' ORDER BY ders_baslangic ASC", null);

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

        String dersAdi = "Tanimlanamayanlar";
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
        return externalStorageReadable;
    }


}