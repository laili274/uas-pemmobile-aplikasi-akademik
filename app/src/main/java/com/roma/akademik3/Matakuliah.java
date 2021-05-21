package com.roma.akademik3;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.roma.akademik3.adapter.Adapter_Matakuliah;
import com.roma.akademik3.data.Data;
import com.roma.akademik3.util.Server;
import com.roma.akademik3.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Matakuliah extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter_Matakuliah adapterMatakuliah;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_KMK, txt_NMK, txt_SKS;
    String KMK, NMK, SKS;

    private static final String TAG = Dosen.class.getSimpleName();
    private static String url_select = Server.URL + "Select_Matakuliah.php";
    public static final String TAG_KMK = "Kode_Mata_Kuliah";
    public static final String TAG_NMK = "Nama_Mata_Kuliah";
    public static final String TAG_SKS = "SKS";
    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matakuliah);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list);
        // untuk mengisi data dari JSON ke dalam adapterMatakuliah
        adapterMatakuliah = new Adapter_Matakuliah(Matakuliah.this, itemList);
        list.setAdapter(adapterMatakuliah);
        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapterMatakuliah.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterMatakuliah.notifyDataSetChanged();
        callVolley();
    }

    private void callVolley() {
        itemList.clear();
        adapterMatakuliah.notifyDataSetChanged();
        swipe.setRefreshing(true);
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Data item = new Data();
                                item.setKMK(obj.getString(TAG_KMK));
                                item.setNMK(obj.getString(TAG_NMK));
                                item.setSKS(obj.getString(TAG_SKS));
                                // menambah item ke array
                                itemList.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifikasi adanya perubahan data pada adapterMatakuliah
                        adapterMatakuliah.notifyDataSetChanged();
                        swipe.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });
        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
}
