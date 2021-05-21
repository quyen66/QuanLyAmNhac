package com.example.quanlyamnhac.baihat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.adapter.CustomAdapterBaiHat.*;
import com.example.quanlyamnhac.adapter.CustomAdapterBaiHat;
import com.example.quanlyamnhac.bieudien.BieuDienActivity;
import com.example.quanlyamnhac.database.BaiHatDatabase;

import java.util.ArrayList;

public class BaiHatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, bhButtonListener {

    Database database;

    ArrayList<BaiHatDatabase> baiHatArrayList;

    ListView lv_tableBaiHat;
    TextView bh_tennhacsi;
    SwipeRefreshLayout swiperefresh_bh;
    SearchView search_bh;

    String mns = "", tenns;
    CustomAdapterBaiHat adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        Bundle bundle = this.getArguments();
        mns = bundle.getString("mns");

        View root = inflater.inflate(R.layout.baihat_fragment, container, false);

        baiHatArrayList = new ArrayList<>();
        getData(mns);
        adapter = new CustomAdapterBaiHat(getContext(), R.layout.baihat_item_list_view, baiHatArrayList);
        adapter.notifyDataSetChanged();

        bh_tennhacsi = root.findViewById(R.id.bh_tennhacsi);
        lv_tableBaiHat = root.findViewById(R.id.lv_tableBaiHat);
        search_bh = root.findViewById(R.id.search_bh);
        swiperefresh_bh = (SwipeRefreshLayout) root.findViewById(R.id.swiperefresh_bh);


        if (mns != null && !mns.equalsIgnoreCase("") && !mns.equalsIgnoreCase(" ")) {

            Cursor tennhacsi = database.GetData("SELECT TenNhacSi FROM NhacSi WHERE '" + mns + "' = MaNhacSi");
            while (tennhacsi.moveToNext()) {
                tenns = tennhacsi.getString(0);
            }
            bh_tennhacsi.setText("Nhạc sĩ: " + tenns);
            bh_tennhacsi.getLayoutParams().height = 250;
        }


        lv_tableBaiHat.setAdapter(adapter);
        adapter.setbhButtonListener(this::onButtonClickListner);

//        database.QueryData("CREATE TABLE IF NOT EXISTS BaiHat(Id INTEGER PRIMARY KEY AUTOINCREMENT, MaBaiHat VARCHAR(200), TenBaiHat VARCHAR(200), NamSangTac VARCHAR(200), MaCaSi VARCHAR(200), foreign key (MaCaSi) references BieuDien)");
//        Cursor dataBaiHat = database.GetData("SELECT MaBaiHat, TenBaiHat, NamSangTac FROM BaiHat WHERE MaCaSi = (SELECT MaCaSi FROM BieuDien WHERE '" + textView.getText().toString() + "' = TenCaSi)");

        setupSearchView();

        swiperefresh_bh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                baiHatArrayList = new ArrayList<>();
                getData(mns);
                adapter = new CustomAdapterBaiHat(getContext(), R.layout.baihat_item_list_view, baiHatArrayList);
                lv_tableBaiHat.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swiperefresh_bh.setRefreshing(false);
            }
        });

        Dialog dialogSelect = new Dialog(getContext());
        dialogSelect.setContentView(R.layout.dialog_select);

        Dialog dialogDelete = new Dialog(getContext());
        dialogDelete.setContentView(R.layout.dialog_confirm_delete);

        Button btnSelectEdit = dialogSelect.findViewById(R.id.btn_select_edit);
        Button btnSelectDelete = dialogSelect.findViewById(R.id.btn_select_delete);
        Button btnNo = dialogDelete.findViewById(R.id.btn_delete_no);
        Button btnYes = dialogDelete.findViewById(R.id.btn_delete_yes);

        lv_tableBaiHat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                dialogSelect.show();
                btnSelectEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSelect.dismiss();
                        UpdateBaiHatFragment nextFrag = new UpdateBaiHatFragment(baiHatArrayList.get(position).getMaBaiHat(),
                                baiHatArrayList.get(position).getTenBaiHat(),
                                baiHatArrayList.get(position).getNamSangTac());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.baihat_fragment, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();


                    }
                });
                btnSelectDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSelect.dismiss();
                        dialogDelete.show();
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogDelete.dismiss();
                            }
                        });
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mabaihat = baiHatArrayList.get(position).getMaBaiHat();
                                database.QueryData("DELETE FROM BaiHat WHERE MaBaiHat = '" + mabaihat + "' ");
                                database.QueryData("DELETE FROM BieuDien WHERE MaBaiHat = '" + mabaihat + "'");
                                getData(mns);
                                Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                dialogDelete.dismiss();
                            }
                        });
                    }
                });
                return true;
            }
        });


        return root;
    }


    private void setupSearchView() {
        search_bh.setIconifiedByDefault(false);
        search_bh.setOnQueryTextListener(this);
        //search_bh.setSubmitButtonEnabled(true);
        search_bh.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void getData(String a) {
        if (a != null && !a.equalsIgnoreCase("") && !a.equalsIgnoreCase(" ")) {

            Cursor dataBaiHat = database.GetData("SELECT * FROM BaiHat WHERE '" + mns + "' = MaNhacSi");
            baiHatArrayList.clear();

            while (dataBaiHat.moveToNext()) {
                String mabaihat = dataBaiHat.getString(0);
                String tenbaihat = dataBaiHat.getString(1);
                String namsangtac = dataBaiHat.getString(2);
                baiHatArrayList.add(new BaiHatDatabase(mabaihat, tenbaihat, namsangtac, mns));
            }

        } else {
            Cursor dataBaiHat = database.GetData("SELECT * FROM BaiHat");
            baiHatArrayList.clear();

            while (dataBaiHat.moveToNext()) {
                String mabaihat = dataBaiHat.getString(0);
                String tenbaihat = dataBaiHat.getString(1);
                String namsangtac = dataBaiHat.getString(2);
                baiHatArrayList.add(new BaiHatDatabase(mabaihat, tenbaihat, namsangtac, mns));
            }
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onButtonClickListner(int position, String value) {
        Intent bieudien = new Intent(getContext(), BieuDienActivity.class);
        bieudien.putExtra("mabaihat", value);
        bieudien.putExtra("macasi", "");
        bieudien.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(bieudien);

    }

}