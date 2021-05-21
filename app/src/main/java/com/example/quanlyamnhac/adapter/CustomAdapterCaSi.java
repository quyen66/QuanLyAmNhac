package com.example.quanlyamnhac.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlyamnhac.R;
import com.example.quanlyamnhac.database.CaSiDatabse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapterCaSi extends ArrayAdapter<CaSiDatabse> implements Filterable {
    Context context;
    int resource;
    ArrayList<CaSiDatabse> data = null;
    ArrayList<CaSiDatabse> dataFilter;
    csButtonListener csListener;

    public interface csButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setcsButtonListener(csButtonListener listener) {
        this.csListener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public class ViewHolder {
        TextView mcs;
        TextView tcs;
        ImageView img_cs;
        Button btn_casi_next;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder csholder;
        if (convertView == null) {
            csholder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(resource, null);

            csholder.mcs = (TextView) convertView.findViewById(R.id.cs_macasi);
            csholder.tcs = (TextView) convertView.findViewById(R.id.cs_tencasi);
            csholder.img_cs = (ImageView) convertView.findViewById(R.id.cs_url);
            csholder.btn_casi_next = (Button) convertView.findViewById(R.id.btn_casi_next);
            convertView.setTag(csholder);

        } else {
            csholder = (ViewHolder) convertView.getTag();
        }

        csholder.mcs.setText(data.get(position).getMacasi());
        csholder.tcs.setText(data.get(position).getTencasi());
        Log.d("immmm", "messss: "+data.get(position).getUrl());
        if (data.get(position).getUrl()== null){
            csholder.img_cs.setImageResource(R.drawable.nopic);
        } else {
            Picasso.with(getContext()).load(data.get(position).getUrl()).into(csholder.img_cs);
        }

        csholder.btn_casi_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (csListener != null) {
                    csListener.onButtonClickListner(position, data.get(position).getMacasi());
                }
            }
        });
        return convertView;
    }

    public CustomAdapterCaSi(Context context, int resource, ArrayList data) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.dataFilter = new ArrayList<CaSiDatabse>();
        this.dataFilter.addAll(data);
        this.resource = resource;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(dataFilter);
        } else {
            for (CaSiDatabse wp : dataFilter) {
                if (wp.getTencasi().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
