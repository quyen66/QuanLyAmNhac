package com.example.quanlyamnhac.nhacsi;


import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.baihat.BaiHatFragment;

import java.util.ArrayList;

public class AddNhacSiFragment extends Fragment {

    Button btnInsert;
    EditText editname, editma;
    Database database;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nhacsi_fragment_insert, container, false);
        View text = inflater.inflate(R.layout.nhacsi_fragment, container, false);
        btnInsert = root.findViewById(R.id.btnInsert);
        editname = root.findViewById(R.id.editname);
        editma = root.findViewById(R.id.editma);

        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editname.getText().toString();
                String ma = editma.getText().toString();
                if (name== null) {
                    Toast.makeText(getContext(), "Vui long nhap ten", Toast.LENGTH_LONG).show();
                }
                if (ma== null) {
                    Toast.makeText(getContext(), "Vui long nhap ma nhac si", Toast.LENGTH_LONG).show();
                } else {

                    database.QueryData("INSERT INTO NhacSi VALUES('" + ma + "', '"+name+"')");
                    Toast.makeText(getContext(), "Đã Thêm", Toast.LENGTH_LONG).show();

                    editname.setText("");
                    editma.setText("");
// back

                }

            }
        });


        return root;
    }
}
