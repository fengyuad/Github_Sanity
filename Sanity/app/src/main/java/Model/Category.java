package Model;

/**
 * Created by EricWang on 10/12/17.
 */

public class Category {
    // getter and setter
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    // member variable
    private String mName;
    private double mAmount; // the upper bound of user setting amount
    private double mCurrentAmount; // current spending amount

    // constructor
    public Category(String name){
        mName = name;
        mAmount = 0;
        mCurrentAmount = 0;
    }
    public Category(String name, double amount){
        mName = name;
        mAmount = amount;
        mCurrentAmount = 0;
    }

    // public methods
    void SetmAmount(double amount){
        mAmount = amount;
    }
    void DeductAmount(double amount){
        mCurrentAmount += amount;
    }




}
