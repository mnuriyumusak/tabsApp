package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.app.Activity;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nuri on 17.01.2017.
 */

public class DersEkleme extends AppCompatActivity {
    Button kaydet_button;
    Spinner university;
    String university_text;
    EditText nick;
    Switch bulut;
    private DBHelper mydb ;

    TextView general_university_name;
    TextView general_nick_name;

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
        setContentView(R.layout.ders_ekleme);
        mydb = new DBHelper(this);


        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_ders_ekleme);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_ders_ekleme);
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
                        intent = new Intent(DersEkleme.this, MainActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(DersEkleme.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(DersEkleme.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(DersEkleme.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


        kaydet_button = (Button) findViewById(R.id.kaydet_button);
        university = (Spinner) findViewById(R.id.university_field);
        nick = (EditText) findViewById(R.id.nick_field);
        bulut = (Switch) findViewById(R.id.bulut_switch);
        general_university_name = (TextView)  navigationView.getHeaderView(0).findViewById(R.id.university_name);
        general_nick_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.isim_nick);
        general_university_name.setText(mydb.getUniversityName());
        general_nick_name.setText(mydb.getNickName());

        university.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                university_text = selected;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        kaydet_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uniName = university_text;
                String nickName = nick.getText().toString();
                int isBulut = (bulut.isChecked())?1:0;
                mydb.addKayıtInfos(uniName,nickName,isBulut);
                general_university_name.setText(university_text);
                general_nick_name.setText(nickName);
            }
        });


    }
}
