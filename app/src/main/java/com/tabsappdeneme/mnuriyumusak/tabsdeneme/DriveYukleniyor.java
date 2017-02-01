package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Nuri on 1.02.2017.
 */

public class DriveYukleniyor extends AppCompatActivity {

    DBHelper mydb;
    public TextView gecici;
    private int mInterval = 500; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private boolean notificationSended = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulut_yukleme_ekrani);
        mydb = new DBHelper(this);
        gecici = (TextView) findViewById(R.id.kalan_sayisi);

        mHandler = new Handler();
        startRepeatingTask();
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_cloud_upload_black_24dp)
                        .setContentTitle("Tabs - Dosyalar Drive'a Atıldı")
                        .setContentText("Fotoğraflarınız başarılı bir şekilde buluta aktarıldı.");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void updateStatus()
    {
        int sayi = mydb.getYuklenmemisResimSayisi();
        gecici.setText(""+sayi);
        if(sayi == 0 && !notificationSended)
        {
            addNotification();
            notificationSended = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

}
