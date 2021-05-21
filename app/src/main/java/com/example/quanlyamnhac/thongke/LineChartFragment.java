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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class LineChartFragment extends Fragment {

    LineChart lineChart;
    Database database;
    public static LineChartFragment newInstance() {
        return new LineChartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        View root =inflater.inflate(R.layout.line_chart_fragment, container, false);;
        lineChart = root.findViewById(R.id.lineChart);
        Cursor data = database.GetData("select c.MaCaSi, count(b.MaCaSi) from CaSi c, BieuDien b where c.MaCaSi = b.MaCaSi group by c.MaCaSi");
        ArrayList<Entry> line = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int[] color = new int[]{Color.RED, Color.GRAY, Color.GREEN, Color.YELLOW};
        int i = 1;
        while (data.moveToNext()){
            line.add(new Entry(i, data.getInt(1)));
            labels.add(data.getString(0));
            i++;
        }
        LineDataSet lineDataSet = new LineDataSet(line, "Ca sĩ biểu diễn bài hát");
        LineData lineData = new LineData(lineDataSet);

        lineDataSet.setColors(color);
        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.invalidate();

        return root;
    }

}