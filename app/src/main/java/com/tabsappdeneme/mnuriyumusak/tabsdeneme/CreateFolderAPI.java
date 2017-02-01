package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

/**
 * Created by Nuri on 28.01.2017.
 */

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.AsyncTaskCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.*;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder.DriveFolderResult;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreateFolderAPI extends BaseDemoActivity{
    private DriveId mFolderDriveId = null;
    private GoogleApiClient myGoogleApiClient;
    DBHelper mydb;
    boolean dersExist = false;
    private String rootFolderName = "TabsAPP37";
    private String tahtaSubFolder = "Tahta Fotograflari";
    private String dersNotuSubFolder = "Ders Notlari";
    ArrayList<String[]> all;
    ArrayList<String[]> yuklenmemisResimler;
    String[] tumDersAdlari;
    String currentDersAdi;
    public int index = 0;
    int fotoIndex = 0;
    File sd;
    BitmapFactory.Options bmOptions;
    String currentSubFolderName;
    boolean inside=false;
    boolean continueBabe = false;

    //upload images
    OutputStream outputStream;
    ByteArrayOutputStream bitmapStream;
    File image;
    Bitmap bitmapImage;
    DriveApi.DriveContentsResult myResult;
    DriveFolder myFolder;
    MetadataChangeSet myMetadataChangeSet;
    public boolean isTabsAPPFolderExist()
    {
        boolean result = true;
        try //TabsAPP folder var
        {
            DriveId myid = DriveId.decodeFromString(mydb.getFolderId(rootFolderName));
            mFolderDriveId = myid;
        }catch (IllegalArgumentException e) //ana folder dahi yok
        {
            result = false;
        }
        finally {
            return result;
        }
    }

    public void createAllFolders()
    {
        if(!isTabsAPPFolderExist())
        {
            createRootFolder();
        }
        else
        {
            if(all.size() != 0)
            {
                index++;
                isDersFolderExist(rootFolderName,currentDersAdi);
            }
            else
            {
            }
        }
    }

    public void createTahtaFotosuFolder()
    {
        DriveId myid = DriveId.decodeFromString(mydb.getFolderId(currentDersAdi));
        mFolderDriveId = myid;
        DriveFolder folder = myid.asDriveFolder();
        currentSubFolderName = tahtaSubFolder;
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(currentSubFolderName).build();
        folder.createFolder(myGoogleApiClient, changeSet)
                .setResultCallback(createSubFolderCallback);
    }

    public void createDersNotuFolder()
    {
        DriveId myid = DriveId.decodeFromString(mydb.getFolderId(currentDersAdi));
        mFolderDriveId = myid;
        DriveFolder folder = myid.asDriveFolder();
        currentSubFolderName = dersNotuSubFolder;
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(currentSubFolderName).build();
        folder.createFolder(myGoogleApiClient, changeSet)
                .setResultCallback(createSubFolderCallback);
    }

    public void createDersFolders()
    {
        if(!dersExist)
        {
            DriveId myid = DriveId.decodeFromString(mydb.getFolderId(rootFolderName));
            mFolderDriveId = myid;
            DriveFolder folder = myid.asDriveFolder();
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(currentDersAdi).build();
            folder.createFolder(myGoogleApiClient, changeSet)
                    .setResultCallback(createFolderCallback);

        }
        else
        {
            if(index < tumDersAdlari.length)
            {
                currentDersAdi = tumDersAdlari[index];
                index++;
                isDersFolderExist(rootFolderName,currentDersAdi);
            }
            else
                uploadImages();

        }
    }

    public void createRootFolder()
    {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(rootFolderName).build();
        Drive.DriveApi.getRootFolder(myGoogleApiClient).createFolder(
                myGoogleApiClient, changeSet).setResultCallback(callback);
    }

    public void isDersFolderExist(String rootFolder,String dersAdi)
    {
        dersExist = true;
        try
        {
            DriveId myid = DriveId.decodeFromString(mydb.getFolderId(rootFolderName));
            mFolderDriveId = myid;
            DriveFolder folder = myid.asDriveFolder();
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.TITLE, dersAdi))
                    .build();
            folder.queryChildren(myGoogleApiClient, query)
                    .setResultCallback(metadataCallback);

        }catch (IllegalArgumentException e)
        {
            dersExist = false;
        }
    }

    public CreateFolderAPI(DBHelper db,File mysd,GoogleApiClient client)
    {
        mydb = db;
        sd = mysd;
        all = mydb.getTumDersler("");
        tumDersAdlari = all.get(0);
        currentDersAdi = tumDersAdlari[index];
        yuklenmemisResimler = mydb.getResimler(false);
        bmOptions = new BitmapFactory.Options();
        fotoIndex = 0;
        myGoogleApiClient = client;
    }
    public void doTaskBabe( )
    {
        if(!inside)
        {
            inside = true;
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    createAllFolders();
                }
            });
        }
    }

    public void onConnected(Bundle connectionHint) {
        //super.onConnected(connectionHint);
        //setContentView(R.layout.bulut_yukleme_ekrani);

        if(!inside)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mydb = new DBHelper(CreateFolderAPI.this);
                    all = mydb.getTumDersler("");
                    tumDersAdlari = all.get(0);
                    currentDersAdi = tumDersAdlari[index];
                    yuklenmemisResimler = mydb.getResimler(false);
                    sd = getExternalFilesDir(Environment.DIRECTORY_DCIM);
                    bmOptions = new BitmapFactory.Options();
                    fotoIndex = 0;
                    inside = true;
                    myGoogleApiClient = getGoogleApiClient();
                    createAllFolders();
                }
            });
        }
    }

    public void uploadImagesSupport()
    {
        if(continueBabe)
        {

            uploadImages();
        }
        else
        {

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    public void uploadImages()
    {
        if(yuklenmemisResimler!= null)
        {
            continueBabe = false;
            final int arrayIndex = fotoIndex;
            final String subFolder;
            if(yuklenmemisResimler.get(2)[arrayIndex].equals("0"))
                subFolder = tahtaSubFolder;
            else
                subFolder = dersNotuSubFolder;

            image = new File(sd+"/tabsApp/"+yuklenmemisResimler.get(1)[arrayIndex]+"/"+subFolder, yuklenmemisResimler.get(0)[arrayIndex]);
            bitmapImage = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            Drive.DriveApi.newDriveContents(myGoogleApiClient)
                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(DriveApi.DriveContentsResult result) {
                            myResult = result;
                            if (!result.getStatus().isSuccess()) {
                                return;
                            }
                            outputStream = result.getDriveContents().getOutputStream();
                            bitmapStream = new ByteArrayOutputStream();
                            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);
                            try {
                                outputStream.write(bitmapStream.toByteArray());
                            } catch (IOException e1) {
                            }
                            myMetadataChangeSet = new MetadataChangeSet.Builder()
                                    .setMimeType("image/jpeg").setTitle(yuklenmemisResimler.get(0)[arrayIndex]).build();


                            DriveId myid = DriveId.decodeFromString(mydb.getFolderId(yuklenmemisResimler.get(1)[arrayIndex]+"-"+subFolder));
                            mFolderDriveId = myid;
                            myFolder = myid.asDriveFolder();
                            myFolder.createFile(myGoogleApiClient,myMetadataChangeSet,myResult.getDriveContents());
                            mydb.photoDriveaYuklendi(yuklenmemisResimler.get(0)[arrayIndex]);
                            fotoIndex++;

                            if(fotoIndex < yuklenmemisResimler.get(0).length)
                            {
                                continueBabe = true;
                                uploadImagesSupport();
                                return;
                            }
                            else
                            {

                                //Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                //startActivity(intent);
                                return;
                            }
                        }
                    });
        }

    }

    final ResultCallback<DriveFolder.DriveFolderResult> callback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            else
            {
                mydb.folderEkle(rootFolderName, result.getDriveFolder().getDriveId().encodeToString());
                mFolderDriveId = result.getDriveFolder().getDriveId();
            }
            if(all.size() != 0)
            {
                index++;
                isDersFolderExist(rootFolderName,currentDersAdi);
            }
            return;
        }
    };

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error 1");
                        return;
                    }
                    DriveFolder folder = mFolderDriveId.asDriveFolder();
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("image/jpg")
                            .setStarred(true).build();
                    folder.createFile(myGoogleApiClient, changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                    return;
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback =
            new ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        return;
                    }
                    return;
                }
            };

    final private ResultCallback<DriveApi.MetadataBufferResult> metadataCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                    }
                    else
                    {
                        if(result.getMetadataBuffer().getCount() == 0)
                            dersExist = false;
                        else
                            dersExist = true;
                    }
                    result.release();
                    createDersFolders();
                    return;
                }
            };

    final ResultCallback<DriveFolderResult> createFolderCallback = new
            ResultCallback<DriveFolderResult>() {
                @Override
                public void onResult(DriveFolderResult result) {
                    if (!result.getStatus().isSuccess()) {
                        return;
                    }
                    mydb.folderEkle(currentDersAdi, result.getDriveFolder().getDriveId().encodeToString());
                    createTahtaFotosuFolder();
                    return;
                }
            };

    final ResultCallback<DriveFolderResult> createSubFolderCallback = new
            ResultCallback<DriveFolderResult>() {
                @Override
                public void onResult(DriveFolderResult result) {
                    if (!result.getStatus().isSuccess()) {
                        return;
                    }
                    mydb.folderEkle(currentDersAdi+"-"+currentSubFolderName, result.getDriveFolder().getDriveId().encodeToString());
                    if(currentSubFolderName.equals(dersNotuSubFolder))
                    {
                        if(index < tumDersAdlari.length)
                        {
                            currentDersAdi = tumDersAdlari[index];
                            index++;
                            isDersFolderExist(rootFolderName,currentDersAdi);
                        }
                        else
                            uploadImages();
                        return;
                    }
                    else
                        createDersNotuFolder();
                }
            };



}
