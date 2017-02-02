package tabsapp.tabsapp.mnuriyumusak.tabsapp;



import android.net.Uri;

import android.os.Environment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Nuri on 16.01.2017.
 */

public class PictureNameCreator{
    private File externalPath;
    private String tahtaSubFolder = "Tahta Fotograflari";
    private String dersNotuSubFolder = "Ders Notlari";

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

    public String getPictureName(DBHelper mydb, boolean isIncrease, boolean isOldName, boolean isTahtaFotosu,String manuelDersAdi)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = sdf.format(new Date());
        String oldName = "";
        String dersAdi;

        if(manuelDersAdi.equals(""))
            dersAdi=  getDersAdi(mydb);
        else
            dersAdi = manuelDersAdi;

        int photoNo = mydb.getPhotoNo(dersAdi,isTahtaFotosu);
        if(isIncrease)
            mydb.increasePhotoNo(dersAdi,isTahtaFotosu);
        if(isOldName)
            photoNo--;
        oldName = "" + dersAdi + "-" + photoNo + "-" + timestamp + ".jpg";
        return oldName;
    }

    public File getPictureImageFile(DBHelper mydb,boolean isOldName,boolean isIncrease,boolean isTahtaFotosu,String manuelDersAdi)
    {
        //creating folders
        File picSavePath3;
        File picSavePath2;
        File picSavePath;
        String dersAdi;
        if(!mydb.hasSDKart())
        {
            picSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp");
            if(!picSavePath.exists())
            {
                picSavePath.mkdir();
            }

            if(manuelDersAdi.equals(""))
                dersAdi= getDersAdi(mydb);
            else
                dersAdi = manuelDersAdi;

            if(!dersAdi.equals("Tanimlanamayanlar"))
            {
                picSavePath2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp/"+dersAdi);
            }
            else
            {
                picSavePath2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp/Tanimlanamayanlar");
            }
            if(isTahtaFotosu)
                picSavePath3 = new File (picSavePath2.getPath() +"/"+tahtaSubFolder);
            else
                picSavePath3 = new File (picSavePath2.getPath() +"/"+dersNotuSubFolder);
            if(!picSavePath2.exists())
            {
                picSavePath2.mkdir();
            }
            if(!picSavePath3.exists())
            {
                picSavePath3.mkdir();
            }
        }
        else //sdcard a yazar
        {
            if (!externalPath.exists()) {
                externalPath.mkdir();
            }
            picSavePath = new File(externalPath + "/tabsApp");
            if (!picSavePath.exists()) {
                picSavePath.mkdir();
            }

            if(manuelDersAdi.equals(""))
                dersAdi= getDersAdi(mydb);
            else
                dersAdi = manuelDersAdi;

            if(!dersAdi.equals("Tanimlanamayanlar"))
            {
                picSavePath2 = new File(externalPath + "/tabsApp/"+dersAdi);
            }
            else
            {
                picSavePath2 = new File(externalPath + "/tabsApp/Tanimlanamayanlar");
            }
            if(isTahtaFotosu)
                picSavePath3 = new File (picSavePath2.getPath() +"/"+tahtaSubFolder);
            else
                picSavePath3 = new File (picSavePath2.getPath() +"/"+dersNotuSubFolder);

            if(!picSavePath2.exists())
            {
                picSavePath2.mkdir();
            }
            if(!picSavePath3.exists())
            {
                picSavePath3.mkdir();
            }
        }
        String picName = getPictureName(mydb,isIncrease,isOldName,isTahtaFotosu,manuelDersAdi);
        File imageFile = new File(picSavePath3, picName);
        return imageFile;
    }

    public Uri getPictureSavePath(DBHelper mydb,boolean isDelete,boolean isTahtaFotosu,String dersAdi)
    {
        File imageFile;
        if(!isDelete)
            imageFile = getPictureImageFile(mydb,false,true,isTahtaFotosu,dersAdi);
        else
            imageFile = getPictureImageFile(mydb,true,false,isTahtaFotosu,dersAdi);

        Uri pictureUri = Uri.fromFile(imageFile);
        return pictureUri;
    }

}
