package com.example.quanlyamnhac.bieudien;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.adapter.CustomAdapterBieuDienBaiHat;
import com.example.quanlyamnhac.database.BieuDienDatabase;

import java.util.ArrayList;

public class BieuDienFragment extends Fragment implements SearchView.OnQueryTextListener{

    Database database;
    ListView lvTableBieuDien;
    TextView tv_tennhacsi, txt_bd_m, txt_bd_t;
    ArrayList<BieuDienDatabase> bieuDienArrayList;
    CustomAdapterBieuDienBaiHat adapter;

    SearchView search_bd;

    String mbh = "", tenbh, mcs = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        mbh = bundle.getString("mbh");
        mcs = bundle.getString("mcs");

        View root = inflater.inflate(R.layout.bieudien_fragment, container, false);
        lvTableBieuDien = root.findViewById(R.id.lv_tableBieuDien);
        txt_bd_m = root.findViewById(R.id.txt_bd_m);
        txt_bd_t = root.findViewById(R.id.txt_bd_t);
        search_bd = root.findViewById(R.id.search_bd);

        tv_tennhacsi = root.findViewById(R.id.tv_tennhacsi);

        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        bieuDienArrayList = new ArrayList<>();
        adapter = new CustomAdapterBieuDienBaiHat(getContext(), R.layout.bieudien_item_list_view, bieuDienArrayList);

        if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
            Cursor bh = database.GetData("SELECT TenBaiHat FROM BaiHat WHERE '" + mbh + "' = MaBaiHat");
            while (bh.moveToNext()) {
                tenbh = bh.getString(0);
            }

            tv_tennhacsi.setText("Bài hát: " + tenbh);
            tv_tennhacsi.getLayoutParams().height = 250;

            getDataBieuDienBaiHat(mbh);
        } else if (mcs != null && !mcs.equalsIgnoreCase("") && !mcs.equalsIgnoreCase(" ")) {
            txt_bd_m.setText("Mã bài hát");
            txt_bd_t.setText("Tên bài hát");
            Cursor bh = database.GetData("SELECT TenCaSi FROM CaSi WHERE '" + mcs + "' = MaCaSi");
            while (bh.moveToNext()) {
                tenbh = bh.getString(0);
            }

            tv_tennhacsi.setText("Ca sĩ: " + tenbh);
            tv_tennhacsi.getLayoutParams().height = 250;

            getDataBieuDienCaSi(mcs);
        }

        Dialog dialogSelect = new Dialog(getContext());
        dialogSelect.setContentView(R.layout.dialog_select);

        Dialog dialogDelete = new Dialog(getContext());
        dialogDelete.setContentView(R.layout.dialog_confirm_delete);

        Dialog dialogEdit = new Dialog(getContext());
        dialogEdit.setContentView(R.layout.bieudien_fragment_update);

        Button btnSelectEdit = dialogSelect.findViewById(R.id.btn_select_edit);
        Button btnSelectDelete = dialogSelect.findViewById(R.id.btn_select_delete);
        Button btnNo = dialogDelete.findViewById(R.id.btn_delete_no);
        Button btnYes = dialogDelete.findViewById(R.id.btn_delete_yes);

        lvTableBieuDien.setAdapter(adapter);
        lvTableBieuDien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                dialogSelect.show();
                btnSelectEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogSelect.dismiss();
                        UpdateBieuDienFragment nextFrag = null;

                        if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
                            int type = 1;
                            nextFrag = new UpdateBieuDienFragment(bieuDienArrayList.get(i).getMaCaSi(),
                                    bieuDienArrayList.get(i).getTenCasi(),
                                    bieuDienArrayList.get(i).getNgayBieuDien(),
                                    bieuDienArrayList.get(i).getDiaDiem(),
                                    bieuDienArrayList.get(i).getMaBaiHat(), type);
                        } else if (mcs != null && !mcs.equalsIgnoreCase("") && !mcs.equalsIgnoreCase(" ")) {
                            int type = 2;
                            nextFrag = new UpdateBieuDienFragment(bieuDienArrayList.get(i).getMaBaiHat(),
                                    bieuDienArrayList.get(i).getTenCasi(),
                                    bieuDienArrayList.get(i).getNgayBieuDien(),
                                    bieuDienArrayList.get(i).getDiaDiem(),
                                    bieuDienArrayList.get(i).getMaCaSi(), type);
                        }
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.bieudien_fragment, nextFrag, "findThisFragment")
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
                                BieuDienDatabase db = bieuDienArrayList.get(i);
                                String maCaSi = db.getMaCaSi();
                                String mabaihat = db.getMaBaiHat();
                                String ngbd = db.getNgayBieuDien();
                                String diadiem = db.getDiaDiem();
                                database.QueryData("DELETE FROM BieuDien WHERE MaCaSi = '" + maCaSi + "' AND MaBaiHat = '" + mabaihat + "' AND NgayBieuDien = '"+ngbd+"' AND DiaDiem = '"+diadiem+"'");
                                database.QueryData("DELETE FROM BieuDien WHERE MaCaSi = '" + mabaihat + "' AND MaBaiHat = '" + maCaSi + "' AND NgayBieuDien = '"+ngbd+"' AND DiaDiem = '"+diadiem+"'");
                                if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
                                    getDataBieuDienBaiHat(mbh);
                                } else if (mcs != null && !mcs.equalsIgnoreCase("") && !mcs.equalsIgnoreCase(" ")) {
                                    getDataBieuDienCaSi(mcs);
                                }
                                Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
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
        search_bd.setIconifiedByDefault(false);
        search_bd.setOnQueryTextListener(this);
        //search_bh.setSubmitButtonEnabled(true);
        search_bd.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filterTenCaSi(text);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public void getDataBieuDienBaiHat(String a) {
        Cursor dataBieuDien = database.GetData("SELECT * FROM BieuDien WHERE '" + a + "' = MaBaiHat");
        bieuDienArrayList.clear();
        while (dataBieuDien.moveToNext()) {

            String mabaihat = dataBieuDien.getString(1);
            String macasi = dataBieuDien.getString(2);
            String tencasi = dataBieuDien.getString(3);
            String ngaybieudien = dataBieuDien.getString(4);
            String diadiem = dataBieuDien.getString(5);
            bieuDienArrayList.add(new BieuDienDatabase(mabaihat, macasi, tencasi, ngaybieudien, diadiem));

        }
        adapter.notifyDataSetChanged();
    }

    public void getDataBieuDienCaSi(String a) {
        Cursor dataBieuDien = database.GetData("SELECT * FROM BieuDien WHERE '" + a + "' = MaCaSi");
        bieuDienArrayList.clear();
        while (dataBieuDien.moveToNext()) {

            String mabaihat = dataBieuDien.getString(1);
            String macasi = dataBieuDien.getString(2);
            String tenbaihat = "";
            Cursor casi = database.GetData("SELECT TenBaiHat FROM BaiHat WHERE '" + mabaihat + "' = MaBaiHat");
            while (casi.moveToNext()) {
                tenbaihat = casi.getString(0);
            }
            String ngaybieudien = dataBieuDien.getString(4);
            String diadiem = dataBieuDien.getString(5);
            bieuDienArrayList.add(new BieuDienDatabase(macasi, mabaihat, tenbaihat, ngaybieudien, diadiem));

        }
        adapter.notifyDataSetChanged();
    }

}