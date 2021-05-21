package com.example.quanlyamnhac.baihat;

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
import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.bieudien.AddBieuDienFragment;
import com.example.quanlyamnhac.bieudien.BieuDienActivity;

public class UpdateBaiHatFragment extends DialogFragment {

    Database database;
    EditText up_mns, up_mbh, up_tbh, up_nst;
    Button btn_up_bh;

    String texta, textb, textc;

    public UpdateBaiHatFragment(String a, String b, String c) {
        texta = a;
        textb = b;
        textc = c;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.baihat_fragment_update, container, false);


        up_mns = root.findViewById(R.id.up_mns);
        up_mbh = root.findViewById(R.id.up_mbh);
        up_tbh = root.findViewById(R.id.up_tbh);
        up_nst = root.findViewById(R.id.up_nst);

        btn_up_bh = root.findViewById(R.id.btn_up_bh);

        up_mbh.setText(texta);
        up_tbh.setText(textb);
        up_nst.setText(textc);


        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        Cursor manhacsi = database.GetData("SELECT MaNhacSi FROM BaiHat WHERE MaBaiHat = '" + up_mbh.getText().toString() + "'");
        while (manhacsi.moveToNext()) {
            up_mns.setText(manhacsi.getString(0));
        }


        btn_up_bh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.QueryData("UPDATE BaiHat SET " +
                        " TenBaiHat = '" + up_tbh.getText().toString() + "'" +
                        ", NamSangTac = '" + up_nst.getText().toString() + "'" +
                        " WHERE MaBaiHat = '" + up_mbh.getText().toString() + "'");

                Toast.makeText(getContext(), "Sửa thành công", Toast.LENGTH_LONG).show();

                Intent baihat = new Intent(getActivity().getBaseContext(), BaiHatActivity.class);
                baihat.putExtra("manhacsi", up_mns.getText().toString());
                startActivity(baihat);

            }

        });

        return root;

    }

}