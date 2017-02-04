package tabsapp.tabsapp.mnuriyumusak.tabsapp;



import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.io.File;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolBar;
    private NavigationView navigationView;
    DBHelper mydb;
    private static final int CAM_REQUEST = 1313;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private GoogleApiClient mGoogleApiClient;
    private boolean camOn = false;

    private TextView cekilen_resim_sayisi;
    private TextView suanki_ders;
    private boolean isFromAnother = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            isFromAnother = bundle.getBoolean("isFromAnother");

        if(mydb.isVeryFirstGiris())
        {
            mydb.insertFirstRowAfterDil();
            Intent intent = new Intent(MainActivity.this, DersEkleme.class);
            startActivity(intent);
        }
        else
        {
            if(!isFromAnother)
            {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        }

        setContentView(R.layout.main_menu_layout);
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
                    case R.id.nav_gallery:
                        intent = new Intent(MainActivity.this, GalleryFolderActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(MainActivity.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        });

        cekilen_resim_sayisi = (TextView) findViewById(R.id.cekilen_resim_sayisi);
        cekilen_resim_sayisi.setText(""+mydb.getToplamResimSayisi());
        File externalPath = null;
        if(mydb.hasSDKart())
            externalPath = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");
        PictureNameCreator pnc = new PictureNameCreator(externalPath,getApplicationContext());
        suanki_ders = (TextView) findViewById(R.id.suanki_ders);
        String currentDersAdi = pnc.getDersAdi(mydb);
        if(currentDersAdi.equals(getResources().getString(R.string.tanimlanamayanlar)))
            suanki_ders.setText(getResources().getString(R.string.yok));
        else
            suanki_ders.setText(currentDersAdi);


    }

    //başarılı bir çekim işleminin sonucu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_CANCELED)
        {
            // TODO Auto-generated method stub
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == CAM_REQUEST)
            {
                Intent intent = new Intent(this, AfterPicture.class);
                startActivity(intent);
            }
        }

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // We are not connected anymore!
    }
}
