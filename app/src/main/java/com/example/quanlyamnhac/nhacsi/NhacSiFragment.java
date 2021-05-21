package com.example.quanlyamnhac.nhacsi;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.MainActivity;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.adapter.CustomAdapterNhacSi.nsButtonListener;
import com.example.quanlyamnhac.adapter.CustomAdapterNhacSi;
import com.example.quanlyamnhac.baihat.BaiHatActivity;
import com.example.quanlyamnhac.database.NhacSiDatabase;

import java.util.ArrayList;

public class NhacSiFragment extends Fragment implements
        nsButtonListener,SearchView.OnQueryTextListener {

    ArrayList<NhacSiDatabase> MusicianArrayList;
    Database database;
    CustomAdapterNhacSi adapter;
    SearchView search_ns;
    ListView lv_tablemusician;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS NhacSi(MaNhacSi VARCHAR(200) PRIMARY KEY NOT NULL, TenNhacSi VARCHAR(200) NOT NULL)");
        database.QueryData("CREATE TABLE IF NOT EXISTS BaiHat(MaBaiHat VARCHAR(200) PRIMARY KEY NOT NULL, TenBaiHat VARCHAR(200) NOT NULL, NamSangTac VARCHAR(200) NOT NULL, MaNhacSi VARCHAR(200) NOT NULL, FOREIGN KEY (MaNhacSi) REFERENCES NhacSi(MaNhacSi))");
        database.QueryData("CREATE TABLE IF NOT EXISTS BieuDien(ID INTEGER PRIMARY KEY AUTOINCREMENT, MaBaiHat VARCHAR(200) NOT NULL, MaCaSi VARCHAR(200) NOT NULL, " +
                                                            "TenCaSi VARCHAR(200) NOT NULL, NgayBieuDien VARCHAR(200) NOT NULL, DiaDiem VARCHAR(200) NOT NULL, " +
                                                            "FOREIGN KEY (MaBaiHat) REFERENCES BaiHat(MaBaiHat), FOREIGN KEY (MaCaSi) REFERENCES CaSi(MaCaSi))");
        database.QueryData("CREATE TABLE IF NOT EXISTS CaSi(MaCaSi VARCHAR(200) PRIMARY KEY NOT NULL, TenCaSi VARCHAR(200) NOT NULL, URL VARCHAR(200) NULL)");

        View root = inflater.inflate(R.layout.nhacsi_fragment, container, false);
        lv_tablemusician = root.findViewById(R.id.lv_tablemusician);
        search_ns = root.findViewById(R.id.search_ns);

        MusicianArrayList = new ArrayList<>();
        getData();
        adapter = new CustomAdapterNhacSi(getContext(), R.layout.nhacsi_item_list_view, MusicianArrayList);

        lv_tablemusician.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Dialog dialogSelect = new Dialog(getContext());
        dialogSelect.setContentView(R.layout.dialog_select);

        Dialog dialogDelete = new Dialog(getContext());
        dialogDelete.setContentView(R.layout.dialog_confirm_delete);

        Button btnSelectEdit = dialogSelect.findViewById(R.id.btn_select_edit);
        Button btnSelectDelete = dialogSelect.findViewById(R.id.btn_select_delete);
        Button btnNo = dialogDelete.findViewById(R.id.btn_delete_no);
        Button btnYes = dialogDelete.findViewById(R.id.btn_delete_yes);


        setupSearchView();

        lv_tablemusician.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                dialogSelect.show();
                btnSelectEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSelect.dismiss();

                        UpdateNhacSiFragment nextFrag = new UpdateNhacSiFragment(MusicianArrayList.get(position).getName(),
                                MusicianArrayList.get(position).getId());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, nextFrag, "findThisFragment")
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
                                database.QueryData("DELETE FROM NhacSi WHERE MaNhacSi = '" + MusicianArrayList.get(position).getId() + "'");
                                database.QueryData("DELETE FROM BaiHat WHERE MaNhacSi = '" + MusicianArrayList.get(position).getId() + "'");
                                Cursor bh = database.GetData("SELECT MaBaiHat FROM BaiHat WHERE MaNhacSi = '" + MusicianArrayList.get(position).getId() + "'");
                                while (bh.moveToNext()) {
                                    String mabh = bh.getString(0);
                                    database.QueryData("DELETE FROM BieuDien WHERE MaBaiHat = '" + mabh + "'");
                                }
                                Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                getData();
                                adapter.notifyDataSetChanged();
                                dialogDelete.dismiss();
                            }
                        });
                    }
                });
                return true;
            }
        });

        adapter.setnsButtonListener(this::onButtonClickListner);

        return root;
    }

    private void setupSearchView() {
        search_ns.setIconifiedByDefault(false);
        search_ns.setOnQueryTextListener(this);
        search_ns.setSubmitButtonEnabled(true);
        search_ns.setQueryHint("Search Here");
    }

    public void getData() {
        Cursor data = database.GetData("SELECT * FROM NhacSi");
        MusicianArrayList.clear();
        while (data.moveToNext()) {
            String id = data.getString(0);
            String name = data.getString(1);
            MusicianArrayList.add(new NhacSiDatabase(id, name));
        }
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        Intent baihat = new Intent(getContext(), BaiHatActivity.class);
        baihat.putExtra("manhacsi", value);
        baihat.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(baihat);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}