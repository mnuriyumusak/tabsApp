package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nuri on 1.02.2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context contex;
    public ArrayList<Uri> images;

    private DBHelper mydb;
    BitmapFactory.Options bmOptions;
    File image;
    Uri imageUri;
    File sd;
    String subFolder;
    ArrayList<String[]> all;
    private String tahtaSubFolder = "Tahta Fotograflari";
    private String dersNotuSubFolder = "Ders Notlari";

    public ImageAdapter(Context c,DBHelper db, File mysd)
    {
        contex = c;
        mydb = db;
        bmOptions = new BitmapFactory.Options();
        sd = mysd;
        ArrayList<String[]> all = mydb.getResimler(true);
        images = new ArrayList<Uri>();

        if(all.size() != 0)
        {
            for(int i = 0; i < all.get(0).length ; i++)
            {
                if(all.get(2)[i].equals("0"))
                    subFolder = tahtaSubFolder;
                else
                    subFolder = dersNotuSubFolder;
                image = new File(sd+"/tabsApp/"+all.get(1)[i]+"/"+subFolder, all.get(0)[i]);
                imageUri = Uri.fromFile(image);
                images.add(imageUri);
            }
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position );
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ImageView imageview;
        imageview = new ImageView(contex);
        imageview.setImageURI(images.get(pos));
        imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageview.setLayoutParams(new GridView.LayoutParams(480,480));
        return imageview;
    }
}
