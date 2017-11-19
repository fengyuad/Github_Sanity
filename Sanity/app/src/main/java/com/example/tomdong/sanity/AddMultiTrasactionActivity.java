package com.example.tomdong.sanity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Budget;
import Model.BudgetModel;
import Model.Category;
import Model.CategoryModel;

/**
 * Created by tomdong on 11/18/17.
 */

public class AddMultiTrasactionActivity extends AppCompatActivity {

     EditText mNumberOfTrans;
     ListView mlistView;
    Button mCompleteButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.form_multiple_transactions);
        mNumberOfTrans=findViewById(R.id.edit_text_multi_trans);
        mlistView=findViewById(R.id.list_view_multi_trans);
       // mCompleteButton=findViewById(R.id.button_mutiTrans);

        //list of addtransaction Card

        //adapter
        //listview.setAdapter
        Map<Long, Budget> bgtMap = BudgetModel.GetInstance().GetBudgetMap();
        Map<Long, Category> catMap = CategoryModel.GetInstance().mIDToCategory;
        final Map<String, Long> bgtNameIdMap = new HashMap<>();
        final Map<String, Long> catNameIdMap = new HashMap<>();
        final ArrayList<String> bgts = new ArrayList<>();
        final ArrayList<String> cats = new ArrayList<>();

        for (Map.Entry<Long, Budget> entry : bgtMap.entrySet()) {
            Long bgtId = entry.getKey();
            Budget bgt = entry.getValue();
            bgtNameIdMap.put(bgt.getmName(), bgtId);
            bgts.add(bgt.getmName());
        }

        for (Map.Entry<Long, Category> entry : catMap.entrySet()) {
            Long catId = entry.getKey();
            Category cat = entry.getValue();
            catNameIdMap.put(cat.getmName(), catId);
        }

       // MutiTransAdapter adapter=new MutiTransAdapter(this,R.layout.muti_trans_list,listTrans,bgtNameIdMap,catNameIdMap);
       // mlistView.setAdapter(adapter);
      //  ArrayList<String>listTrans=new ArrayList<>();
        mNumberOfTrans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer amount=0;
                if(!s.toString().isEmpty()) {
                    amount = Integer.parseInt(s.toString());
                }

                ArrayList<AddTransactionCard>listTrans=new ArrayList<>();
                for(int i=0;i< amount;i++)
                {
                    listTrans.add(new AddTransactionCard(bgts,cats));
                }
                MutiTransAdapter adapter=new MutiTransAdapter(AddMultiTrasactionActivity.this,R.layout.muti_trans_list,listTrans,bgtNameIdMap,catNameIdMap);
                mlistView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



       // MutiTransAdapter adapter=new MutiTransAdapter(this,R.layout.muti_trans_list,listTrans,bgtNameIdMap,catNameIdMap);
        //ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,listTrans);

    }
}
