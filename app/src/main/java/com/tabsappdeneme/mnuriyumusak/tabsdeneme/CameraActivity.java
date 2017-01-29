package com.tabsappdeneme.mnuriyumusak.tabsdeneme;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;

public class CameraActivity extends AppCompatActivity {

    DBHelper mydb;
    Button ekleye_git;
    Button btnTakePhoto; //foto çekme butonu
    ImageView imgTakenPhoto; //fonun çekilince nerde gösterileceği
    private static final int CAM_REQUEST = 1313;
    PictureNameCreator pnc;


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
        setContentView(R.layout.activity_main);
        mydb = new DBHelper(this);

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_camera_activity);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_camera_activity);
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
                        intent = new Intent(CameraActivity.this, MainActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(CameraActivity.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(CameraActivity.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(CameraActivity.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


        File externalPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        pnc = new PictureNameCreator(externalPath);

        if(!hasPermissions())
        {
            requestPerms();
        }

        ekleye_git = (Button) findViewById(R.id.git);
        btnTakePhoto = (Button) findViewById(R.id.cek_button);
        imgTakenPhoto = (ImageView) findViewById(R.id.imageview1);
        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());


        ekleye_git.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, DersGirme.class);
                startActivity(intent);
            }
        });
    }

    //başarılı bir çekim işleminin sonucu
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


    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,100);
        }
    }

    //camera açma isteğinin gittiği yer
    class btnTakePhotoClicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Uri pictureUri = pnc.getPictureSavePath(mydb,false);
            Toast.makeText(getApplicationContext(),"Kaydedildi"+pictureUri.getPath().toString(),Toast.LENGTH_LONG).show();
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(cameraintent, CAM_REQUEST);
        }




    }


}
