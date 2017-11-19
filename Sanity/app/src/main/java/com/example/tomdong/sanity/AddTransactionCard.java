package com.example.tomdong.sanity;

import android.support.design.widget.FloatingActionButton;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by tomdong on 11/18/17.
 */

public class AddTransactionCard {
    public ArrayList<String>Budgets;
    public ArrayList<String>Categories;
    public int amount;

 public AddTransactionCard(ArrayList<String>bgt,ArrayList<String>Cat)
 {
     Budgets=bgt;
     Categories=Cat;
 }
}
