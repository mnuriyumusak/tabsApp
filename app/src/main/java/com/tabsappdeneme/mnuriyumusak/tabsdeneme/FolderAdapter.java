package com.tabsappdeneme.mnuriyumusak.tabsdeneme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nuri on 2.02.2017.
 */

public class FolderAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<String> folderNames;

    public FolderAdapter(Context context, ArrayList<String> folderNames) {
        this.mContext = context;
        this.folderNames = folderNames;
    }

    // 2
    @Override
    public int getCount() {
        return folderNames.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return folderNames.get(position);
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String folderName = folderNames.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.folder_item, null);
        }

        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_folder_name);
        nameTextView.setText(folderName);
        return convertView;
    }

}
