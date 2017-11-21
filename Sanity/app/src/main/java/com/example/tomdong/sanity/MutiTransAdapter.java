package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Budget;
import Model.BudgetModel;
import Model.Category;
import Model.CategoryModel;
import Model.Transaction;
import Model.TransactionModel;

/**
 * Created by tomdong on 11/18/17.
 */

public class MutiTransAdapter extends ArrayAdapter<AddTransactionCard> {

    private static final String TAG = "MutiTransAdapter";
    private Context mContext;
    private int mResource;
    private int mPosition = -1;
    private int transYear, transMonth, transDay;
    ArrayList<AddTransactionCard>mlist;
    Map<String, Long> bgtNameIdMap;
    Map<String,Long> catNameIdMap;
    public MutiTransAdapter(Context context, int resource,ArrayList<AddTransactionCard> objects,Map<String, Long> bgtNameIdMap,Map<String, Long> catNameIdMap) {
        super(context, resource,objects);
        mContext=context;
        mResource=resource;
        mlist=objects;
        this.bgtNameIdMap=bgtNameIdMap;
        this.catNameIdMap=catNameIdMap;
        final Calendar c = Calendar.getInstance();
        transDay = c.get(Calendar.DAY_OF_MONTH);
        transMonth = c.get(Calendar.MONTH);
        transYear = c.get(Calendar.YEAR);
    }



    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        private Spinner catSpinner;
         private Spinner bgtSpinner;
        private TextView transDateText;
        private EditText transNote;
        private EditText transAmount;
        private FloatingActionButton scan;
        private Button transDateButton;
        private ProgressBar progressBar;
        private Button Add;
        private Switch AutoSwich;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       final MutiTransAdapter.ViewHolder holder;
        holder= new MutiTransAdapter.ViewHolder();
        mPosition=position;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext()).setView(convertView);

        holder.transAmount = (EditText) convertView.findViewById(R.id.multi_trans_amt);
        holder.transNote = (EditText) convertView.findViewById((R.id.multi_trans_note));
        holder.transDateButton = (Button) convertView.findViewById(R.id.multi_trans_date_button);
        holder.transDateText = (TextView) convertView.findViewById(R.id.multi_trans_date_text);
        //holder.transDateText.setText(transYear + "-" + (transMonth + 1) + "-" + transDay);
        holder.transDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        holder.transDateText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        transYear = year;
                        transMonth = month;
                        transDay = dayOfMonth;
                    }
                }, transYear, transMonth, transDay);
                datePickerDialog.show();
            }
        });
        holder. bgtSpinner = (Spinner) convertView.findViewById(R.id.multi_bgt_spinner);
        holder. catSpinner = (Spinner) convertView.findViewById(R.id.multi_cat_spinner);
        //holder. scan=(FloatingActionButton)convertView.findViewById(R.id.multi_fab_scan);
        //holder.progressBar=(ProgressBar)convertView.findViewById(R.id.multi_progressbar_recept_scanning);
        holder.Add=(Button)convertView.findViewById(R.id.button_mutiTrans_Add);
        holder.AutoSwich=(Switch)convertView.findViewById(R.id.multi_auto_switch);
      //  holder. progressBar.setVisibility(View.GONE);
//        holder.scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //dispatchTakePictureIntent();
//            }
//        });

        ArrayAdapter<String> bgtAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,mlist.get(position).Budgets);

        final ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mlist.get(position).Categories);

        holder.bgtSpinner.setAdapter(bgtAdapter);
        holder.catSpinner.setAdapter(catAdapter);

        holder.bgtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mlist.get(mPosition).Categories.clear();
                Long selectedBgtID = bgtNameIdMap.get(holder.bgtSpinner.getSelectedItem());
                List<Category> cList = BudgetModel.GetInstance().getCategoriesUnderBudget(selectedBgtID);
                for (Category c : cList) {
                    mlist.get(mPosition).Categories.add(c.getmName());
                    catAdapter.notifyDataSetChanged();
                }
                catAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        holder.Add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (holder.transAmount.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Amount cannot be empty! Please Try Again", Toast.LENGTH_SHORT).show();
                }
                else if(mlist.get(position).Categories.size()==0)
                {
                    Toast.makeText(getContext(), "Please add a category to Budget first", Toast.LENGTH_SHORT).show();
                }else {
                    TransactionModel.GetInstance().addTransaction(
                            new Transaction(Double.parseDouble(holder.transAmount.getText().toString()),
                                    catNameIdMap.get(holder.catSpinner.getSelectedItem()).longValue(),
                                    holder.transNote.getText().toString(),
                                    transYear,
                                    transMonth,
                                    transDay,
                                    holder.AutoSwich.isChecked()));
                    String trans = Double.parseDouble(holder.transAmount.getText().toString()) + " " +
                            catNameIdMap.get(holder.catSpinner.getSelectedItem()).longValue() + " " +
                            holder.transNote.getText().toString() + " " +
                            transYear + " " +
                            transMonth + " " +
                            transDay;
                    Toast.makeText(getContext(), "Add Transaction: " + trans, Toast.LENGTH_SHORT).show();
                    holder.Add.setEnabled(false);
                }

            }
        });


        return convertView;

    }

}
