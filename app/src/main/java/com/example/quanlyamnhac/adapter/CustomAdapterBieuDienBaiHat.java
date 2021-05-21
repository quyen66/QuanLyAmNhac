package com.example.quanlyamnhac.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.database.BaiHatDatabase;
import com.example.quanlyamnhac.database.BieuDienDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapterBieuDienBaiHat extends ArrayAdapter<BieuDienDatabase> {
    Context context;
    int resource;
    ArrayList<BieuDienDatabase> data;
    ArrayList<BieuDienDatabase> dataFilter;

    @Override
    public int getCount() {
        return data.size();
    }

    public class ViewHolder {
        TextView tvMaCaSi;
        TextView tvTenCaSi;
        TextView tvNgayBieuDien;
        TextView tvDiaDiem;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resource, null);


            holder.tvMaCaSi = convertView.findViewById(R.id.tv_macasi);
            holder.tvTenCaSi = convertView.findViewById(R.id.tv_tencasi);
            holder.tvNgayBieuDien = convertView.findViewById(R.id.tv_ngaybieudien);
            holder.tvDiaDiem = convertView.findViewById(R.id.tv_diadiem);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BieuDienDatabase bieuDienDatabase = data.get(i);

        holder.tvMaCaSi.setText(bieuDienDatabase.getMaCaSi());
        holder.tvTenCaSi.setText(bieuDienDatabase.getTenCasi());
        holder.tvNgayBieuDien.setText(bieuDienDatabase.getNgayBieuDien());
        holder.tvDiaDiem.setText(bieuDienDatabase.getDiaDiem());

        return convertView;
    }

    public CustomAdapterBieuDienBaiHat(Context context, int resource, ArrayList data) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.dataFilter = new ArrayList<BieuDienDatabase>();
        this.dataFilter.addAll(data);
        this.resource = resource;
    }

    public void filterTenCaSi(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(dataFilter);
        } else {
            for (BieuDienDatabase wp : dataFilter) {
                if (wp.getTenCasi().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

