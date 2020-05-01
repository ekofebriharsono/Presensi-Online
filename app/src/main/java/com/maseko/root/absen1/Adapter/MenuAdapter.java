package com.maseko.root.absen1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maseko.root.absen1.R;

public class MenuAdapter extends BaseAdapter {

    Context context;
    int logos[];
    String titles[];
    LayoutInflater inflater;
    String menuPresensi;

    public MenuAdapter(Context applicationContext, int[] logos, String[] titles, String menuPresensi) {
        this.context = applicationContext;
        this.logos = logos;
        this.titles = titles;
        this.menuPresensi = menuPresensi;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return logos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



//        if (menuPresensi.equals("menuPresensi")){
//            convertView = inflater.inflate(R.layout.item_menu, null);
//            ImageView icon = convertView.findViewById(R.id.icon);
//            TextView title = convertView.findViewById(R.id.title);
//            icon.setImageResource(logos[position]);
//            title.setText(titles[position]);
//        }else{
            convertView = inflater.inflate(R.layout.item_menu, null);
            ImageView icon = convertView.findViewById(R.id.icon);
            TextView title = convertView.findViewById(R.id.title);
            icon.setImageResource(logos[position]);
            title.setText(titles[position]);
//        }

        return convertView;
    }
}
