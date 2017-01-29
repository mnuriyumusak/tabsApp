package com.tabsappdeneme.mnuriyumusak.tabsdeneme;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity  {
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolBar;
    private NavigationView navigationView;
    DBHelper mydb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        mydb = new DBHelper(this);

        Toast.makeText(getApplicationContext(),"durum:"+mydb.getExternalStorageStatus(),Toast.LENGTH_SHORT).show();

        navigationView = (NavigationView) findViewById(R.id.navigation_view_main);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
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
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(MainActivity.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(MainActivity.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(MainActivity.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        });

    }


    //yukardaki soldaki menu butonuna basınca menünün açılması için
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
