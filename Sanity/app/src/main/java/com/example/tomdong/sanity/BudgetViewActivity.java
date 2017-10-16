package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tomdong on 10/13/17.
 */

public class BudgetViewActivity extends AppCompatActivity implements Button.OnClickListener {
    private ProgressBar BudgetProgress;
    private ListView CateGory_ListView;
    private TextView BudgetPercent;

    Button editBgtDateButton;
    TextView editBgtDateText;
    private int editYear, editMonth, editDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetview);
        BudgetProgress = findViewById(R.id.Budget_progress);
        CateGory_ListView = findViewById(R.id.category_listview);
        BudgetPercent = findViewById(R.id.budget_percent);

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
        final Calendar c = Calendar.getInstance();
        editDay = c.get(Calendar.DAY_OF_MONTH);
        editMonth = c.get(Calendar.MONTH);
        editYear = c.get(Calendar.YEAR);

        FloatingActionButton bgt_fab = (FloatingActionButton) findViewById(R.id.bgt_edit_fab);
        bgt_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    protected void showEditDialog() {

        // get input_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.budget_edit_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        editBgtDateText = (TextView) promptView.findViewById(R.id.edit_bgt_date);
        editBgtDateButton = (Button) promptView.findViewById(R.id.edit_bgt_date_button);
        editBgtDateButton.setOnClickListener(this);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editBgtDateText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, editYear, editMonth, editDay);
        datePickerDialog.show();
    }
}
