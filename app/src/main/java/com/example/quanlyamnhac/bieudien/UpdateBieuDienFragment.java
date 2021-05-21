package com.example.quanlyamnhac.bieudien;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateBieuDienFragment extends DialogFragment {
    String final_mabaihat, final_macasi;
    Spinner sp_mabaihat, edt_macasi;
    Database database;
    DatePickerDialog picker;
    String macasi, tencasi, ngaybieudien, diadiem, mabaihat;
    int type, id;

    public UpdateBieuDienFragment(String macasi, String tencasi, String ngaybieudien, String diadiem, String mabaihat, int type) {
        this.macasi = macasi;
        this.tencasi = tencasi;
        this.ngaybieudien = ngaybieudien;
        this.diadiem = diadiem;
        this.mabaihat = mabaihat;
        this.type = type;
    }

    ArrayList array_mabaihat, array_macasi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);
        View root = inflater.inflate(R.layout.bieudien_fragment_update, container, false);

        Button btnEdit = root.findViewById(R.id.btn_edit);

        EditText edtTencasi = root.findViewById(R.id.edt_tencasi);
        EditText edtNgaybd = root.findViewById(R.id.edt_ngaybd);
        EditText edtDiadiem = root.findViewById(R.id.edt_diadiem);

        sp_mabaihat = root.findViewById(R.id.sp_edit_mabaihat);
        edt_macasi = root.findViewById(R.id.edt_macasi);

        khoitao();

        Cursor dataCaSi = database.GetData("SELECT ID FROM BieuDien WHERE MaCaSi = '" + macasi + "' AND MaBaiHat = '" + mabaihat + "' AND NgayBieuDien = '" + ngaybieudien + "' AND DiaDiem = '" + diadiem + "'");
        while (dataCaSi.moveToNext()) {
            id = dataCaSi.getInt(0);
        }

        ArrayAdapter adapterBH = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array_mabaihat);
        ArrayAdapter adapterCS = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array_macasi);
        sp_mabaihat.setAdapter(adapterBH);
        edt_macasi.setAdapter(adapterCS);

        if (type == 2) {
            edt_macasi.setSelection(vitriCS(macasi));
            edt_macasi.setEnabled(false);
            final_macasi = macasi;

            edtTencasi.setText(tencasi);
            edtDiadiem.setText(diadiem);
            edtNgaybd.setText(ngaybieudien);

            sp_mabaihat.setSelection(vitriBH(mabaihat));
            sp_mabaihat.setEnabled(true);
            sp_mabaihat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final_mabaihat = sp_mabaihat.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (type == 1) {
            sp_mabaihat.setSelection(vitriBH(mabaihat));
            sp_mabaihat.setEnabled(false);
            final_mabaihat = mabaihat;
            edtDiadiem.setText(diadiem);
            edtNgaybd.setText(ngaybieudien);

            edt_macasi.setSelection(vitriCS(macasi));
            edt_macasi.setEnabled(true);
            edt_macasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final_macasi = edt_macasi.getSelectedItem().toString();
                    Cursor dataBaiHat = database.GetData("SELECT TenCaSi FROM CaSi WHERE MaCaSi = '" + final_macasi + "'");
                    while (dataBaiHat.moveToNext()) {
                        edtTencasi.setText(dataBaiHat.getString(0).toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        edtNgaybd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtNgaybd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTencasi.getText().toString();
                String diadiem = edtDiadiem.getText().toString();
                String ngay = edtNgaybd.getText().toString();

                if (ten == null || diadiem == null || ngay == null) {
                    Toast.makeText(getContext(), "Vui lòng điền thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
                } else {

                    database.QueryData("UPDATE BieuDien SET TenCaSi = '" + ten + "' ,NgayBieuDien = '" + ngay + "' ,DiaDiem = '" + diadiem + "' ,MaCaSi = '" + final_macasi + "',MaBaiHat = '" + final_mabaihat + "' WHERE ID= '" + id + "'");

                    Toast.makeText(getContext(), "Chỉnh sửa thành công!", Toast.LENGTH_SHORT).show();
                    if (type == 2) {
                        Intent baihat = new Intent(getActivity().getBaseContext(), BieuDienActivity.class);
                        baihat.putExtra("mabaihat", "");
                        baihat.putExtra("macasi", macasi);
                        startActivity(baihat);
                    } else {
                        Intent baihat = new Intent(getActivity().getBaseContext(), BieuDienActivity.class);
                        baihat.putExtra("mabaihat", final_mabaihat);
                        baihat.putExtra("macasi", "");
                        startActivity(baihat);
                    }
                }
            }
        });

        return root;
    }


    private void khoitao() {
        array_mabaihat = new ArrayList();
        array_macasi = new ArrayList();
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        Cursor dataBaiHat = database.GetData("SELECT * FROM BaiHat");
        array_mabaihat.clear();
        while (dataBaiHat.moveToNext()) {
            String mabaihat = dataBaiHat.getString(0);
            array_mabaihat.add(mabaihat);
        }

        Cursor dataCaSi = database.GetData("SELECT * FROM CaSi");
        array_macasi.clear();
        while (dataCaSi.moveToNext()) {
            String macasi = dataCaSi.getString(0);
            array_macasi.add(macasi);
        }
    }

    private int vitriBH(String mabh) {
        for (int i = 0; i < array_mabaihat.size(); i++) {
            if (array_mabaihat.get(i).equals(mabh)) {
                return i;
            }
        }
        return array_mabaihat.size();
    }

    private int vitriCS(String macs) {
        for (int i = 0; i < array_macasi.size(); i++) {
            if (array_macasi.get(i).equals(macs)) {
                return i;
            }
        }
        return array_macasi.size();
    }
}
