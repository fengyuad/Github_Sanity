package com.example.tomdong.sanity;

/**
 * Created by tomdong on 10/14/17.
 */
public class Category_card {
    private String CatoryType;
    private String BudgetType;
    private double curr;
    private double limit;
    private long id;


    public Category_card(String CatoryType, double curr, double limit, long id) {
        //this.BudgetType = BudgetType;
        this.CatoryType = CatoryType;
        this.curr = curr;
        this.limit = limit;
        this.id = id;
    }

    public String GetBudgetType() {
        return BudgetType;
    }

    public String GetCatoryType() {
        return CatoryType;
    }

    public double GetCatCurr() { return curr; }
    public double GetCatLimit() { return limit; }

    public long GetId() { return id; }

    public void setCatoryType(String CatoryType) {
        this.CatoryType = CatoryType;
    }

    public void setBudgetType(String BudgetType) {
        this.BudgetType = BudgetType;
    }
}