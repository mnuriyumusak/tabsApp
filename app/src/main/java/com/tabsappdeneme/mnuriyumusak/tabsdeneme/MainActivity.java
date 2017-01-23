package com.tabsappdeneme.mnuriyumusak.tabsdeneme;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity  {
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolBar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view_main);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        myToggle = new ActionBarDrawerToggle(this,myDrawer,R.string.open, R.string.close);
        myToolBar = (Toolbar)  findViewById(R.id.nav_action);

        setSupportActionBar(myToolBar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.nav_settings:
                        Intent intent = new Intent(MainActivity.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_account:
                        Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent2);
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
