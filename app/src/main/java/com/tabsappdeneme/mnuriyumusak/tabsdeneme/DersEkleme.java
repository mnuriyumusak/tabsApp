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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nuri on 17.01.2017.
 */

public class DersEkleme extends Activity {
    Button kaydet_button;
    EditText university;
    EditText nick;
    Switch bulut;
    private DBHelper mydb ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ders_ekleme);
        mydb = new DBHelper(this);

        kaydet_button = (Button) findViewById(R.id.kaydet_button);
        university = (EditText) findViewById(R.id.university_field);
        nick = (EditText) findViewById(R.id.nick_field);
        bulut = (Switch) findViewById(R.id.bulut_switch);

        kaydet_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uniName = university.getText().toString();
                String nickName = nick.getText().toString();
                int isBulut = (bulut.isChecked())?1:0;
                mydb.addKayıtInfos(uniName,nickName,isBulut);
            }
        });


    }
}
