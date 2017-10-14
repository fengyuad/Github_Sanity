package Model;


/**
 * Created by EricWang on 10/12/17.
 */

public class Category {
    /**
     * Getter and setter
     */
    public String GetName() {
        return mName;
    }
    public void SetID(Long mID) {
        this.mID = mID;
    }
    public Long GetBudgetID() {
        return mBudgetID;
    }
    public void SetBudgetID(Long mBudgetID) {
        this.mBudgetID = mBudgetID;
    }
    public void SetName(String mName) {
        this.mName = mName;
    }
    public Long GetID() {
        return mID;
    }
    public double GetAmount() {
        return mAmount;
    }
    public void SetAmount(double amount) {
        mAmount = amount;
    }
    public double GetCurrentAmount() {
        return mCurrentAmount;
    }


    /**
     * Member Variable
     */
    private String mName;



    private Long mID;



    private Long mBudgetID;
    private double mAmount; // the upper bound of user setting amount
    private double mCurrentAmount; // current spending amount

    /**
     * Constructor
     */
    public Category(String name, double amount, long budgetID){
        mID = System.currentTimeMillis()/1000;
        mName = name;
        mAmount = amount;
        mCurrentAmount = 0;
        mBudgetID = budgetID;
    }
    public Category(String name, double amount, long id, double currentAmount, long budgetID){
        mID = id;
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
