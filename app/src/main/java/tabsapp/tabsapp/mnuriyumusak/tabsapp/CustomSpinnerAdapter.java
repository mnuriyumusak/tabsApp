package tabsapp.tabsapp.mnuriyumusak.tabsapp;

/**
 * Created by Nuri on 31.01.2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context;
    String[] dersAdlari;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context applicationContext, String[] dersAdlari) {
        this.context = applicationContext;
        this.dersAdlari = dersAdlari;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return dersAdlari.length;
    }

    @Override
    public Object getItem(int i) {
        return dersAdlari[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.spinner_text_view);
        names.setText(dersAdlari[i]);
        return view;
    }


}