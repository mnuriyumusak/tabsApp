package tabsapp.tabsapp.mnuriyumusak.tabsapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

import java.io.File;

/**
 * Created by Nuri on 16.01.2017.
 */

public class AfterPicture extends AppCompatActivity {
    ImageView imgTakenPhoto; //fonun çekilince nerde gösterileceği
    Button yenidenCek;
    Button kaydet;
    PictureNameCreator pnc;
    DBHelper mydb;
    private static final int CAM_REQUEST = 1313;
    public boolean isTahtaFotosu ;
    public String manuelDersAdi ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_take_picture);
        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        isTahtaFotosu = extras.getBoolean("isTahtaFotosu");
        manuelDersAdi = extras.getString("manuelDersAdi");


        File externalPath = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");
        pnc = new PictureNameCreator(externalPath);

        imgTakenPhoto = (ImageView) findViewById(R.id.after_take_imageview);
        yenidenCek = (Button) findViewById(R.id.yenidencek_button);
        kaydet = (Button) findViewById(R.id.ok_button);

        File imageFile = pnc.getPictureImageFile(mydb,true,false,isTahtaFotosu,manuelDersAdi);
        Bitmap thumbnail = BitmapFactory.decodeFile(imageFile.getPath());
        imgTakenPhoto.setImageBitmap(thumbnail);

        yenidenCek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File imageFile = pnc.getPictureImageFile(mydb,true,false,isTahtaFotosu,manuelDersAdi);
                imageFile.delete();
                Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Uri pictureUri = pnc.getPictureSavePath(mydb,true,isTahtaFotosu,manuelDersAdi);
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(cameraintent, CAM_REQUEST);

            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String photoName = pnc.getPictureName(mydb,false, true,isTahtaFotosu,manuelDersAdi);
                Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_SHORT).show();
                mydb.addNewPhoto(photoName, pnc.getDersAdi(mydb),isTahtaFotosu);
                Intent intent = new Intent(AfterPicture.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    //başarılı bir çekim işleminin sonucu, tekrar kendi classına dönmesini sağlar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST)
        {
            Intent intent = new Intent(this, AfterPicture.class);
            intent.putExtra("isTahtaFotosu", isTahtaFotosu);
            intent.putExtra("manuelDersAdi", manuelDersAdi);
            startActivity(intent);
        }
    }


}
