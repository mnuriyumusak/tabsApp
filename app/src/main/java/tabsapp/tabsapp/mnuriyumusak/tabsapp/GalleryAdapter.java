package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nuri on 2.02.2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ImageModel> data = new ArrayList<>();

    public GalleryAdapter(Context context, List<ImageModel> data) {
            this.context = context;
            this.data = data;
            }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.list_item, parent, false);
            viewHolder = new MyItemHolder(v);
            return viewHolder;
            }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Glide.with(context).load(new File(data.get(position).getUrl()))
            .thumbnail(0.5f)
            .override(200,200)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(((MyItemHolder) holder).mImg);

    }



    @Override
    public int getItemCount() {
            return data.size();
            }

    public static class MyItemHolder extends RecyclerView.ViewHolder   {
        ImageView mImg;

        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
        }



    }


}