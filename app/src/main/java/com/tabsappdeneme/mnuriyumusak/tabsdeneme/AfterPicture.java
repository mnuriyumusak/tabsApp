package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nuri on 16.01.2017.
 */

public class AfterPicture extends AppCompatActivity {
    ImageView imgTakenPhoto; //fonun çekilince nerde gösterileceği
    Button yenidenCek;
    Button kaydet;
    PictureNameCreator pnc;
    DBHelper mydb;
    private static final int CAM_REQUEST = 1313;

    //drawer things
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolBar;
    private NavigationView navigationView;

    //yukardaki soldaki menu butonuna basınca menünün açılması için
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_take_picture);
        mydb = new DBHelper(this);

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_after_picture);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_after_picture);
        myToggle = new ActionBarDrawerToggle(this,myDrawer,R.string.open, R.string.close);
        myToolBar = (Toolbar)  findViewById(R.id.nav_action);

        setSupportActionBar(myToolBar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView general_university_name = (TextView)  navigationView.getHeaderView(0).findViewById(R.id.university_name);
        TextView general_nick_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.isim_nick);
        general_university_name.setText(mydb.getUniversityName());
        general_nick_name.setText(mydb.getNickName());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.nav_main_activity:
                        intent = new Intent(AfterPicture.this, MainActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(AfterPicture.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(AfterPicture.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(AfterPicture.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(AfterPicture.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        File externalPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        pnc = new PictureNameCreator(externalPath);

        imgTakenPhoto = (ImageView) findViewById(R.id.after_take_imageview);
        yenidenCek = (Button) findViewById(R.id.yenidencek_button);
        kaydet = (Button) findViewById(R.id.ok_button);

        File imageFile = pnc.getPictureImageFile(mydb,true,false);
        Bitmap thumbnail = BitmapFactory.decodeFile(imageFile.getPath());
        imgTakenPhoto.setImageBitmap(thumbnail);

        yenidenCek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File imageFile = pnc.getPictureImageFile(mydb,true,false);
                imageFile.delete();

                Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Uri pictureUri = pnc.getPictureSavePath(mydb,true);
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(cameraintent, CAM_REQUEST);

            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //başarılı bir çekim işleminin sonucu, tekrar kendi classına dönmesini sağlar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST)
        {
            Intent intent = new Intent(this, AfterPicture.class);
            startActivity(intent);
        }
    }


}
