package com.roma.akademik3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.roma.akademik3.data.Data;
import com.roma.akademik3.adapter.Adapter_Mahasiswa;
import com.roma.akademik3.util.Server;
import com.roma.akademik3.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mahasiswa extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter_Mahasiswa adapterMahasiswa;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_nim, txt_nama, txt_alamat;
    String nim, nama, alamat;

    private static final String TAG = Mahasiswa.class.getSimpleName();
    private static String url_select = Server.URL + "Select_Mahasiswa.php";
    private static String url_insert = Server.URL + "Insert.php";
    private static String url_edit = Server.URL + "Edit.php";
    private static String url_update = Server.URL + "Update.php";
    private static String url_delete = Server.URL + "Delete.php";
    public static final String TAG_NIM = "NIM";
    public static final String TAG_NAMA = "Nama";
    public static final String TAG_ALAMAT = "Alamat";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // menghubungkan variablel pada layout dan pada java
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list);
        // untuk mengisi data dari JSON ke dalam adapterMahasiswa
        adapterMahasiswa = new Adapter_Mahasiswa(Mahasiswa.this, itemList);
        list.setAdapter(adapterMahasiswa);
        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapterMahasiswa.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
        // fungsi floating action button memanggil form biodata
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "SIMPAN");
            }
        });
        // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String nimx = itemList.get(position).getNim();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(Mahasiswa.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(nimx);
                                break;
                            case 1:
                                delete(nimx);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterMahasiswa.notifyDataSetChanged();
        callVolley();
    }

    // untuk mengosongi edittext pada form
    private void kosong() {
        txt_nim.setText(null);
        txt_nama.setText(null);
        txt_alamat.setText(null);
    }


    // untuk menampilkan dialog from biodata
    private void DialogForm(String nimx, String namax, String alamatx, String button) {
        dialog = new AlertDialog.Builder(Mahasiswa.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_mahasiswa, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Biodata");
        txt_nim = (EditText) dialogView.findViewById(R.id.txt_nim);
        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama);
        txt_alamat = (EditText) dialogView.findViewById(R.id.txt_alamat);
        if (!nimx.isEmpty()) {
            txt_nim.setText(nimx);
            txt_nama.setText(namax);
            txt_alamat.setText(alamatx);
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nim = txt_nim.getText().toString();
                nama = txt_nama.getText().toString();
                alamat = txt_alamat.getText().toString();
                simpan_update();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });
        dialog.show();
    }


    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapterMahasiswa.notifyDataSetChanged();
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
                                item.setNim(obj.getString(TAG_NIM));
                                item.setNama(obj.getString(TAG_NAMA));
                                item.setAlamat(obj.getString(TAG_ALAMAT));
                                // menambah item ke array
                                itemList.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifikasi adanya perubahan data pada adapterMahasiswa
                        adapterMahasiswa.notifyDataSetChanged();
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

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (nim.isEmpty()) {
            url = url_insert;
        } else {
            url = url_update;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", jObj.toString());
                                callVolley();
                                kosong();
                                Toast.makeText(Mahasiswa.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                                adapterMahasiswa.notifyDataSetChanged();
                            } else {
                                Toast.makeText(Mahasiswa.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Mahasiswa.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (nim.isEmpty()) {
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                } else {
                    params.put("nim", nim);
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get edit data
    private void edit(final String nimx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("get edit data", jObj.toString());
                                String nimx = jObj.getString(TAG_NIM);
                                String namax = jObj.getString(TAG_NAMA);
                                String alamatx = jObj.getString(TAG_ALAMAT);
                                DialogForm(nimx, namax, alamatx, "UPDATE");
                                adapterMahasiswa.notifyDataSetChanged();
                            } else {
                                Toast.makeText(Mahasiswa.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Mahasiswa.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nim", nimx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk menghapus
    private void delete(final String nimx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("delete", jObj.toString());
                                callVolley();
                                Toast.makeText(Mahasiswa.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                                adapterMahasiswa.notifyDataSetChanged();
                            } else {
                                Toast.makeText(Mahasiswa.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(Mahasiswa.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nim", nimx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}
