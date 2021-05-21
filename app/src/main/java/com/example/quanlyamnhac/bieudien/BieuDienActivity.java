package com.example.quanlyamnhac.bieudien;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.quanlyamnhac.MainActivity;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.baihat.AddBaiHatFragment;
import com.example.quanlyamnhac.baihat.BaiHatActivity;
import com.example.quanlyamnhac.baihat.BaiHatFragment;
import com.example.quanlyamnhac.casi.CaSiActivity;
import com.example.quanlyamnhac.thongke.ThongKeActivity;
import com.google.android.material.navigation.NavigationView;

public class BieuDienActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    //    Integer RESULT_LOAD_IMAGE = 1, RESULT_OK;
    Button bieudien_back;
    Button bieudien_fab;

    String mbh = "", mcs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bieu_dien);

        bieudien_back = findViewById(R.id.bieudien_back);
        bieudien_fab = findViewById(R.id.bieudien_fab);

        Intent intent = getIntent();
        mbh = intent.getStringExtra("mabaihat");
        mcs = intent.getStringExtra("macasi");

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

//        if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
        Bundle bundle = new Bundle();
        bundle.putString("mbh", mbh);
        bundle.putString("mcs", mcs);

        BieuDienFragment main = new BieuDienFragment();
        main.setArguments(bundle);

        FragmentManager FragManager = getSupportFragmentManager();
        FragmentTransaction FragTrans = FragManager.beginTransaction();
        FragTrans.replace(R.id.bieudien_fragment, main);
        FragTrans.commit();
//        } else {
//            BieuDienFragment main = new BieuDienFragment(mbh);
//            FragmentManager FragManager = getSupportFragmentManager();
//            FragmentTransaction FragTrans = FragManager.beginTransaction();
//            FragTrans.replace(R.id.bieudien_fragment, main);
//            FragTrans.commit();
//        }

        setEvent();
    }

    private void setEvent() {

        bieudien_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
                Bundle bundle = new Bundle();
                bundle.putString("mbh", mbh);
                bundle.putString("mcs", mcs);
                AddBieuDienFragment main = new AddBieuDienFragment();
                main.setArguments(bundle);

                FragmentManager FragManager = getSupportFragmentManager();
                FragmentTransaction FragTrans = FragManager.beginTransaction();
                FragTrans.replace(R.id.bieudien_fragment, main);
                FragTrans.commit();

//                } else {
//                    AddBieuDienFragment main = new AddBieuDienFragment(mbh);
//                    FragmentManager FragManager = getSupportFragmentManager();
//                    FragmentTransaction FragTrans = FragManager.beginTransaction();
//                    FragTrans.replace(R.id.bieudien_fragment, main);
//                    FragTrans.commit();
//                }
                bieudien_fab.setEnabled(false);
                bieudien_back.setEnabled(true);
            }
        });

        bieudien_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
                Bundle bundle = new Bundle();
                bundle.putString("mbh", mbh);
                bundle.putString("mcs", mcs);
                BieuDienFragment main = new BieuDienFragment();
                main.setArguments(bundle);

                FragmentManager FragManager = getSupportFragmentManager();
                FragmentTransaction FragTrans = FragManager.beginTransaction();
                FragTrans.replace(R.id.bieudien_fragment, main);
                FragTrans.commit();
//                } else {
//                    BieuDienFragment main = new BieuDienFragment(mbh);
//                    FragmentManager FragManager = getSupportFragmentManager();
//                    FragmentTransaction FragTrans = FragManager.beginTransaction();
//                    FragTrans.replace(R.id.bieudien_fragment, main);
//                    FragTrans.commit();
//                }
                bieudien_fab.setEnabled(true);
                bieudien_back.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_baihat:
                Intent baihat = new Intent(BieuDienActivity.this, BaiHatActivity.class);
                startActivity(baihat);
                break;
            case R.id.nav_nhacsi:
                Intent nhacsi = new Intent(BieuDienActivity.this, MainActivity.class);
                startActivity(nhacsi);
                break;
            case R.id.nav_casi:
                Intent casi = new Intent(BieuDienActivity.this, CaSiActivity.class);
                startActivity(casi);
                break;
            case R.id.nav_thongke:
                Intent thongke = new Intent(BieuDienActivity.this, ThongKeActivity.class);
                startActivity(thongke);
                break;
//            case R.id.nav_baocao:
//                Intent baocao = new Intent(BieuDienActivity.this, BaoCaoActivity.class);
//                baocao.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(baocao);
//                break;

        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }
//    }

}