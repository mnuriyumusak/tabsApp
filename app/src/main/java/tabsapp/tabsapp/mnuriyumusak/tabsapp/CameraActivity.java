package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.graphics.Color;

import android.net.Uri;

import android.os.Bundle;

import android.content.Intent;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;


public class CameraActivity extends AppCompatActivity {

    DBHelper mydb;
    Button btnTakePhoto; //foto çekme butonu
    ImageButton ders_notu_cek;
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
                        intent.putExtra("isFromAnother", true);
                        startActivity(intent);
                        item.setChecked(true);
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
                    case R.id.nav_gallery:
                        intent = new Intent(CameraActivity.this, GalleryFolderActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(CameraActivity.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


        //File externalPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File externalPath = null;
        if(mydb.hasSDKart())
            externalPath = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");


        pnc = new PictureNameCreator(externalPath,getApplicationContext());


        btnTakePhoto = (Button) findViewById(R.id.cek_button);
        imgTakenPhoto = (ImageView) findViewById(R.id.imageview1);
        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
        ders_notu_cek = (ImageButton) findViewById(R.id.not_cek_image);

        final GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        ders_notu_cek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    Intent intent = new Intent(CameraActivity.this, CameraActivityDersNotu.class);
                    startActivity(intent);
                    return true;
                } else {
                    // your code for move and drag
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ders_notu_cek.setColorFilter(Color.argb(255, 208, 234, 204));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        ders_notu_cek.clearColorFilter(); // White Tint
                        return true; // if you want to handle the touch event
                }
                return false;
            }

        });


    }

    //başarılı bir çekim işleminin sonucu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST && resultCode == RESULT_OK)
        {
            Intent intent = new Intent(this, AfterPicture.class);
            intent.putExtra("isTahtaFotosu", true);
            intent.putExtra("manuelDersAdi", "");
            startActivity(intent);
        }
    }

    //camera açma isteğinin gittiği yer
    class btnTakePhotoClicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Uri pictureUri = pnc.getPictureSavePath(mydb,false,true,"");
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(cameraintent, CAM_REQUEST);
        }
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


}
