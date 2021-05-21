package com.example.quanlyamnhac.thongke;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.MainActivity;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.adapter.CustomAdapterThongKe;
import com.example.quanlyamnhac.baihat.BaiHatActivity;
import com.example.quanlyamnhac.casi.CaSiActivity;
import com.example.quanlyamnhac.database.BieuDienDatabase;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ThongKeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Database database;
    Spinner sp_thang, sp_nam;
    ListView lv_thongke;
    Button btn_bar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

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

        database = new Database(this, "QuanLyAmNhac.sqlite", null, 1);

        lv_thongke = findViewById(R.id.lv_thongke);
        sp_thang = findViewById(R.id.sp_thang);
        sp_nam = findViewById(R.id.sp_nam);

        btn_bar = findViewById(R.id.show_barchart);
//        btn_line = findViewById(R.id.show_linechart);

//        btn_line.setBackgroundColor(Color.GRAY);
        btn_bar.setBackgroundColor(Color.GRAY);
        ThongKeCaSiBieuDienTheoThang();

    }

    private void ThongKeCaSiBieuDienTheoThang() {

        ArrayList<Integer> nam = new ArrayList<Integer>();
        nam.add(2017);
        nam.add(2018);
        nam.add(2019);
        nam.add(2020);
        nam.add(2021);
        ArrayAdapter adapterNam = new ArrayAdapter(this, android.R.layout.simple_spinner_item, nam);
        sp_nam.setAdapter(adapterNam);
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Tháng 1");
        labels.add("Tháng 2");
        labels.add("Tháng 3");
        labels.add("Tháng 4");
        labels.add("Tháng 5");
        labels.add("Tháng 6");
        labels.add("Tháng 7");
        labels.add("Tháng 8");
        labels.add("Tháng 9");
        labels.add("Tháng 10");
        labels.add("Tháng 11");
        labels.add("Tháng 12");
        ArrayAdapter adapterThang = new ArrayAdapter(this, android.R.layout.simple_spinner_item, labels);
        sp_thang.setAdapter(adapterThang);
        sp_thang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        ThongKe("1");
                        break;
                    }
                    case 1: {
                        ThongKe("2");
                        break;
                    }
                    case 2: {
                        ThongKe("3");
                        break;
                    }
                    case 3: {
                        ThongKe("4");
                        break;
                    }
                    case 4: {

                        ThongKe("5");
                        break;
                    }
                    case 5: {

                        ThongKe("6");
                        break;
                    }
                    case 6: {

                        ThongKe("7");
                        break;
                    }
                    case 7: {
                        ThongKe("8");
                        break;
                    }
                    case 8: {
                        ThongKe("9");
                        break;
                    }
                    case 9: {
                        ThongKe("10");
                        break;
                    }
                    case 10: {
                        ThongKe("11");
                        break;
                    }
                    case 11: {
                        ThongKe("12");
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void ThongKe(String month) {
        ArrayList<BieuDienDatabase> bieuDienArrayList = new ArrayList<>();
        Cursor data = database.GetData("select * from BieuDien");
        while (data.moveToNext()) {
            String d = data.getString(4);
            String[] date = d.split("/");
            int namChon = Integer.parseInt(sp_nam.getSelectedItem().toString());
            if (date[1].equals(month) && namChon == Integer.parseInt(date[2])) {
                BieuDienDatabase bieuDienDatabase = new BieuDienDatabase();
                bieuDienDatabase.setMaCaSi(data.getString(2));
                bieuDienDatabase.setMaBaiHat(data.getString(1));
                bieuDienDatabase.setTenCasi(data.getString(3));
                bieuDienDatabase.setDiaDiem(data.getString(5));
                bieuDienDatabase.setNgayBieuDien(data.getString(4));
                bieuDienArrayList.add(bieuDienDatabase);
            }
        }
        CustomAdapterThongKe adapterThang = new CustomAdapterThongKe(getApplication(), R.layout.thongke_item_list_view, bieuDienArrayList);
        lv_thongke.setAdapter(adapterThang);
    }

    public void bieuDoBarChart(View view) {
        btn_bar.setBackgroundColor(Color.RED);
//        btn_line.setBackgroundColor(Color.GRAY);
        BieuDoFragment fragment = new BieuDoFragment();
        FragmentManager FragManager = getSupportFragmentManager();
        FragmentTransaction FragTrans = FragManager.beginTransaction();
        FragTrans.replace(R.id.bieudo_fragment, fragment);
        FragTrans.commit();
    }

//    public void bieuDoLineChart(View view) {
//        btn_bar.setBackgroundColor(Color.GRAY);
//        btn_line.setBackgroundColor(Color.RED);
//        LineChartFragment fragment = new LineChartFragment();
//        FragmentManager FragManager = getSupportFragmentManager();
//        FragmentTransaction FragTrans = FragManager.beginTransaction();
//        FragTrans.replace(R.id.bieudo_fragment, fragment);
//        FragTrans.commit();
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_baihat:
                Intent baihat = new Intent(ThongKeActivity.this, BaiHatActivity.class);
                startActivity(baihat);
                break;
            case R.id.nav_nhacsi:
                Intent nhacsi = new Intent(ThongKeActivity.this, MainActivity.class);
                startActivity(nhacsi);
                break;
            case R.id.nav_casi:
                Intent casi = new Intent(ThongKeActivity.this, CaSiActivity.class);
                startActivity(casi);
                break;
            case R.id.nav_thongke:
                Intent thongke = new Intent(ThongKeActivity.this, ThongKeActivity.class);
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