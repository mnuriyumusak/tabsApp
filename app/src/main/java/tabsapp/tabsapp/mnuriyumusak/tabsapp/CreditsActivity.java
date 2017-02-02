package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class CreditsActivity extends AppCompatActivity {

    DBHelper mydb;

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
        setContentView(R.layout.activity_credits);
        mydb = new DBHelper(this);

        //drawer things
        navigationView = (NavigationView) findViewById(R.id.navigation_view_credits);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_credits);
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
                if(!mydb.isIlkGiris())
                {
                    switch(item.getItemId())
                    {
                        case R.id.nav_main_activity:
                            intent = new Intent(CreditsActivity.this, MainActivity.class);
                            intent.putExtra("isFromAnother", true);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_take_picture:
                            intent = new Intent(CreditsActivity.this, CameraActivity.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_ders_girme:
                            intent = new Intent(CreditsActivity.this, DersGirme.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_ders_ekleme:
                            intent = new Intent(CreditsActivity.this, DersEkleme.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_drive_api:
                            intent = new Intent(CreditsActivity.this, DriveApi.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                        case R.id.nav_gallery:
                            intent = new Intent(CreditsActivity.this, GalleryFolderActivity.class);
                            startActivity(intent);
                            item.setChecked(true);
                            break;
                    }
                }

                return false;
            }
        });




    }

}
