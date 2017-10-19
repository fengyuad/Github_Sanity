package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.BudgetModel;
import Model.Category;
import Model.CategoryModel;

import static android.content.ContentValues.TAG;

/**
 * Created by tomdong on 10/13/17.
 */

public class BudgetViewActivity extends AppCompatActivity implements Button.OnClickListener {
    Button editBgtDateButton;
    TextView editBgtDateText;
    EditText edit_bgt_period;
    SwipeMenuListView lv;
    EditText cat_add_dialog_Amount;
    TextView cat_add_dialog_catype;
    ListView cat_add_dialog_availableCat;
    private ProgressBar BudgetProgress;
    private ListView CateGory_ListView;
    private TextView BudgetPercent;
    private EditText edit_cat;
    private EditText edit_buddget_name;
    private EditText edit_cat_amount;
    private int editYear, editMonth, editDay;
    private long id;
    private long catid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetview);
        BudgetProgress = findViewById(R.id.Budget_progress);
        CateGory_ListView = findViewById(R.id.category_listview);
        BudgetPercent = findViewById(R.id.budget_percent);

        id = getIntent().getExtras().getLong("bgtID");


        GetDataAndUpdateGUI();


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

        FloatingActionButton add_cat_fab = (FloatingActionButton) findViewById(R.id.add_catTobudget_fab);
        add_cat_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCatToBudgetDialog();
            }
        });

        sendNotification();

    }

    protected void GetDataAndUpdateGUI() {
        ArrayList<Category_card> list = new ArrayList<>();
        List<Category> catList = BudgetModel.GetInstance().getCategoriesUnderBudget(id);
        double bgtTotal = 0;
        double bgtCurr = 0;

        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String dueDate = f.format(new Date(BudgetModel.GetInstance().getBudgetById(id).getmDueTime() * 1000));

        for (Category c : catList) {
            list.add(new Category_card(c.getmName(), c.getmCurrentAmount(), c.getmAmount(), c.getmID()));
            bgtCurr += c.getmCurrentAmount();
            bgtTotal += c.getmAmount();
        }

        int bgtProgress = (int) ((bgtCurr / bgtTotal) * 100);

        TextView bgtPct = (TextView) findViewById(R.id.budget_percent);
        bgtPct.setText(Integer.toString(bgtProgress) + "%");

        TextView bgtDue = (TextView) findViewById(R.id.Budget_time);
        bgtDue.setText("Due on: " + dueDate);

        CustomCardAdapter adapter = new CustomCardAdapter(this, R.layout.card_layout, list);
        CateGory_ListView.setAdapter(adapter);
        sendNotification();
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
        edit_cat = promptView.findViewById(R.id.edit_catgory_name);
        edit_buddget_name = promptView.findViewById(R.id.edit_bgt_name);
        edit_bgt_period = promptView.findViewById(R.id.edit_bgt_period);
        edit_cat_amount = (EditText) promptView.findViewById(R.id.budget_edit_catamount);
        lv = promptView.findViewById(R.id.budget_edit_catgoryList);
        // setup a dialog window
        GetCategoriesShows();
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int idex) {
                        if (edit_cat.getText().toString().isEmpty() || edit_buddget_name.getText().toString().isEmpty()
                                || editBgtDateText.getText().toString().isEmpty()) {
                            Toast.makeText(BudgetViewActivity.this, "please fill out the form!!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        double amount = Double.parseDouble(edit_cat_amount.getText().toString());
                        String budgetName = edit_buddget_name.getText().toString();
                        int period = Integer.parseInt(edit_bgt_period.getText().toString());
                        SimpleDateFormat datetimeFormatter = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        Date dueDate = null;
                        try {
                            dueDate = datetimeFormatter.parse(editBgtDateText.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //CategoryModel.GetInstance().GetCategoryById(catid).setmAmount(amount);
                        //BudgetModel.GetInstance().getBudgetById(id).setmDueTime(dueDate.getTime());
                        //BudgetModel.GetInstance().getBudgetById(id).setmPeriod(period);
                        //BudgetModel.GetInstance().getBudgetById(id).setmName(budgetName);
                        CategoryModel.GetInstance().UpdateAmountAndUpdateDatabase(catid, amount);
                        BudgetModel.GetInstance().UpdateBudget(id, dueDate.getTime(), period);
                        BudgetModel.GetInstance().UpdateBudget(id, budgetName);
                        GetDataAndUpdateGUI();

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

    protected void showAddCatToBudgetDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.cat_addtobudget_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        cat_add_dialog_Amount = (EditText) promptView.findViewById(R.id.cat_addtobudget_dialog_catamount);
        cat_add_dialog_catype = (TextView) promptView.findViewById(R.id.cat_addtobudget_dialog_cat_type);
        cat_add_dialog_availableCat = (ListView) promptView.findViewById(R.id.cat_add_tobudget_dialog_listview);


        GetOthercategoriesShows();
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int idx) {
                        if (cat_add_dialog_Amount.getText().toString().isEmpty() || cat_add_dialog_catype.getText().toString().isEmpty()) {
                            Toast.makeText(BudgetViewActivity.this, "please fill out the form !!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //TODO
                        double amount = Double.parseDouble(cat_add_dialog_Amount.getText().toString());
                        String CatType = cat_add_dialog_catype.getText().toString();
                        BudgetModel.GetInstance().AddACategory(id, catid);
                        CategoryModel.GetInstance().UpdateAmountAndUpdateDatabase(catid, amount);
                        GetDataAndUpdateGUI();
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
                editYear = year;
                editMonth = month + 1;
                editDay = dayOfMonth;
            }
        }, editYear, editMonth, editDay);
        datePickerDialog.show();
    }

    public void GetOthercategoriesShows() {
        CategoryModel catmodel = CategoryModel.GetInstance();
        final ArrayList<Category> nlist = (ArrayList<Category>) catmodel.getAllNoUsedCategory();
        ArrayList<String> catNames = new ArrayList<>();
        for (int i = 0; i < nlist.size(); i++) {
            catNames.add(nlist.get(i).getmName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BudgetViewActivity.this, android.R.layout.simple_list_item_1, catNames);
        cat_add_dialog_availableCat.setAdapter(adapter);
        cat_add_dialog_availableCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long idx) {

                cat_add_dialog_catype.setText(adapter.getItem(position));
                catid = nlist.get(position).getmID();
            }
        });
    }

    public void GetCategoriesShows() {
        BudgetModel bmodel = BudgetModel.GetInstance();
        final ArrayList<Category> nlist = (ArrayList<Category>) bmodel.getCategoriesUnderBudget(id);
        ArrayList<String> catNames = new ArrayList<>();
        for (int i = 0; i < nlist.size(); i++) {
            catNames.add(nlist.get(i).getmName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BudgetViewActivity.this, android.R.layout.simple_list_item_1, catNames);
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
                edit_cat_amount.setText(nlist.get(position).getmAmount() + "");
                edit_buddget_name.setText(BudgetModel.GetInstance().getBudgetById(nlist.get(position).getmBudgetID()).getmName());
                catid = nlist.get(position).getmID();

            }
        });

        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.e(TAG, "Delete Position: " + position);
                        // TODO
                        // Set catID
                        catid = nlist.get(position).getmID();
                        long budgetID = nlist.get(position).getmBudgetID();
                        // 从Budget里删掉CatID
                        BudgetModel.GetInstance().RemoveACategory(budgetID, catid);
                        // 从Cat里删多BudgetID
                        CategoryModel.GetInstance().UpdateBudgetIdAndUpdateDatabase(catid, 0L);
                        // 更新Budget的amount
                        BudgetModel.GetInstance().CalcTotalAmount(budgetID);
                        adapter.remove(adapter.getItem(position));
<<<<<<< HEAD

=======
                        GetDataAndUpdateGUI();
>>>>>>> db7702bfd1aff74e41c7c7df569ede1b5e2f140d
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    public void sendNotification() {
        String text = "";
        for (String s : CategoryModel.GetInstance().getNotification()) {
            text += s;
        }
        for (String s : BudgetModel.GetInstance().getNotification()) {
            text += s;
        }
        if (!text.isEmpty()) {
            int id = 1;
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.app_icon);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            //设置小图标
            mBuilder.setSmallIcon(R.drawable.app_icon);
            //设置大图标
            mBuilder.setLargeIcon(bitmap);
            //设置标题
            mBuilder.setContentTitle("Sanity Reminding");
            //设置通知正文
            mBuilder.setContentText(text);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id++, mBuilder.build());
        }

    }
}
