package tabsapp.tabsapp.mnuriyumusak.tabsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class DilSecimActivity extends AppCompatActivity {

    Button kaydet_button;
    Spinner dil;
    String selectedLanguage;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dil_secim);
        mydb = new DBHelper(this);

        if(mydb.isVeryFirstGiris())
        {
            kaydet_button = (Button) findViewById(R.id.dil_kaydet);
            dil = (Spinner) findViewById(R.id.dil_secim);

            dil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView parent, View view, int position, long id) {
                    String selected = (String) parent.getItemAtPosition(position);

                    if(selected.equals("Türkçe"))
                    {
                        selectedLanguage = "";
                        kaydet_button.setText("KAYDET");
                        mydb.dilEkle("tr");
                    }
                    else
                    {
                        selectedLanguage = "en";
                        kaydet_button.setText("SAVE");
                        mydb.dilEkle("en");
                    }

                    String languageToLoad = selectedLanguage; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            kaydet_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(DilSecimActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(DilSecimActivity.this);
                    builder.setMessage(getResources().getString(R.string.dil_secim)).setPositiveButton(getResources().getString(R.string.evet), dialogClickListener)
                            .setNegativeButton(getResources().getString(R.string.hayir), dialogClickListener).show();


                }
            });
        }
        else
        {
            Intent intent = new Intent(DilSecimActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
