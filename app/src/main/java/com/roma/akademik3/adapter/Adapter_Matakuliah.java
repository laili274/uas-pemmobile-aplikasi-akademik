package com.roma.akademik3.adapter;

//TODO 6 buat adapter untuk ddatanya

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roma.akademik3.R;
import com.roma.akademik3.data.Data;

import java.util.List;

public class Adapter_Matakuliah extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public Adapter_Matakuliah(Activity activity, List<Data> items){
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_mata_kuliah,null);

        TextView Kode_Matkul = (TextView) convertView.findViewById(R.id.Kode_Mata_Kuliah);
        TextView Nama_Matkul = (TextView) convertView.findViewById(R.id.Nama_Mata_Kuliah);
        TextView SKS         = (TextView) convertView.findViewById(R.id.Lay_SKS);

        Data data = items.get(position);

        Kode_Matkul.setText(data.getKMK());
        Nama_Matkul.setText(data.getNMK());
        SKS.setText(data.getSKS());


        return convertView;
    }
}
