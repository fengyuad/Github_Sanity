package Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yifan on 10/12 012.
 */

public class Budget implements Serializable {
    private long mBudgetId = 0;
    private String mName = "";
    private long mDueDate = 0;
    private int mPeriod = 0;
    private double mAmount = 0.0f;
    private List<Long> mCatIds;
    /**
     * Constructor - Initialize a Budget
     *
     * @param dueDate due date
     * @param period  budget period
     * @param amount  budget amount
     * @param catIds  category Ids
     */
    public Budget(String name, long dueDate, int period, double amount, List<Long> catIds) {
        mName = name;
        mBudgetId = System.currentTimeMillis() / 1000;
        mDueDate = dueDate;
        mPeriod = period;
        mAmount = amount;
        mCatIds = catIds;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * Getter
     *
     * @return mBudgetId
     */
    public long getmBudgetId() {
        return mBudgetId;
    }

    /**
     * Setter
     *
     * @param mBudgetId
     */
    /*public void setmBudgetId(long mBudgetId) {
        this.mBudgetId = mBudgetId;
    }*/

    /**
     * Getter
     *
     * @return mDueDate
     */
    public long getmDueDate() {
        return mDueDate;
    }

    /**
     * Setter
     *
     * @param mDueDate
     */
    public void setmDueDate(long mDueDate) {
        this.mDueDate = mDueDate;
    }

    /**
     * Getter
     *
     * @return mPeriod
     */
    public int getmPeriod() {
        return mPeriod;
    }

    /**
     * Setter
     *
     * @param mPeriod
     */
    public void setmPeriod(int mPeriod) {
        this.mPeriod = mPeriod;
    }

    /**
     * Getter
     *
     * @return mAmount
     */
    public double getmAmount() {
        return mAmount;
    }

    /**
     * Setter
     *
     * @param mAmount
     */
    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    /**
     * Getter
     *
     * @return mCatIds
     */
    public List<Long> getmCatIds() {
        return mCatIds;
    }

    /**
     * Setter
     *
     * @param mCatIds
     */
    public void setmCatIds(List<Long> mCatIds) {
        this.mCatIds = mCatIds;
    }
}
