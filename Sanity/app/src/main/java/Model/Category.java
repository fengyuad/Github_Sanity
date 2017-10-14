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
    public void SetmID(Long mID) {
        this.mID = mID;
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
    public double GetCurrentAmount() {
        return mCurrentAmount;
    }


    /**
     * Member Variable
     */
    private String mName;



    private Long mID;
    private double mAmount; // the upper bound of user setting amount
    private double mCurrentAmount; // current spending amount

    /**
     * Constructor
     */
    public Category(String name){
        mID = System.currentTimeMillis()/1000;
        mName = name;
        mAmount = 0;
        mCurrentAmount = 0;
    }
    public Category(String name, double amount){
        mID = System.currentTimeMillis()/1000;
        mName = name;
        mAmount = amount;
        mCurrentAmount = 0;
    }
    public Category(String name, double amount, long id, double currentAmount){
        mID = id;
        mName = name;
        mAmount = amount;
        mCurrentAmount = currentAmount;
    }

    /**
     * Public method
     */
    public void SetAmount(double amount) {
        mAmount = amount;
    }
    public void AddCurrentAmount(double amount) {
        mCurrentAmount += amount;
    }


}
