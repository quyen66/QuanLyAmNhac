package com.example.quanlyamnhac.casi;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.adapter.CustomAdapterCaSi;
import com.example.quanlyamnhac.adapter.CustomAdapterCaSi.*;
import com.example.quanlyamnhac.bieudien.BieuDienActivity;
import com.example.quanlyamnhac.database.CaSiDatabse;

import java.util.ArrayList;

public class CaSiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, csButtonListener {

    Database database;

    ArrayList<CaSiDatabse> casiArrayList;

    ListView lv_tableCaSi;

    SearchView search_cs;
    CustomAdapterCaSi adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        View root = inflater.inflate(R.layout.casi_fragment, container, false);

        casiArrayList = new ArrayList<>();
        getData();
        adapter = new CustomAdapterCaSi(getContext(), R.layout.casi_item_list_view, casiArrayList);
        adapter.notifyDataSetChanged();

        lv_tableCaSi = root.findViewById(R.id.lv_tableCaSi);
        search_cs = root.findViewById(R.id.search_cs);


        lv_tableCaSi.setAdapter(adapter);
        adapter.setcsButtonListener(this::onButtonClickListner);

        setupSearchView();

        Dialog dialogSelect = new Dialog(getContext());
        dialogSelect.setContentView(R.layout.dialog_select);

        Dialog dialogDelete = new Dialog(getContext());
        dialogDelete.setContentView(R.layout.dialog_confirm_delete);

        Button btnSelectEdit = dialogSelect.findViewById(R.id.btn_select_edit);
        Button btnSelectDelete = dialogSelect.findViewById(R.id.btn_select_delete);
        Button btnNo = dialogDelete.findViewById(R.id.btn_delete_no);
        Button btnYes = dialogDelete.findViewById(R.id.btn_delete_yes);

        lv_tableCaSi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                dialogSelect.show();
                btnSelectEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSelect.dismiss();
                        UpdateCaSiFragment nextFrag = new UpdateCaSiFragment(casiArrayList.get(position).getMacasi(),
                                casiArrayList.get(position).getTencasi(),
                                casiArrayList.get(position).getUrl());

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.casi_fragment, nextFrag, "findThisFragment")
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
                                String macasi = casiArrayList.get(position).getMacasi();
                                database.QueryData("DELETE FROM CaSi WHERE MaCaSi = '" + macasi + "' ");
                                database.QueryData("DELETE FROM BieuDien WHERE MaCaSi = '" + macasi + "'");
                                getData();
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
        search_cs.setIconifiedByDefault(false);
        search_cs.setOnQueryTextListener(this);
        //search_bh.setSubmitButtonEnabled(true);
        search_cs.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void getData() {
            Cursor dataCaSi = database.GetData("SELECT * FROM CaSi");
            casiArrayList.clear();
            while (dataCaSi.moveToNext()) {
                String macasi = dataCaSi.getString(0);
                String tencasi = dataCaSi.getString(1);
                String url = dataCaSi.getString(2);
                casiArrayList.add(new CaSiDatabse(macasi, tencasi, url));
            }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onButtonClickListner(int position, String value) {
        Intent bieudien = new Intent(getContext(), BieuDienActivity.class);
        bieudien.putExtra("macasi", value);
        bieudien.putExtra("mabaihat", "");
        bieudien.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(bieudien);
    }

}