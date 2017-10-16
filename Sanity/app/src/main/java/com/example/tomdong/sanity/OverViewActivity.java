package com.example.tomdong.sanity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by tomdong on 10/12/17.
 */

public class OverViewActivity extends AppCompatActivity{

    PieChart pieChart;
    ListView Budget_ListView;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        pieChart=(PieChart)findViewById(R.id.overview_pie);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);
        //pieChart.getDescription().setText("Budgets OverView");
        ArrayList<PieEntry> yvalues= new ArrayList<>();
        yvalues.add(new PieEntry(34f,"PartyA"));
        yvalues.add(new PieEntry(23f,"USA"));
        yvalues.add(new PieEntry(14f,"China"));
        yvalues.add(new PieEntry(35f,"Japan"));
        yvalues.add(new PieEntry(23f,"Russia"));

        PieDataSet dataSet= new PieDataSet(yvalues,"Counntries");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data=new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);
        pieChart.animateY(1000);

           pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
               @Override
               public void onValueSelected(Entry e, Highlight h) {
                   Log.i("VAL SELECTED",
                           "Value: " + ((PieEntry)e).getLabel() + ", index: " + h.getX()
                                   + ", DataSet index: " + h.getDataSetIndex());
                   startActivity(new Intent(getApplicationContext(),BudgetViewActivity.class));
               }

               @Override
               public void onNothingSelected() {

               }
           });
//        Budget_ListView=findViewById(R.id.Budget_listview);
//        ArrayList<Category_card> list = new ArrayList<>();
//        list.add(new Category_card("Parking"));
//        list.add(new Category_card("Eating"));
//        list.add(new Category_card("Studying"));
//        list.add(new Category_card("Working"));
//        list.add(new Category_card("Skiing"));
//        list.add(new Category_card("Gaming"));
//        list.add(new Category_card("Travelling"));
//        list.add(new Category_card("pooping"));
//        CustomCardAdapter adapter = new CustomCardAdapter(this, R.layout.card_layout, list);
//        Budget_ListView.setAdapter(adapter);

    }




}
