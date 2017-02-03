package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nuri on 2.02.2017.
 */

public class GalleryFolderActivity extends AppCompatActivity {
    private DBHelper mydb ;

    private GridView gridView;
    private FolderAdapter folderAdapter;
    private  ArrayList<String> folders;
    private ArrayList<String> subFolders;
    private  String secilenDers;
    private  boolean isOnSubFolder = false;

    private String tahtaSubFolder;
    private String dersNotuSubFolder;

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
        setContentView(R.layout.gallery_folder_view);
        mydb = new DBHelper(this);

        tahtaSubFolder = getResources().getString(R.string.tahta_fotolari);
        dersNotuSubFolder = getResources().getString(R.string.ders_notlari);

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_gallery_folder);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_gallery_folder);
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
                        intent = new Intent(GalleryFolderActivity.this, MainActivity.class);
                        intent.putExtra("isFromAnother", true);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(GalleryFolderActivity.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(GalleryFolderActivity.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(GalleryFolderActivity.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(GalleryFolderActivity.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(GalleryFolderActivity.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        gridView = (GridView)findViewById(R.id.folder_grid);
        folders = mydb.getDersFolders();
        subFolders = new ArrayList<>();
        subFolders.add(tahtaSubFolder);
        subFolders.add(dersNotuSubFolder);
        dersFolderlariniGoster();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if(!isOnSubFolder)
                {
                    secilenDers = folders.get(position);
                    seciliDersinSubFolderlariniGoster();
                }
                else
                {
                    if(subFolders.get(position).equals(dersNotuSubFolder))
                        galeriyeGit(secilenDers,true);
                    else
                        galeriyeGit(secilenDers,false);
                }
            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                if(!isOnSubFolder)
                {
                    final CharSequence[] items = {getResources().getString(R.string.delete)};
                    final String silinecekDersAdi = folders.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(GalleryFolderActivity.this);
                    builder.setTitle(getResources().getString(R.string.seciniz));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if(item == 0)
                            {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                mydb.dersSil(silinecekDersAdi);
                                                //File externalPath = getExternalFilesDir(Environment.DIRECTORY_DCIM);
                                                File externalPath = null;
                                                if(mydb.hasSDKart())
                                                    externalPath = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");
                                                PictureNameCreator pnc = new PictureNameCreator(externalPath,getApplicationContext());
                                                pnc.dersiSil(silinecekDersAdi,mydb);
                                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.ders_silindi),Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(GalleryFolderActivity.this, GalleryFolderActivity.class);
                                                startActivity(intent);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(GalleryFolderActivity.this);
                                builder.setMessage(getResources().getString(R.string.dersi_sil_soru)).setPositiveButton(getResources().getString(R.string.evet), dialogClickListener)
                                        .setNegativeButton(getResources().getString(R.string.hayir), dialogClickListener).show();
                            }
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });
    }


    public void dersFolderlariniGoster()
    {
        folderAdapter = new FolderAdapter(this, folders);
        gridView.setAdapter(folderAdapter);
    }

    public void seciliDersinSubFolderlariniGoster()
    {
        isOnSubFolder = true;
        folderAdapter = new FolderAdapter(this, subFolders);
        gridView.setAdapter(folderAdapter);
    }

    public void galeriyeGit(String secilenDersAdi,boolean isDersNotu)
    {
        Intent intent = new Intent(GalleryFolderActivity.this, GalleryActivity.class);
        intent.putExtra("secilenDersAdi", secilenDersAdi);
        intent.putExtra("isDersNotu", isDersNotu);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        if(isOnSubFolder)
        {
            Intent intent = new Intent(GalleryFolderActivity.this, GalleryFolderActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(GalleryFolderActivity.this, MainActivity.class);
            intent.putExtra("isFromAnother", true);
            startActivity(intent);
        }
    }
}

