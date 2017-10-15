package com.example.tomdong.sanity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.ListViewCompat;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by tomdong on 10/13/17.
 */

public class BudgetViewActivity extends AppCompatActivity {
    private ProgressBar BudgetProgress;
    private ListView CateGory_ListView;
    private TextView BudgetPercent;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetview);
        BudgetProgress=findViewById(R.id.Budget_progress);
        CateGory_ListView=findViewById(R.id.category_listview);
        BudgetPercent=findViewById(R.id.budget_percent);

        ArrayList<Category_card> list = new ArrayList<>();

        list.add(new Category_card("Parking"));
        list.add(new Category_card("Eating"));
        list.add(new Category_card("Studying"));
        list.add(new Category_card("Working"));
        list.add(new Category_card("Skiing"));
        list.add(new Category_card("Gaming"));
        list.add(new Category_card("Travelling"));
        list.add(new Category_card("pooping"));
        CustomCardAdapter adapter = new CustomCardAdapter(this, R.layout.card_layout, list);
        CateGory_ListView.setAdapter(adapter);

    }


}
