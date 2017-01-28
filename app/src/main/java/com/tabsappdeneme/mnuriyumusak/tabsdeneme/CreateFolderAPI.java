package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

/**
 * Created by Nuri on 28.01.2017.
 */

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder.DriveFolderResult;
import com.google.android.gms.drive.MetadataChangeSet;

public class CreateFolderAPI extends BaseDemoActivity{
    DBHelper mydb;
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        mydb = new DBHelper(this);

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle("TabsAPP").build();
        Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(
                getGoogleApiClient(), changeSet).setResultCallback(callback);

    }

    final ResultCallback<DriveFolder.DriveFolderResult> callback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            else
            {
                mydb.folderEkle("TabsAPP", result.getDriveFolder().getDriveId().encodeToString());
            }
            showMessage("Folder Created");
        }
    };
}
