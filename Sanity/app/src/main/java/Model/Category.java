package Model;


/**
 * Created by EricWang on 10/12/17.
 */

public class Category implements java.io.Serializable{
    /**
     * Getter and Setter
     */
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmBudgetID() {
        return mBudgetID;
    }

    public void setmBudgetID(String mBudgetID) {
        this.mBudgetID = mBudgetID;
    }

    public double getmAmount() {
        return mAmount;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public double getmCurrentAmount() {
        return mCurrentAmount;
    }

    public void setmCurrentAmount(double mCurrentAmount) {
        this.mCurrentAmount = mCurrentAmount;
    }

    /**
     * Member Variable
     */
    private String mName;
    private String mID;
    private String mBudgetID;
    private double mAmount; // the upper bound of user setting amount
    private double mCurrentAmount; // current spending amount

    /**
     * Constructor
     */
    public Category(){
        mName = "";
        mID = "";
        mBudgetID = "";
        mAmount = 0;
        mCurrentAmount = 0;
    }
    public Category(String name, String budgetID){
        mName = name;
        mAmount = 0;
        mCurrentAmount = 0;
        mBudgetID = budgetID;
    }
    public Category(String name, double amount, String budgetID){
        mName = name;
        mAmount = amount;
        mCurrentAmount = 0;
        mBudgetID = budgetID;
    }
    public Category(String name, double amount, double currentAmount, String budgetID){
        mName = name;
        mAmount = amount;
        mCurrentAmount = currentAmount;
        mBudgetID = budgetID;
    }

    /**
     * Public method
     */
    /**
     * Add to current amount
     * @param amount
     */
    public void AddCurrentAmount(double amount) {
        mCurrentAmount += amount;
    }



}
