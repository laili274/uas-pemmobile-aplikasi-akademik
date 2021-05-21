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

public class Adapter_Dosen extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public Adapter_Dosen(Activity activity, List<Data> items){
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
            convertView = inflater.inflate(R.layout.list_row_dosen,null);

        TextView NIK = (TextView) convertView.findViewById(R.id.L_NIK);
        TextView NIDN = (TextView) convertView.findViewById(R.id.L_NIDN);
        TextView Nama_Dosen = (TextView) convertView.findViewById(R.id.L_Nama_Dosen);
        TextView Alamat_Dosen = (TextView) convertView.findViewById(R.id.L_Alamat_Dosen);

        Data data = items.get(position);

        NIK.setText(data.getNIK());
        NIDN.setText(data.getNIDN());
        Nama_Dosen.setText(data.getNama_Dosen());
        Alamat_Dosen.setText(data.getAlamat_Dosen());

        return convertView;
    }
}
