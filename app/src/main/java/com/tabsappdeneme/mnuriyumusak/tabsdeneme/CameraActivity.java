package com.tabsappdeneme.mnuriyumusak.tabsdeneme;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;

public class CameraActivity extends AppCompatActivity {

    Button btnTakePhoto; //foto çekme butonu
    ImageView imgTakenPhoto; //fonun çekilince nerde gösterileceği
    private static final int CAM_REQUEST = 1313;
    PictureNameCreator pnc = new PictureNameCreator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!hasPermissions())
        {
            requestPerms();
        }

        btnTakePhoto = (Button) findViewById(R.id.cek_button);
        imgTakenPhoto = (ImageView) findViewById(R.id.imageview1);
        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
    }

    //başarılı bir çekim işleminin sonucu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST)
        {
            Intent intent = new Intent(this, AfterPicture.class);
            startActivity(intent);
        }
    }


    private boolean hasPermissions(){
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,100);
        }
    }

    //camera açma isteğinin gittiği yer
    class btnTakePhotoClicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File picSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/tabsApp");
            if(!picSavePath.exists())
            {
                picSavePath.mkdir();
            }
            String picName = pnc.getPictureName();
            File imageFile = new File(picSavePath, picName);
            Uri pictureUri = Uri.fromFile(imageFile);
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
            startActivityForResult(cameraintent, CAM_REQUEST);
        }




    }


}