package tabsapp.tabsapp.mnuriyumusak.tabsapp;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nuri on 18.01.2017.
 */

public class DersGirme  extends AppCompatActivity {
    DBHelper mydb;
    EditText baslangic;
    EditText bitis;
    EditText ders_harf;
    EditText ders_rakam;
    Spinner gunPicker;
    Button kaydet_ekle;
    String gun;
    TextView v1,v2,v3,v4,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15,v16,v17,v18,v19,v20,v21,v22,v23,v24,v25,v26,v27,v28,v29,v30,v31,v32,v33,v34,v35,v36,v37,v38,v39,v40;
    TextView[] allTextViews;
    TextView ders_girilmemis;
    boolean isBaslangis = true;
    int hour_x;
    int minute_x;
    int currentColor = -1;


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
        setContentView(R.layout.ders_girme);
        mydb = new DBHelper(this);

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_ders_girme);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_ders_girme);
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
                        intent = new Intent(DersGirme.this, MainActivity.class);
                        intent.putExtra("isFromAnother", true);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(DersGirme.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(DersGirme.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(DersGirme.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_gallery:
                        intent = new Intent(DersGirme.this, GalleryFolderActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(DersGirme.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


        baslangic = (EditText) findViewById(R.id.baslangic_field);
        bitis = (EditText) findViewById(R.id.bitis_field);
        ders_harf = (EditText) findViewById(R.id.ders_harf);
        ders_rakam = (EditText) findViewById(R.id.ders_rakam);
        gunPicker = (Spinner) findViewById(R.id.gun);
        kaydet_ekle = (Button) findViewById(R.id.kaydet_ekle);
        ders_girilmemis = (TextView)  findViewById(R.id.ders_girilmemis);
        allTextViews = new TextView[40];
        v1 = (TextView)  findViewById(R.id.v1);
        allTextViews[0] = v1;
        v2 = (TextView)  findViewById(R.id.v2);
        allTextViews[1] = v2;
        v3 = (TextView)  findViewById(R.id.v3);
        allTextViews[2] = v3;
        v4 = (TextView)  findViewById(R.id.v4);
        allTextViews[3] = v4;
        v5 = (TextView)  findViewById(R.id.v5);
        allTextViews[4] = v5;
        v6 = (TextView)  findViewById(R.id.v6);
        allTextViews[5] = v6;
        v7 = (TextView)  findViewById(R.id.v7);
        allTextViews[6] = v7;
        v8 = (TextView)  findViewById(R.id.v8);
        allTextViews[7] = v8;
        v9 = (TextView)  findViewById(R.id.v9);
        allTextViews[8] = v9;
        v10 = (TextView)  findViewById(R.id.v10);
        allTextViews[9] = v10;
        v11 = (TextView)  findViewById(R.id.v11);
        allTextViews[10] = v11;
        v12 = (TextView)  findViewById(R.id.v12);
        allTextViews[11] = v12;
        v13 = (TextView)  findViewById(R.id.v13);
        allTextViews[12] = v13;
        v14 = (TextView)  findViewById(R.id.v14);
        allTextViews[13] = v14;
        v15 = (TextView)  findViewById(R.id.v15);
        allTextViews[14] = v15;
        v16 = (TextView)  findViewById(R.id.v16);
        allTextViews[15] = v16;
        v17 = (TextView)  findViewById(R.id.v17);
        allTextViews[16] = v17;
        v18 = (TextView)  findViewById(R.id.v18);
        allTextViews[17] = v18;
        v19 = (TextView)  findViewById(R.id.v19);
        allTextViews[18] = v19;
        v20 = (TextView)  findViewById(R.id.v20);
        allTextViews[19] = v20;
        v21 = (TextView)  findViewById(R.id.v21);
        allTextViews[20] = v21;
        v22 = (TextView)  findViewById(R.id.v22);
        allTextViews[21] = v22;
        v23 = (TextView)  findViewById(R.id.v23);
        allTextViews[22] = v23;
        v24 = (TextView)  findViewById(R.id.v24);
        allTextViews[23] = v24;
        v25 = (TextView)  findViewById(R.id.v25);
        allTextViews[24] = v25;
        v26 = (TextView)  findViewById(R.id.v26);
        allTextViews[25] = v26;
        v27 = (TextView)  findViewById(R.id.v27);
        allTextViews[26] = v27;
        v28 = (TextView)  findViewById(R.id.v28);
        allTextViews[27] = v28;
        v29 = (TextView)  findViewById(R.id.v29);
        allTextViews[28] = v29;
        v30 = (TextView)  findViewById(R.id.v30);
        allTextViews[29] = v30;
        v31 = (TextView)  findViewById(R.id.v31);
        allTextViews[30] = v31;
        v32 = (TextView)  findViewById(R.id.v32);
        allTextViews[31] = v32;
        v33 = (TextView)  findViewById(R.id.v33);
        allTextViews[32] = v33;
        v34 = (TextView)  findViewById(R.id.v34);
        allTextViews[33] = v34;
        v35 = (TextView)  findViewById(R.id.v35);
        allTextViews[34] = v35;
        v36 = (TextView)  findViewById(R.id.v36);
        allTextViews[35] = v36;
        v37 = (TextView)  findViewById(R.id.v37);
        allTextViews[36] = v37;
        v38 = (TextView)  findViewById(R.id.v38);
        allTextViews[37] = v38;
        v39 = (TextView)  findViewById(R.id.v39);
        allTextViews[38] = v39;
        v40 = (TextView)  findViewById(R.id.v40);
        allTextViews[39] = v40;

        setAllInvisible();
        gorSayfasiniOlustur();

        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.ekle));
        spec.setContent(R.id.EKLE);
        spec.setIndicator(getResources().getString(R.string.ekle));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.gor));
        spec.setContent(R.id.GOR);
        spec.setIndicator(getResources().getString(R.string.gor));
        host.addTab(spec);


        baslangic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isBaslangis = true;
                showDialog(0);
            }
        });

        bitis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isBaslangis = false;
                showDialog(0);
            }
        });




        gunPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                gun = selected;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        kaydet_ekle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ders_adi = ders_harf.getText().toString().toUpperCase()+ders_rakam.getText().toString().toUpperCase();
                if(!baslangic.getText().toString().equals("") && !bitis.getText().toString().equals("") && !ders_harf.getText().toString().equals("") && !ders_rakam.getText().toString().equals(""))
                {
                    mydb.dersEkle(ders_adi,gun,baslangic.getText().toString(),bitis.getText().toString());
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.kaydedildi),Toast.LENGTH_SHORT).show();
                    gorSayfasiniYenile(gun);
                    baslangic.setText("");
                    bitis.setText("");
                    ders_harf.setText("");
                    ders_rakam.setText("");
                }
                else
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.lutfen),Toast.LENGTH_SHORT).show();

            }
        });
    }

    public int getColor()
    {
        if(currentColor == -1)
        {
            currentColor = R.color.color1;
            return R.color.color1;
        }
        else
        {
            if(currentColor == R.color.color1) {
                currentColor = R.color.color2;
                return R.color.color2;
            }
            else if(currentColor == R.color.color2)
            {
                currentColor = R.color.color3;
                return R.color.color3;
            }

            else if(currentColor == R.color.color3)
            {
                currentColor = R.color.color4;
                return R.color.color4;
            }

            else if(currentColor == R.color.color4)
            {
                currentColor = R.color.color1;
                return R.color.color1;
            }
            else
            {
                currentColor = R.color.color1;
                return R.color.color1;
            }
        }
    }

    public void setAllInvisible()
    {
        for(TextView t : allTextViews)
            t.setVisibility(View.INVISIBLE);
    }

    public void gorSayfasiniYenile(String gun)
    {
        clearTextViews(gun);
        ArrayList<String[]> all = mydb.getTumDersler(gun);
        if(all.size() != 0)
        {
            for(int i = 0; i < all.get(0).length ; i++)
            {
                gorSayfasinaEkle(all.get(0)[i], all.get(1)[i], all.get(2)[i], all.get(3)[i]);
            }

        }
    }

    public void gorSayfasiniOlustur()
    {
        ArrayList<String[]> all = mydb.getTumDersler("");
        if(all.size() != 0)
        {
            for(int i = 0; i < all.get(0).length ; i++)
            {
                gorSayfasinaEkle(all.get(0)[i], all.get(1)[i], all.get(2)[i], all.get(3)[i]);
            }

        }

    }

    public void clearTextViews(String dersGun)
    {
        if(dersGun.equalsIgnoreCase(getResources().getString(R.string.pztb)))
        {
            for(int i = 0 ; i < 36 ; i+=5)
            {
                allTextViews[i].setText("");
                allTextViews[i].setVisibility(View.INVISIBLE);
            }

        }
        else if(dersGun.equalsIgnoreCase(getResources().getString(R.string.salib)))
        {
            for(int i = 1 ; i < 37 ; i+=5)
            {
                allTextViews[i].setText("");
                allTextViews[i].setVisibility(View.INVISIBLE);
            }
        }
        else if(dersGun.equalsIgnoreCase(getResources().getString(R.string.crsb)))
        {
            for(int i = 2 ; i < 38 ; i+=5)
            {
                allTextViews[i].setText("");
                allTextViews[i].setVisibility(View.INVISIBLE);
            }
        }
        else if(dersGun.equalsIgnoreCase(getResources().getString(R.string.prsb)))
        {
            for(int i = 3 ; i < 39 ; i+=5)
            {
                allTextViews[i].setText("");
                allTextViews[i].setVisibility(View.INVISIBLE);
            }
        }
        else if(dersGun.equalsIgnoreCase(getResources().getString(R.string.cumab)))
        {
            for(int i = 4 ; i < 40 ; i+=5)
            {
                allTextViews[i].setText("");
                allTextViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }
    public void gorSayfasinaEkle(String dersAdi, String dersGun, String baslangic, String bitis)
    {
        ders_girilmemis.setVisibility(View.INVISIBLE);
        String tmp = dersAdi+" "+baslangic+" "+bitis;
        if(dersGun.equalsIgnoreCase(getResources().getString(R.string.pztb)))
        {
            for(int i = 0 ; i < 36 ; i+=5)
            {
                if(allTextViews[i].getText().toString().equals(""))
                {
                    allTextViews[i].setText(tmp);
                    allTextViews[i].setVisibility(View.VISIBLE);
                    allTextViews[i].setBackgroundResource(getColor());
                    break;
                }
            }
        }
        else if (dersGun.equalsIgnoreCase(getResources().getString(R.string.salib)))
        {
            for(int i = 1 ; i < 37 ; i+=5)
            {
                if(allTextViews[i].getText().toString().equals(""))
                {
                    allTextViews[i].setText(tmp);
                    allTextViews[i].setVisibility(View.VISIBLE);
                    allTextViews[i].setBackgroundResource(getColor());
                    break;
                }
            }
        }
        else if (dersGun.equalsIgnoreCase(getResources().getString(R.string.crsb)))
        {
            for(int i = 2 ; i < 38 ; i+=5)
            {
                if(allTextViews[i].getText().toString().equals(""))
                {
                    allTextViews[i].setText(tmp);
                    allTextViews[i].setVisibility(View.VISIBLE);
                    allTextViews[i].setBackgroundResource(getColor());
                    break;
                }
            }
        }
        else if (dersGun.equalsIgnoreCase(getResources().getString(R.string.prsb)))
        {
            for(int i = 3 ; i < 39 ; i+=5)
            {
                if(allTextViews[i].getText().toString().equals(""))
                {
                    allTextViews[i].setText(tmp);
                    allTextViews[i].setVisibility(View.VISIBLE);
                    allTextViews[i].setBackgroundResource(getColor());
                    break;
                }
            }
        }
        else if (dersGun.equalsIgnoreCase(getResources().getString(R.string.cumab)))
        {
            for(int i = 4 ; i < 40 ; i+=5)
            {
                if(allTextViews[i].getText().toString().equals(""))
                {
                    allTextViews[i].setText(tmp);
                    allTextViews[i].setVisibility(View.VISIBLE);
                    allTextViews[i].setBackgroundResource(getColor());
                    break;
                }
            }
        }
    }

    public void setTime()
    {
        String hour,min;
        hour = ""+hour_x;
        min = ""+minute_x;
        if(hour_x <= 9)
            hour = "0"+hour_x;
        if(minute_x <= 9)
            min = "0"+minute_x;

        String time = ""+hour+":"+min;
        if (isBaslangis)
            baslangic.setText(time);
        else
            bitis.setText(time);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 0)
        {
            return new TimePickerDialog(DersGirme.this, kTimePickerListener, hour_x, minute_x,true);
        }
        return null;
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


    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    hour_x = hourOfDay;
                    minute_x = minute;
                    setTime();
                }
            };
}
