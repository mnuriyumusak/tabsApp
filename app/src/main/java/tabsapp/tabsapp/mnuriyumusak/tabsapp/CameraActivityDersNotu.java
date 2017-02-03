package tabsapp.tabsapp.mnuriyumusak.tabsapp;


import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

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

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nuri on 31.01.2017.
 */

public class CameraActivityDersNotu extends AppCompatActivity {

    DBHelper mydb;
    Button ders_notu_cek;
    private static final int CAM_REQUEST = 1313;
    PictureNameCreator pnc;
    Spinner ders_secimi;
    String selectedDers = "";

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
        setContentView(R.layout.ders_notu_cek_layout);
        mydb = new DBHelper(this);

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_ders_notu_cek);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_ders_notu_cek);
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
                        intent = new Intent(CameraActivityDersNotu.this, MainActivity.class);
                        intent.putExtra("isFromAnother", true);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(CameraActivityDersNotu.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(CameraActivityDersNotu.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(CameraActivityDersNotu.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(CameraActivityDersNotu.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_gallery:
                        intent = new Intent(CameraActivityDersNotu.this, GalleryFolderActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(CameraActivityDersNotu.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


        File externalPath = null;
        if(mydb.hasSDKart())
            externalPath = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");
        pnc = new PictureNameCreator(externalPath,getApplicationContext());

        ders_notu_cek = (Button) findViewById(R.id.not_cek_foto_Cek);
        ders_notu_cek.setOnClickListener(new CameraActivityDersNotu.btnTakePhotoClicker());
        ders_secimi = (Spinner) findViewById(R.id.ders_secim_spinner);
        ArrayList<String[]> all = mydb.getTumDersler("");
        final CustomSpinnerAdapter customAdapter;
        if(all.size() != 0)
        {
            customAdapter =new CustomSpinnerAdapter(getApplicationContext(),all.get(0));
        }
        else
        {
            String[] tmp = {getResources().getString(R.string.ders_girilmemis)};
            customAdapter =new CustomSpinnerAdapter(getApplicationContext(),tmp);
        }
        ders_secimi.setAdapter(customAdapter);
        ders_secimi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String selected = (String) parent.getSelectedItem().toString();
                selectedDers = selected;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            intent.putExtra("isTahtaFotosu", false);
            intent.putExtra("manuelDersAdi", selectedDers);
            startActivity(intent);
        }
    }

    //camera açma isteğinin gittiği yer
    class btnTakePhotoClicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(selectedDers == null)
            {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.secim_gerekli),Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(selectedDers.equals(""))
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.secim_gerekli),Toast.LENGTH_SHORT).show();
                else
                {
                    Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri pictureUri = pnc.getPictureSavePath(mydb,false,false,selectedDers);
                    cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                    startActivityForResult(cameraintent, CAM_REQUEST);
                }
            }
        }
    }


}
