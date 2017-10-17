package com.example.tomdong.sanity;

/**
 * Created by tomdong on 10/15/17.
 */

public class Budget_card {
    private String BudgetType;

    private double limit;
    private double current;

    public Budget_card(String BudgetType, double limit, double current) {
        this.BudgetType = BudgetType;
        this.limit = limit;
        this.current = current;
    }

    public String GetBudgetType() {
        return BudgetType;
    }

    public double GetLimit() {
        return limit;
    }

    public double GetCurrent() {
        return current;
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