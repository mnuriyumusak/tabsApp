package com.tabsappdeneme.mnuriyumusak.tabsdeneme;



import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Nuri on 16.01.2017.
 */

public class PictureNameCreator{
    private File externalPath;

    public PictureNameCreator(File externalPath)
    {
        this.externalPath = externalPath;
    }

    public String getDersAdi(DBHelper mydb)
    {
        Calendar c;
        int minute,hour,dayOfWeek;
        String day;
        String formattedDate;
        c = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date now = new Date();
        formattedDate = dateFormat.format(now);

        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == 2)
            day = "Pazartesi";
        else if(dayOfWeek == 3)
            day = "Salı";
        else if(dayOfWeek == 4)
            day = "Çarşamba";
        else if(dayOfWeek == 5)
            day = "Perşembe";
        else if(dayOfWeek == 6)
            day = "Cuma";
        else
            day = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = sdf.format(new Date());

        String dersAdi = mydb.getCurrentDersAdi(day,formattedDate, formattedDate);
        return dersAdi;
    }

    public String getPictureName(DBHelper mydb, boolean isIncrease, boolean isOldName)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = sdf.format(new Date());
        String oldName = "";
        String dersAdi =  getDersAdi(mydb);
        int photoNo = mydb.getPhotoNo(dersAdi);
        if(isIncrease)
            mydb.increasePhotoNo(dersAdi);
        if(isOldName)
            photoNo--;
        oldName = "" + dersAdi + "-" + photoNo + "-" + timestamp + ".jpg";
        return oldName;
    }

    public File getPictureImageFile(DBHelper mydb,boolean isOldName,boolean isIncrease)
    {
        //creating folders
        File picSavePath2;
        File picSavePath;
        if(!mydb.getExternalStorageStatus())
        {
            picSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp");
            if(!picSavePath.exists())
            {
                picSavePath.mkdir();
            }
            String dersAdi = getDersAdi(mydb);
            if(!dersAdi.equals(""))
            {
                picSavePath2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp/"+dersAdi);
            }
            else
            {
                picSavePath2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp/Tanimlanamayanlar");
            }
            if(!picSavePath2.exists())
            {
                picSavePath2.mkdir();
            }
        }
        else //sdcard a yazar
        {
            picSavePath = new File(externalPath + "/tabsApp");
            if (!picSavePath.exists()) {
                picSavePath.mkdir();
            }
            String dersAdi = getDersAdi(mydb);
            if(!dersAdi.equals(""))
            {
                picSavePath2 = new File(externalPath + "/tabsApp/"+dersAdi);
            }
            else
            {
                picSavePath2 = new File(externalPath + "/tabsApp/Tanimlanamayanlar");
            }
            if(!picSavePath2.exists())
            {
                picSavePath2.mkdir();
            }
        }
        String picName = getPictureName(mydb,isIncrease,isOldName);
        File imageFile = new File(picSavePath2, picName);
        return imageFile;
    }

    public Uri getPictureSavePath(DBHelper mydb,boolean isDelete)
    {
        File imageFile;
        if(!isDelete)
            imageFile = getPictureImageFile(mydb,false,true);
        else
            imageFile = getPictureImageFile(mydb,true,false);

        Uri pictureUri = Uri.fromFile(imageFile);
        return pictureUri;
    }

}
