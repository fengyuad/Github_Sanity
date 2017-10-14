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

    /**
     * Constructor - Initialize a Budget
     *
     * @param dueDate due date
     * @param period  budget period
     * @param amount  budget amount
     * @param catIds  category Ids
     */
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
     * @param catIds a Set of <b>long</b> category IDs
     * @see Set
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

    /**
     * Get due date
     *
     * @return <b>long</b> mDueDate
     */
    long GetDueDate() {
        return mDueDate;
    }

    /**
     * Get budget period
     *
     * @return <b>int</b> mPeriod
     */

    int GetPeriod() {
        return mPeriod;
    }

    /**
     * Get budget amount
     *
     * @return <b>double</b> mAmount
     */

    double GetAmount() {
        return mAmount;
    }

    /**
     * Get category IDs
     *
     * @return <b>Set&lt;Long&gt;</b> GetCatIds
     * @see Set
     */

    Set<Long> GetCatIds() {
        return mCatIds;
    }
}
