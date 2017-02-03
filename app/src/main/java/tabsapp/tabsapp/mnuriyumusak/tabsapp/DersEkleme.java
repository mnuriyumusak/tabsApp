package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nuri on 17.01.2017.
 */

public class DersEkleme extends AppCompatActivity {
    Button kaydet_button;
    Spinner university;
    String university_text;
    EditText nick;
    Switch sdKart;
    private DBHelper mydb ;

    TextView general_university_name;
    TextView general_nick_name;
    TextView sdkart_yazisi;

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


        if(mydb.isIlkGiris())
        {
            if(!hasPermissions())
            {
                requestPerms();
            }
        }

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
                if(!mydb.isIlkGiris())
                {
                    switch(item.getItemId())
                    {
                        case R.id.nav_main_activity:
                            intent = new Intent(DersEkleme.this, MainActivity.class);
                            intent.putExtra("isFromAnother", true);
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
                        case R.id.nav_drive_api:
                            intent = new Intent(DersEkleme.this, DriveApi.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_gallery:
                            intent = new Intent(DersEkleme.this, GalleryFolderActivity.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_ders_hakkinda:
                            intent = new Intent(DersEkleme.this, CreditsActivity.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                    }
                }
                else
                    showWarning();

                return false;
            }
        });


        kaydet_button = (Button) findViewById(R.id.kaydet_button);
        university = (Spinner) findViewById(R.id.university_field);
        nick = (EditText) findViewById(R.id.nick_field);
        sdKart = (Switch) findViewById(R.id.sdkart_switch);
        sdkart_yazisi = (TextView) findViewById(R.id.sdkart_yazisi);

        if(!mydb.isIlkGiris())
        {
            sdkart_yazisi.setVisibility(View.INVISIBLE);
            sdKart.setVisibility(View.INVISIBLE);
        }

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
                if(!hasPermissions())
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.izinsiz_devam_yok),Toast.LENGTH_SHORT).show();
                    requestPerms();
                }
                else
                {
                    if(mydb.isIlkGiris())
                    {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        if(!nick.getText().toString().equals(""))
                                        {
                                            String uniName = university_text;
                                            String nickName = nick.getText().toString();
                                            int isSdKart = (sdKart.isChecked())?1:0;
                                            if(!mydb.getExternalStorageStatus() && isSdKart==1)
                                            {
                                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.sd_kart_yok),Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                mydb.addKayıtInfos(uniName,nickName,isSdKart);
                                                general_university_name.setText(university_text);
                                                general_nick_name.setText(nickName);
                                                Intent intent = new Intent(DersEkleme.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                        else
                                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.lutfen),Toast.LENGTH_SHORT).show();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(DersEkleme.this);
                        builder.setMessage(getResources().getString(R.string.sd_kart_Secenegi)).setPositiveButton(getResources().getString(R.string.evet), dialogClickListener)
                                .setNegativeButton(getResources().getString(R.string.hayir), dialogClickListener).show();
                    }
                    else
                    {
                        String uniName = university_text;
                        String nickName = nick.getText().toString();
                        int isSdKart = (sdKart.isChecked())?1:0;
                        if(!mydb.getExternalStorageStatus())
                        {
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.sd_kart_yok),Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mydb.addKayıtInfos(uniName,nickName,isSdKart);
                            general_university_name.setText(university_text);
                            general_nick_name.setText(nickName);
                            Intent intent = new Intent(DersEkleme.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.GET_ACCOUNTS,android.Manifest.permission.ACCESS_NETWORK_STATE,android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.INTERNET};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.GET_ACCOUNTS,android.Manifest.permission.ACCESS_NETWORK_STATE,android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.INTERNET};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,100);
        }
    }

    public void showWarning()
    {
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.oncelikle_bilgi),Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }




}
