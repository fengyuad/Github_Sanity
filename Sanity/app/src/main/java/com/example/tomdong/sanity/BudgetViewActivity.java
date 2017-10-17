package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Model.Budget;
import Model.BudgetModel;
import Model.Category;

import Model.BudgetModel;
import Model.Category;

import static android.content.ContentValues.TAG;

/**
 * Created by tomdong on 10/13/17.
 */

public class BudgetViewActivity extends AppCompatActivity implements Button.OnClickListener {
    private ProgressBar BudgetProgress;
    private ListView CateGory_ListView;
    private TextView BudgetPercent;
    private EditText edit_cat;
    private EditText edit_buddget_name;
    private Long budget_id;
    Button editBgtDateButton;
    TextView editBgtDateText;
    SwipeMenuListView lv;
    private int editYear, editMonth, editDay;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetview);
        BudgetProgress = findViewById(R.id.Budget_progress);
        CateGory_ListView = findViewById(R.id.category_listview);
        BudgetPercent = findViewById(R.id.budget_percent);

        id = getIntent().getExtras().getLong("bgtID");

        ArrayList<Category_card> list = new ArrayList<>();

        List<Category> catList = BudgetModel.GetInstance().getCategoriesUnderBudget(id);
        double bgtTotal = 0;
        double bgtCurr = 0;

        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String dueDate = f.format(new Date(BudgetModel.GetInstance().getBudgetById(id).getmDueTime()*1000));

        for (Category c : catList) {
            list.add(new Category_card(c.getmName(), c.getmCurrentAmount(), c.getmAmount()));
            bgtCurr += c.getmCurrentAmount();
            bgtTotal += c.getmAmount();
        }

        int bgtProgress = (int)((bgtCurr/bgtTotal)*100);

        TextView bgtPct = (TextView) findViewById(R.id.budget_percent);
        bgtPct.setText(Integer.toString(bgtProgress) + "%");

        TextView bgtDue = (TextView) findViewById(R.id.Budget_time);
        bgtDue.setText("Due on: " + dueDate);


        CustomCardAdapter adapter = new CustomCardAdapter(this, R.layout.card_layout, list);
        CateGory_ListView.setAdapter(adapter);
        final Calendar c = Calendar.getInstance();
        editDay = c.get(Calendar.DAY_OF_MONTH);
        editMonth = c.get(Calendar.MONTH);
        editYear = c.get(Calendar.YEAR);
        budget_id=getIntent().getLongExtra("bgt_id",0);
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
        edit_cat=promptView.findViewById(R.id.edit_catgory_name);
        edit_buddget_name=promptView.findViewById(R.id.edit_bgt_name);
        lv=promptView.findViewById(R.id.budget_edit_catgoryList);
        // setup a dialog window
       GetCategoriesShows();
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       if(edit_cat.getText().toString().isEmpty()||edit_buddget_name.getText().toString().isEmpty()
                               ||editBgtDateText.getText().toString().isEmpty())
                       {
                           Toast.makeText(BudgetViewActivity.this, "Email sent!!!", Toast.LENGTH_SHORT).show();
                       }
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

    public void GetCategoriesShows()
    {
        Long temp=1508211333309L;
        budget_id=temp;
        BudgetModel bmodel=BudgetModel.GetInstance();
       ArrayList<Category>nlist= (ArrayList<Category>) bmodel.getCategoriesUnderBudget(budget_id);
        ArrayList<String>catNames=new ArrayList<>();
        for(int i=0;i<nlist.size();i++)
        {
            catNames.add(nlist.get(i).getmName());
        }
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(BudgetViewActivity.this,android.R.layout.simple_list_item_1,catNames);
        lv.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                menu.addMenuItem(deleteItem);
            }
        };
        lv.setMenuCreator(creator);
        lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                edit_cat.setText(adapter.getItem(position));
            }
        });

        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.e(TAG, "Delete Position: " + position);
                        adapter.remove(adapter.getItem(position));
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }
}
