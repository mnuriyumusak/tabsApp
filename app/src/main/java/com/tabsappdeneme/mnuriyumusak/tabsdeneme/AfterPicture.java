package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nuri on 16.01.2017.
 */

public class AfterPicture extends Activity {
    ImageView imgTakenPhoto; //fonun çekilince nerde gösterileceği
    Button yenidenCek;
    Button kaydet;
    PictureNameCreator pnc = new PictureNameCreator();
    private static final int CAM_REQUEST = 1313;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_take_picture);
        imgTakenPhoto = (ImageView) findViewById(R.id.after_take_imageview);
        yenidenCek = (Button) findViewById(R.id.yenidencek_button);
        kaydet = (Button) findViewById(R.id.ok_button);
        yenidenCek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File picSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp");
                String picName = pnc.getPictureName();
                File imageFile = new File(picSavePath, picName);
                imageFile.delete();

                Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if(!picSavePath.exists())
                {
                    picSavePath.mkdir();
                }
                String picName2 = pnc.getPictureName();
                File imageFile2 = new File(picSavePath, picName2);
                Uri pictureUri = Uri.fromFile(imageFile2);
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(cameraintent, CAM_REQUEST);
            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_SHORT).show();
            }
        });

        CameraActivity ca = new CameraActivity();
        File picSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp");
        String picName = pnc.getPictureName();
        File imageFile = new File(picSavePath, picName);
        Bitmap thumbnail = BitmapFactory.decodeFile(imageFile.getPath());

        imgTakenPhoto.setImageBitmap(thumbnail);
    }


}
