package Model;

import android.support.v4.content.res.TypedArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricWang on 11/19/17.
 */

public class SharedBudget {
    // member variable
    private long mBudgetID = 0;
    private String mName = "";
    private long mDueTime = 0;
    private int mPeriod = 0;
    private double mAmountLimit = 0.0;
    private double mCurrentAmount = 0.0;
    private List<Long> mSharedUserIDs = new ArrayList<>();
    /**
       Getter and Setter
     */
    public long getmBudgetID() {
        return mBudgetID;
    }

    public void setmBudgetID(long mBudgetID) {
        this.mBudgetID = mBudgetID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getmDueTime() {
        return mDueTime;
    }

    public void setmDueTime(long mDueTime) {
        this.mDueTime = mDueTime;
    }

    public int getmPeriod() {
        return mPeriod;
    }

    public void setmPeriod(int mPeriod) {
        this.mPeriod = mPeriod;
    }

    public double getmAmountLimit() {
        return mAmountLimit;
    }

    public void setmAmountLimit(double mAmountLimit) {
        this.mAmountLimit = mAmountLimit;
    }

    public double getmTotalAmount() {
        return mCurrentAmount;
    }

    public void setmTotalAmount(double mTotalAmount) {
        this.mCurrentAmount = mTotalAmount;
    }

    public List<Long> getmSharedUserIDs() {
        return mSharedUserIDs;
    }

    public void setmSharedUserIDs(List<Long> mSharedUserIDs) {
        this.mSharedUserIDs = mSharedUserIDs;
    }

    /**
     *  Start of the other functions
     *
     */

    public void AddAmount(double amount){
        this.mCurrentAmount += amount;
    }

    public void AddSharedUser(long userId){
        this.mSharedUserIDs.add(userId);
    }

}
