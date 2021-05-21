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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.roma.akademik3.adapter.Adapter_Dosen;
import com.roma.akademik3.data.Data;
import com.roma.akademik3.util.Server;
import com.roma.akademik3.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dosen extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter_Dosen adapterDosen;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    private static final String TAG = Dosen.class.getSimpleName();
    private static String url_select = Server.URL + "Select_Dosen.php";
    public static final String TAG_NIK = "NIK";
    public static final String TAG_NIDN = "NIDN";
    public static final String TAG_NAMA_Dosen = "Nama";
    public static final String TAG_ALAMAT_Dosen = "Alamat";
    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // menghubungkan variablel pada layout dan pada java
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list);

        //nim1 = (TextView) findViewById(R.id.nim);

        // untuk mengisi data dari JSON ke dalam adapterDosen
        adapterDosen = new Adapter_Dosen(Dosen.this, itemList);
        list.setAdapter(adapterDosen);
        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapterDosen.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterDosen.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapterDosen.notifyDataSetChanged();
        swipe.setRefreshing(true);
        // membuat request JSON
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
                                item.setNIK(obj.getString(TAG_NIK));
                                item.setNIDN(obj.getString(TAG_NIDN));
                                item.setNama_Dosen(obj.getString(TAG_NAMA_Dosen));
                                item.setAlamat_Dosen(obj.getString(TAG_ALAMAT_Dosen));
                                // menambah item ke array
                                itemList.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifikasi adanya perubahan data pada adapterDosen
                        adapterDosen.notifyDataSetChanged();
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
