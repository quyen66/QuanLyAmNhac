package com.example.quanlyamnhac.nhacsi;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.MainActivity;
import com.example.quanlyamnhac.R;

public class UpdateNhacSiFragment extends DialogFragment {
    Database database;
    EditText up_tennhacsi;
    Button btn_up_nhacsi;

    String texta, textb;

    public UpdateNhacSiFragment(String a, String b) {
        texta = a;
        textb=b;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.nhacsi_fragment_update, container, false);

        up_tennhacsi = root.findViewById(R.id.up_tennhacsi);
        //thêm mã nhạc sĩ trong xml luôn nha
//        up_manhacsi = root.findViewById(R.id.up_manhacsi);

        btn_up_nhacsi = root.findViewById(R.id.btn_up_nhacsi);

        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        up_tennhacsi.setText(texta);
//        up_manhacsi.setText(textb);

        btn_up_nhacsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.QueryData("UPDATE NhacSi SET TenNhacSi  = '" + up_tennhacsi.getText().toString() + "' where MaNhacSi= '" + textb+"'");
                Toast.makeText(getContext(), "Chỉnh sửa thành công!" , Toast.LENGTH_SHORT).show();

                Intent nhacsi = new Intent(getActivity().getBaseContext(), MainActivity.class);
                startActivity(nhacsi);
            }
        });

        return root;

    }
}

