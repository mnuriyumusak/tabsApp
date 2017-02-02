package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nuri on 1.02.2017.
 */

public class GalleryActivity extends AppCompatActivity  {

    DBHelper mydb;
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    private File imageFile;
    private String tahtaSubFolder = "Tahta Fotograflari";
    private String dersNotuSubFolder = "Ders Notlari";

    private String secilenDersAdi;
    private boolean isDersNotu;

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
        setContentView(R.layout.gallery_layout);
        Bundle bundle = getIntent().getExtras();

        secilenDersAdi = bundle.getString("secilenDersAdi");
        isDersNotu = bundle.getBoolean("isDersNotu");

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_gallery);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_gallery);
        myToggle = new ActionBarDrawerToggle(this,myDrawer,R.string.open, R.string.close);
        myToolBar = (Toolbar)  findViewById(R.id.nav_action);

        setSupportActionBar(myToolBar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.nav_main_activity:
                        intent = new Intent(GalleryActivity.this, MainActivity.class);
                        intent.putExtra("isFromAnother", true);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(GalleryActivity.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(GalleryActivity.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(GalleryActivity.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(GalleryActivity.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(GalleryActivity.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        mydb = new DBHelper(this);

        ArrayList<String[]> tumResimler = mydb.getResimler(secilenDersAdi,isDersNotu);
        ArrayList<ImageModel> data = new ArrayList<>();
        String[] IMGS = null;

        File ext = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");
        String subFolder;
        //gallery işleri burdan sonra başlıyor
        if(tumResimler != null)
        {
            IMGS = new String[tumResimler.get(0).length];
            for(int i = 0; i < tumResimler.get(0).length ; i++)
            {
                if(tumResimler.get(2)[i].equals("0"))
                    subFolder = tahtaSubFolder;
                else
                    subFolder = dersNotuSubFolder;

                imageFile = new File(ext+"/tabsApp/"+tumResimler.get(1)[i]+"/"+subFolder, tumResimler.get(0)[i]);
                IMGS[i] = imageFile.getPath().toString();
            }
        }
        if(IMGS != null)
        {
            for (int i = 0; i < IMGS.length; i++)
            {
                ImageModel imageModel = new ImageModel();
                imageModel.setName("Image " + i);
                imageModel.setUrl(IMGS[i]);
                data.add(imageModel);
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.gallery_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(GalleryActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);

    }
}
