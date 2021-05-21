package com.example.quanlyamnhac;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quanlyamnhac.baihat.BaiHatActivity;
import com.example.quanlyamnhac.bieudien.BieuDienActivity;
import com.example.quanlyamnhac.casi.CaSiActivity;
import com.example.quanlyamnhac.nhacsi.AddNhacSiFragment;
import com.example.quanlyamnhac.nhacsi.NhacSiFragment;
import com.example.quanlyamnhac.thongke.ThongKeActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    Button nhacsi_back, nhacsi_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nhacsi_back = findViewById(R.id.nhacsi_back);
        nhacsi_fab = findViewById(R.id.nhacsi_fab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navmenu);
        navigationView.setItemIconTintList(null);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

        NhacSiFragment main = new NhacSiFragment();
        FragmentManager FragManager = getSupportFragmentManager();
        FragmentTransaction FragTrans = FragManager.beginTransaction();
        FragTrans.replace(R.id.main_fragment, main);
        FragTrans.commit();

        setEvent();
    }

    private void setEvent() {
        nhacsi_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNhacSiFragment fragment = new AddNhacSiFragment();
                FragmentManager FragManager = getSupportFragmentManager();
                FragmentTransaction FragTrans = FragManager.beginTransaction();
                FragTrans.replace(R.id.main_fragment, fragment);
                FragTrans.commit();
                nhacsi_fab.setEnabled(false);
                nhacsi_back.setEnabled(true);
            }
        });

        nhacsi_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NhacSiFragment main = new NhacSiFragment();
                FragmentManager FragManager = getSupportFragmentManager();
                FragmentTransaction FragTrans = FragManager.beginTransaction();
                FragTrans.replace(R.id.main_fragment, main);
                FragTrans.commit();
                nhacsi_fab.setEnabled(true);
                nhacsi_back.setEnabled(true);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_baihat:
                Intent baihat = new Intent(MainActivity.this, BaiHatActivity.class);
                startActivity(baihat);
                break;
            case R.id.nav_nhacsi:
                Intent nhacsi = new Intent(MainActivity.this, MainActivity.class);
                startActivity(nhacsi);
                break;
            case R.id.nav_casi:
                Intent casi = new Intent(MainActivity.this, CaSiActivity.class);
                startActivity(casi);
                break;
            case R.id.nav_thongke:
                Intent thongke = new Intent(MainActivity.this, ThongKeActivity.class);
                startActivity(thongke);
                break;
//            case R.id.nav_baocao:
//                Intent baocao = new Intent(MainActivity.this, BaoCaoActivity.class);
//                baocao.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(baocao);
//                break;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}