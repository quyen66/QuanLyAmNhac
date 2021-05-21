package com.example.quanlyamnhac.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.database.BaiHatDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapterBaiHat extends ArrayAdapter<BaiHatDatabase> implements Filterable {
    Context context;
    int resource;
    ArrayList<BaiHatDatabase> data = null;
    ArrayList<BaiHatDatabase> dataFilter;
    bhButtonListener bhListener;

    public interface bhButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setbhButtonListener(bhButtonListener listener) {
        this.bhListener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public class ViewHolder {
        TextView mbh;
        TextView tbh;
        TextView nst;
        Button btn_baihat_next;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(resource, null);

            holder.mbh = (TextView) convertView.findViewById(R.id.bh_mabaihat);
            holder.tbh = (TextView) convertView.findViewById(R.id.bh_tenbaihat);
            holder.nst = (TextView) convertView.findViewById(R.id.bh_namsangtac);
            holder.btn_baihat_next = (Button) convertView.findViewById(R.id.btn_baihat_next);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mbh.setText(data.get(position).getMaBaiHat());
        holder.tbh.setText(data.get(position).getTenBaiHat());
        holder.nst.setText(data.get(position).getNamSangTac());

        holder.btn_baihat_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bhListener != null) {
                    bhListener.onButtonClickListner(position, data.get(position).getMaBaiHat());
                }
            }
        });
        return convertView;
    }

    public CustomAdapterBaiHat(Context context, int resource, ArrayList data) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.dataFilter = new ArrayList<BaiHatDatabase>();
        this.dataFilter.addAll(data);
        this.resource = resource;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(dataFilter);
        } else {
            for (BaiHatDatabase wp : dataFilter) {
                if (wp.getTenBaiHat().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
