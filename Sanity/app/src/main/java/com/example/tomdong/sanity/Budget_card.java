package com.example.tomdong.sanity;

/**
 * Created by tomdong on 10/15/17.
 */

public class Budget_card {
    private String CatoryType;
    private String BudgetType;

    public Budget_card(String CatoryType) {
        //this.BudgetType = BudgetType;
        this.BudgetType = CatoryType;
    }

    public String GetBudgetType() {
        return BudgetType;
    }

    public String GetCatoryType() {
        return CatoryType;
    }

    public void setCatoryType(String CatoryType) {
        this.CatoryType = CatoryType;
    }

    public void setBudgetType(String BudgetType) {
        this.BudgetType = BudgetType;
    }
}