package com.example.quanlyamnhac.baihat;

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

import com.example.quanlyamnhac.MainActivity;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.bieudien.BieuDienActivity;
import com.example.quanlyamnhac.casi.CaSiActivity;
import com.example.quanlyamnhac.thongke.ThongKeActivity;
import com.google.android.material.navigation.NavigationView;

public class BaiHatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    Button baihat_back;
    Button baihat_fab;

    String mns = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_hat);

        baihat_back = findViewById(R.id.baihat_back);
        baihat_fab = findViewById(R.id.baihat_fab);

        Intent intent = getIntent();
        mns = intent.getStringExtra("manhacsi");

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

//        if (mns != null && !mns.equalsIgnoreCase("") && !mns.equalsIgnoreCase(" ")) {


        Bundle bundle = new Bundle();
        bundle.putString("mns", mns);

        BaiHatFragment main = new BaiHatFragment();
        main.setArguments(bundle);
        FragmentManager FragManager = getSupportFragmentManager();
        FragmentTransaction FragTrans = FragManager.beginTransaction();
        FragTrans.replace(R.id.baihat_fragment, main);
        FragTrans.commit();
//
//        } else {
//
//            BaiHatFragment main = new BaiHatFragment(mns);
//            FragmentManager FragManager = getSupportFragmentManager();
//            FragmentTransaction FragTrans = FragManager.beginTransaction();
//            FragTrans.replace(R.id.baihat_fragment, main);
//            FragTrans.commit();
//        }

        setEvent();
    }

    private void setEvent() {
        baihat_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mns != null && !mns.equalsIgnoreCase("") && !mns.equalsIgnoreCase(" ")) {

                Bundle bundle = new Bundle();
                bundle.putString("mns", mns);

                AddBaiHatFragment main = new AddBaiHatFragment();
                main.setArguments(bundle);
                FragmentManager FragManager = getSupportFragmentManager();
                FragmentTransaction FragTrans = FragManager.beginTransaction();
                FragTrans.replace(R.id.baihat_fragment, main);
                FragTrans.commit();
//                } else {
//
//                    AddBaiHatFragment main = new AddBaiHatFragment(mns);
//                    FragmentManager FragManager = getSupportFragmentManager();
//                    FragmentTransaction FragTrans = FragManager.beginTransaction();
//                    FragTrans.replace(R.id.baihat_fragment, main);
//                    FragTrans.commit();
//                }
                baihat_fab.setEnabled(false);
                baihat_back.setEnabled(true);
            }
        });

        baihat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mns != null && !mns.equalsIgnoreCase("") && !mns.equalsIgnoreCase(" ")) {

                Bundle bundle = new Bundle();
                bundle.putString("mns", mns);

                BaiHatFragment main = new BaiHatFragment();
                main.setArguments(bundle);
                FragmentManager FragManager = getSupportFragmentManager();
                FragmentTransaction FragTrans = FragManager.beginTransaction();
                FragTrans.replace(R.id.baihat_fragment, main);
                FragTrans.commit();
//                } else {
//
//                    BaiHatFragment main = new BaiHatFragment(mns);
//                    FragmentManager FragManager = getSupportFragmentManager();
//                    FragmentTransaction FragTrans = FragManager.beginTransaction();
//                    FragTrans.replace(R.id.baihat_fragment, main);
//                    FragTrans.commit();
//                }
                baihat_fab.setEnabled(true);
                baihat_back.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_baihat:
                Intent baihat = new Intent(BaiHatActivity.this, BaiHatActivity.class);
                startActivity(baihat);
                break;
            case R.id.nav_nhacsi:
                Intent nhacsi = new Intent(BaiHatActivity.this, MainActivity.class);
                startActivity(nhacsi);
                break;
            case R.id.nav_casi:
                Intent casi = new Intent(BaiHatActivity.this, CaSiActivity.class);
                startActivity(casi);
                break;
            case R.id.nav_thongke:
                Intent thongke = new Intent(BaiHatActivity.this, ThongKeActivity.class);
                startActivity(thongke);
                break;
//            case R.id.nav_baocao:
//                Intent baocao = new Intent(BaiHatActivity.this, BaoCaoActivity.class);
//                baocao.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(baocao);
//                break;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}