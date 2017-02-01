package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Nuri on 1.02.2017.
 */

public class GalleryActivity extends AppCompatActivity  {

    private GridView myGrid;
    private ImageAdapter myAdapter;
    DBHelper mydb;

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
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(GalleryActivity.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        mydb = new DBHelper(this);
        myGrid = (GridView) findViewById(R.id.gallery_grid);
        myAdapter = new ImageAdapter(this,mydb,getExternalFilesDir(Environment.DIRECTORY_DCIM));
        myGrid.setAdapter(myAdapter);

        myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i = new Intent(getApplicationContext(),FullImageActivity.class);
                i.putExtra("id",position);
                Uri filePath = (Uri)myAdapter.getItem(position);
                i.putExtra("path",filePath.getPath().toString());
                startActivity(i);
            }
        });

    }
}
