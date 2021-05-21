package com.example.quanlyamnhac.bieudien;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.fragment.app.Fragment;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import java.util.ArrayList;
import java.util.Calendar;

public class AddBieuDienFragment extends Fragment {

    Database database;

    String mbh = "", mcs = "";

    ArrayList array_mabaihat;
    ArrayList array_macasi;
    Button btnInsert, buttonLoadImage;
    EditText edt_ins_tencasi, edt_ins_diadiem, edt_ins_ngaybd;
    Spinner sp_mabaihat, edt_ins_macasi;
    DatePickerDialog picker;

    String final_mabaihat, final_macasi;

    public AddBieuDienFragment() {
    }

    public AddBieuDienFragment(String a) {
        mbh = a;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        mbh = bundle.getString("mbh");
        mcs = bundle.getString("mcs");

        View root = inflater.inflate(R.layout.bieudien_fragment_insert, container, false);

        btnInsert = root.findViewById(R.id.insert_thongtin);
        edt_ins_tencasi = root.findViewById(R.id.insert_tencasi);
        sp_mabaihat = (Spinner) root.findViewById(R.id.sp_mabaihat);
        edt_ins_diadiem = root.findViewById(R.id.insert_diadiem);
        edt_ins_ngaybd = root.findViewById(R.id.insert_ngaybd);
        edt_ins_macasi = root.findViewById(R.id.insert_macasi);
//        buttonLoadImage = (Button) root.findViewById(R.id.buttonLoadPicture);

        khoitaoBH();
        khoitaoCS();

        ArrayAdapter adapterBH = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_item, array_mabaihat);
        sp_mabaihat.setAdapter(adapterBH);
        ArrayAdapter adapterCS = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_item, array_macasi);
        edt_ins_macasi.setAdapter(adapterCS);

        if (mbh != null && !mbh.equalsIgnoreCase("") && !mbh.equalsIgnoreCase(" ")) {
            sp_mabaihat.setSelection(vitriBH(mbh));
            final_mabaihat = array_mabaihat.get(vitriBH(mbh)).toString();
            sp_mabaihat.setEnabled(false);

            edt_ins_macasi.setEnabled(true);
            edt_ins_macasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final_macasi = edt_ins_macasi.getSelectedItem().toString();
                    Cursor dataBaiHat = database.GetData("SELECT TenCaSi FROM CaSi WHERE MaCaSi = '" + final_macasi + "'");
                    while (dataBaiHat.moveToNext()) {
                        edt_ins_tencasi.setText(dataBaiHat.getString(0));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {

            edt_ins_macasi.setSelection(vitriCS(mcs));
            final_macasi = array_macasi.get(vitriCS(mcs)).toString();
            Cursor dataCaSi = database.GetData("SELECT TenCaSi FROM CaSi WHERE MaCaSi = '" + final_macasi + "'");
            while (dataCaSi.moveToNext()) {
                edt_ins_tencasi.setText(dataCaSi.getString(0));
            }
            edt_ins_macasi.setEnabled(false);

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
        }


//        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });

        edt_ins_ngaybd.setInputType(InputType.TYPE_NULL);
        edt_ins_ngaybd.setOnClickListener(new View.OnClickListener() {
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
                                edt_ins_ngaybd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edt_ins_tencasi.getText().toString();
                String ngay = edt_ins_ngaybd.getText().toString();
                String diadiem = edt_ins_diadiem.getText().toString();
                if (ngay == null) {
                    Toast.makeText(getContext(), "Ngàu biểu diễn không được trống", Toast.LENGTH_LONG).show();
                } else if (diadiem == null) {
                    Toast.makeText(getContext(), "Địa điểm không được trống", Toast.LENGTH_LONG).show();
                } else {
                    database.QueryData("INSERT INTO BieuDien (MaBaiHat, MaCaSi, TenCaSi, NgayBieuDien, DiaDiem) VALUES('" + final_mabaihat + "', '" + final_macasi + "', '" + ten + "', '" + ngay + "', '" + diadiem + "')");
                    Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();

                    edt_ins_ngaybd.setText("");
                    edt_ins_diadiem.setText("");
                }
            }
        });

        return root;
    }


    private void khoitaoBH() {
        array_mabaihat = new ArrayList();
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);
        Cursor dataBaiHat = database.GetData("SELECT * FROM BaiHat");
        array_mabaihat.clear();
        while (dataBaiHat.moveToNext()) {
            String mabaihat = dataBaiHat.getString(0);
            array_mabaihat.add(mabaihat);
        }
    }

    private void khoitaoCS() {
        array_macasi = new ArrayList<>();
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);
        Cursor dataBaiHat = database.GetData("SELECT * FROM CaSi");
        array_macasi.clear();
        while (dataBaiHat.moveToNext()) {
            array_macasi.add(dataBaiHat.getString(0));
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