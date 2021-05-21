package com.example.quanlyamnhac.thongke;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BieuDoFragment extends Fragment {

    Database database;
    BarChart barChart;
    public static BieuDoFragment newInstance() {
        return new BieuDoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        View root =inflater.inflate(R.layout.bieudo_fragment, container, false);;
        barChart = root.findViewById(R.id.barChart);
        Cursor data = database.GetData("select c.MaCaSi, count(b.MaCaSi) from CaSi c, BieuDien b where c.MaCaSi = b.MaCaSi group by c.MaCaSi");
        ArrayList<BarEntry> bar = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int i = 0;
        while (data.moveToNext()){
            bar.add(new BarEntry(i, data.getInt(1)));
            labels.add(data.getString(0));
            i++;
        }
        BarDataSet barDataSet = new BarDataSet(bar, "Số lần ca sĩ biểu diễn bài hát");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(20);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);

        XAxis xAxis = barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(20);
        xAxis.setLabelCount(labels.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Legend l = barChart.getLegend();
        l.setTextSize(20);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setTextSize(15);

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setEnabled(false);

        barChart.animateY(2000);
        barChart.invalidate();

        return root;
    }

}