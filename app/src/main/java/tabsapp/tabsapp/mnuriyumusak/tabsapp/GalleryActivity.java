package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nuri on 1.02.2017.
 */

public class GalleryActivity extends AppCompatActivity  {

    DBHelper mydb;
    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    private File imageFile;
    private String tahtaSubFolder;
    private String dersNotuSubFolder;

    private String secilenDersAdi;
    private boolean isDersNotu;

    //drawer things
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private Toolbar myToolBar;
    private NavigationView navigationView;

    //yukardaki soldaki menu butonuna basınca menünün açılması için
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

        tahtaSubFolder = getResources().getString(R.string.tahta_fotolari);
        dersNotuSubFolder = getResources().getString(R.string.ders_notlari);

        Bundle bundle = getIntent().getExtras();

        secilenDersAdi = bundle.getString("secilenDersAdi");
        isDersNotu = bundle.getBoolean("isDersNotu");

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_gallery);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_gallery);
        myToggle = new ActionBarDrawerToggle(this,myDrawer,R.string.open, R.string.close);
        myToolBar = (Toolbar)  findViewById(R.id.nav_action);

        setSupportActionBar(myToolBar);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.nav_main_activity:
                        intent = new Intent(GalleryActivity.this, MainActivity.class);
                        intent.putExtra("isFromAnother", true);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_take_picture:
                        intent = new Intent(GalleryActivity.this, CameraActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_girme:
                        intent = new Intent(GalleryActivity.this, DersGirme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_ekleme:
                        intent = new Intent(GalleryActivity.this, DersEkleme.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_drive_api:
                        intent = new Intent(GalleryActivity.this, DriveApi.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_ders_hakkinda:
                        intent = new Intent(GalleryActivity.this, CreditsActivity.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        mydb = new DBHelper(this);

        ArrayList<String[]> tumResimler = mydb.getResimler(secilenDersAdi,isDersNotu);
        ArrayList<ImageModel> data = new ArrayList<>();
        String[] IMGS = null;

        File externalPath = null;
        if(mydb.hasSDKart())
            externalPath = new File(getApplicationContext().getExternalCacheDirs()[1].getPath().toString()+"/Fotolar");
        else
            externalPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());

        String subFolder;
        //gallery işleri burdan sonra başlıyor
        if(tumResimler != null)
        {
            IMGS = new String[tumResimler.get(0).length];
            for(int i = 0; i < tumResimler.get(0).length ; i++)
            {
                if(tumResimler.get(2)[i].equals("0"))
                    subFolder = tahtaSubFolder;
                else
                    subFolder = dersNotuSubFolder;

                imageFile = new File(externalPath+"/tabsApp/"+tumResimler.get(1)[i]+"/"+subFolder, tumResimler.get(0)[i]);
                IMGS[i] = imageFile.getPath().toString();
            }
        }
        if(IMGS != null)
        {
            for (int i = 0; i < IMGS.length; i++)
            {
                ImageModel imageModel = new ImageModel();
                imageModel.setName("Image " + i);
                imageModel.setUrl(IMGS[i]);
                data.add(imageModel);
            }
        }

        final String[] allIMGS = IMGS;

        mRecyclerView = (RecyclerView) findViewById(R.id.gallery_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(GalleryActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intentquick = new Intent(Intent.ACTION_VIEW);
                Uri imgUri = Uri.fromFile(new File(allIMGS[position]));
                intentquick.setDataAndType(imgUri,"image/*");
                intentquick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentquick);
            }

            @Override
            public void onLongClick(final View view, final int position) {
                final CharSequence[] items = {getResources().getString(R.string.delete)};

                AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
                builder.setTitle(getResources().getString(R.string.seciniz));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0)
                        {
                            File deletedOne = new File(allIMGS[position]);
                            deletedOne.delete();Glide.clear(view);
                            Glide.get(getApplicationContext()).clearMemory();
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                public void run() {
                                    Glide.get(getApplicationContext()).clearDiskCache();
                                }
                            };
                            mydb.deletePhoto(allIMGS[position]);
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.silindi),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GalleryActivity.this, GalleryActivity.class);
                            intent.putExtra("secilenDersAdi", secilenDersAdi);
                            intent.putExtra("isDersNotu", isDersNotu);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }

        }));



    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

}




class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private GalleryActivity.ClickListener clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recycleView, final GalleryActivity.ClickListener clicklistener){

        this.clicklistener=clicklistener;
        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null){
                    clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
            clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
