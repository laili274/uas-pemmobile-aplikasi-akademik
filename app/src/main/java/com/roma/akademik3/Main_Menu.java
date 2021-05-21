package com.roma.akademik3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main_Menu extends AppCompatActivity {
    ImageButton Mahasiswa, Dosen, Matakuliah, About, Logout, Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);

        Mahasiswa   = (ImageButton) findViewById(R.id.Mahasiswa);
        Dosen       = (ImageButton) findViewById(R.id.Dosen);
        Matakuliah  = (ImageButton) findViewById(R.id.Matakuliah);
        About       = (ImageButton) findViewById(R.id.About);
        Exit        = (ImageButton) findViewById(R.id.Exit);
        Logout       = (ImageButton) findViewById(R.id.logout);

        Mahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mhs = new Intent(Main_Menu.this, com.roma.akademik3.Mahasiswa.class);
                startActivity(mhs);
            }
        });

        Dosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsn = new Intent(Main_Menu.this, com.roma.akademik3.Dosen.class);
                startActivity(dsn);
            }
        });

        Matakuliah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent matkul = new Intent(Main_Menu.this, Matakuliah.class);
                startActivity(matkul);
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tentang = new Intent(Main_Menu.this, About.class);
                startActivity(tentang);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(Main_Menu.this, Login.class);
                startActivity(logout);
                finish();
            }
        });
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
