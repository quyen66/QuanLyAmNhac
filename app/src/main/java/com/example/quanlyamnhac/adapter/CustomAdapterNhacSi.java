package com.example.quanlyamnhac.adapter;


import android.content.Context;
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
import com.example.quanlyamnhac.database.NhacSiDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapterNhacSi extends ArrayAdapter<NhacSiDatabase> implements Filterable {
    Context context;
    int resource;
    ArrayList<NhacSiDatabase> data = null;
    ArrayList<NhacSiDatabase> dataFilter;
    nsButtonListener nsListener;

    public interface nsButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setnsButtonListener(nsButtonListener listener) {
        this.nsListener = listener;
    }

    public CustomAdapterNhacSi(Context context, int resource, ArrayList data) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.dataFilter = new ArrayList<NhacSiDatabase>();
        this.dataFilter.addAll(data);
        this.resource = resource;
    }

    public class ViewHolder {
        TextView tvID;
        TextView tvName;
        Button btn_nhacsi_next;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(final int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(resource, null);

            viewHolder.tvID = convertView.findViewById(R.id.tv_id);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.btn_nhacsi_next = convertView.findViewById(R.id.btn_nhacsi_next);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvID.setText(data.get(i).getId());
        viewHolder.tvName.setText(data.get(i).getName());

        viewHolder.btn_nhacsi_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nsListener != null) {
                    nsListener.onButtonClickListner(i, data.get(i).getId());
                }
            }
        });

        return  convertView;

    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(dataFilter);
        } else {
            for (NhacSiDatabase wp : dataFilter) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}

