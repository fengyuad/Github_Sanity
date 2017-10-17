package com.example.tomdong.sanity;

/**
 * Created by tomdong on 10/15/17.
 */

public class Budget_card {
    private String CatType;
    private String BudgetType;

    private double limit;
    private double current;

    public Budget_card(String BudgetType, String CatType, double limit, double current) {
        this.BudgetType = BudgetType;
        this.BudgetType = CatType;
        this.limit = limit;
        this.current = current;
    }

    public String GetCatType() {
        return CatType;
    }

    public String GetBudgetType() {
        return BudgetType;
    }


    public String GetCatoryType() {
        return CatType;
    }

    public void setCatoryType(String CatoryType) {
        this.CatType = CatoryType;
    }


    public double GetLimit() {
        return limit;
    }


    public double GetCurrent() {
        return current;
    }

    public void setCatType(String CatType) {
        this.CatType = CatType;
    }

    public void setBudgetType(String BudgetType) {
        this.BudgetType = BudgetType;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public void setCurrent(double current) {
        this.current = current;
    }
}