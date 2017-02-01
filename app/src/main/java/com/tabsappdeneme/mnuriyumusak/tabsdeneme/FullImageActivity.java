package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullImageActivity extends AppCompatActivity {
    //ImageAdapter myAdapter;
    PhotoViewAttacher mAttacher;
    DBHelper mydb;
    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        mydb = new DBHelper(this);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        String path = i.getExtras().getString("path");

        //myAdapter = new ImageAdapter(this,mydb,getExternalFilesDir(Environment.DIRECTORY_DCIM));
        im = (ImageView) findViewById(R.id.full_size_image);
        File myPath = new File(path);
        im.setImageURI(Uri.fromFile(myPath));
        mAttacher = new PhotoViewAttacher(im);
        mAttacher.update();
    }
}
