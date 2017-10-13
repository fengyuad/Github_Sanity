package Model;

import java.util.Set;

/**
 * Created by Yifan on 10/12 012.
 */

public class Budget {
    private long mBudgetId = 0;
    private long mDueDate = 0;
    private int mPeriod = 0;
    private double mAmount = 0.0f;
    private Set<Long> mCatIds;

    public Budget(long dueDate, int period, double amount, Set<Long> catIds) {
        mDueDate = dueDate;
        mPeriod = period;
        mAmount = amount;
        mCatIds = catIds;
    }

    /**
     * Update due date
     *
     * @param dueDate a <b>long</b> Unix Time that represents the due date
     */
    void UpdateDueDate(long dueDate) {
        mDueDate = dueDate;
    }

    /**
     * Update budget period
     *
     * @param period an <b>int</b> that represents budget period in days
     */
    void UpdatePeriod(int period) {
        mPeriod = period;
    }

    /**
     * Update budget amount
     *
     * @param amount a <b>double</b> that represents the budget amount
     */
    void UpdateAmount(double amount) {
        mAmount = amount;
    }

    /**
     * Update Categories
     *
     * @param catIds
     */
    void UpdateCatIds(Set<Long> catIds) {
        mCatIds = catIds;
    }

    /**
     * Return this budget's Id
     *
     * @return mBudgetId Budget Id
     */
    long GetId() {
        return mBudgetId;
    }

    long GetDueDate() {
        return mDueDate;
    }

    int GetPeriod() {
        return mPeriod;
    }

    double GetAmount() {
        return mAmount;
    }

    Set<Long> GetCatIds() {
        return mCatIds;
    }
}
