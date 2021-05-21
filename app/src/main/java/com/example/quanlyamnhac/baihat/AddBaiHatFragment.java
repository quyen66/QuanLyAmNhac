package com.example.quanlyamnhac.baihat;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;

import java.util.ArrayList;

public class AddBaiHatFragment extends Fragment {

    Database database;
    EditText add_mbh, add_tbh, add_nst;
    Spinner add_mns;
    Button btn_add_bh;
    ArrayList array_manhacsi;


    String mns="", final_mns;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        mns = bundle.getString("mns");

        View root = inflater.inflate(R.layout.baihat_fragment_insert, container, false);

        add_mns = root.findViewById(R.id.add_mns);
        add_mbh = root.findViewById(R.id.add_mbh);
        add_tbh = root.findViewById(R.id.add_tbh);
        add_nst = root.findViewById(R.id.add_nst);
        btn_add_bh = root.findViewById(R.id.btn_add_bh);

        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        khoitaoSpinner();
        ArrayAdapter adapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_item, array_manhacsi);
        add_mns.setAdapter(adapter);

        if (mns != null && !mns.equalsIgnoreCase("") && !mns.equalsIgnoreCase(" ")) {
            add_mns.setSelection(vitri(mns));
            final_mns = array_manhacsi.get(vitri(mns)).toString();
            add_mns.setEnabled(false);
        } else {
            add_mns.setEnabled(true);
            add_mns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final_mns = add_mns.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


        btn_add_bh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor mbh = database.GetData("SELECT MaBaiHat FROM BaiHat WHERE '" + add_mbh.getText().toString() + "' = MaBaiHat");
                if (add_mbh.getText().toString()== null) {
                    Toast.makeText(getContext(), "Mã bài hát không được trống", Toast.LENGTH_LONG).show();
                } else if (add_tbh.getText().toString()== null) {
                    Toast.makeText(getContext(), "Tên bài hát không được trống", Toast.LENGTH_LONG).show();
                } else if (add_nst.getText().toString()== null) {
                    Toast.makeText(getContext(), "Năm sáng tác không được trống", Toast.LENGTH_LONG).show();
                } else if (mbh.moveToNext()) {
                    Toast.makeText(getContext(), "Mã bài hát đã tồn tại", Toast.LENGTH_LONG).show();
                } else {

                    database.QueryData("INSERT INTO BaiHat (MaBaiHat,TenBaiHat, NamSangTac, MaNhacSi)" +
                            "VALUES ('" + add_mbh.getText().toString() +
                            "', '" + add_tbh.getText().toString() +
                            "', '" + add_nst.getText().toString() +
                            "', '" + final_mns +
                            "')");

                    Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_LONG).show();

                    add_mbh.setText("");
                    add_tbh.setText("");
                    add_nst.setText("");


                }
            }
        });

        return root;

    }

    private void khoitaoSpinner() {
        array_manhacsi = new ArrayList();
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);
        Cursor dataBaiHat = database.GetData("SELECT * FROM NhacSi");
        array_manhacsi.clear();
        while (dataBaiHat.moveToNext()) {
            String mnsi = dataBaiHat.getString(0);
            array_manhacsi.add(mnsi);
        }
    }

    private int vitri(String mabh){
        for (int i = 0; i < array_manhacsi.size();i++){
            if(array_manhacsi.get(i).equals(mabh)){
                return i;
            }
        }
        return array_manhacsi.size();
    }

}