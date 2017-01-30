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
import android.widget.Toast;

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
    DBHelper mydb;
    boolean dersExist = false;
    private String rootFolderName = "TabsAPP12";
    ArrayList<String[]> all;
    ArrayList<String[]> yuklenmemisResimler;
    String[] tumDersAdlari;
    String currentDersAdi;
    int index = 0;
    int fotoIndex = 0;
    File sd;
    BitmapFactory.Options bmOptions;

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

    public void createDersFolders()
    {
        if(!dersExist)
        {
            DriveId myid = DriveId.decodeFromString(mydb.getFolderId(rootFolderName));
            mFolderDriveId = myid;
            DriveFolder folder = myid.asDriveFolder();
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(currentDersAdi).build();
            folder.createFolder(getGoogleApiClient(), changeSet)
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
        }
    }

    public void createRootFolder()
    {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(rootFolderName).build();
        Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(
                getGoogleApiClient(), changeSet).setResultCallback(callback);
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
            folder.queryChildren(getGoogleApiClient(), query)
                    .setResultCallback(metadataCallback);

        }catch (IllegalArgumentException e)
        {
            dersExist = false;
        }
    }

    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        mydb = new DBHelper(this);
        all = mydb.getTumDersler("");
        tumDersAdlari = all.get(0);
        currentDersAdi = tumDersAdlari[index];
        yuklenmemisResimler = mydb.getYuklenmemisResimler();
        sd = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        bmOptions = new BitmapFactory.Options();
        fotoIndex = 0;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                createAllFolders();
                uploadImages();
            }
        });
    }

    public void uploadImages()
    {
        if(yuklenmemisResimler!= null)
        {
            final int arrayIndex = fotoIndex;
            File image = new File(sd+"/tabsApp/"+yuklenmemisResimler.get(1)[arrayIndex], yuklenmemisResimler.get(0)[arrayIndex]);
            final Bitmap bitmapImage = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(DriveApi.DriveContentsResult result) {
                            if (!result.getStatus().isSuccess()) {
                                return;
                            }
                            OutputStream outputStream = result.getDriveContents().getOutputStream();
                            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bitmapStream);
                            try {
                                outputStream.write(bitmapStream.toByteArray());
                            } catch (IOException e1) {
                            }
                            MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                    .setMimeType("image/jpeg").setTitle(yuklenmemisResimler.get(0)[arrayIndex]).build();

                            DriveId myid = DriveId.decodeFromString(mydb.getFolderId(yuklenmemisResimler.get(1)[arrayIndex]));
                            mFolderDriveId = myid;
                            DriveFolder folder = myid.asDriveFolder();
                            folder.createFile(getGoogleApiClient(),metadataChangeSet,result.getDriveContents());
                            mydb.photoDriveaYuklendi(yuklenmemisResimler.get(0)[arrayIndex]);
                            fotoIndex++;
                            if(fotoIndex < yuklenmemisResimler.get(0).length)
                            {
                                uploadImages();
                            }
                            else
                            {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
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
                    folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
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
                    if(index < tumDersAdlari.length)
                    {
                        currentDersAdi = tumDersAdlari[index];
                        index++;
                        isDersFolderExist(rootFolderName,currentDersAdi);
                    }
                    return;
                }
            };



}
