package Model;

import java.util.Map;
import java.util.Set;

/**
 * Created by Yifan on 10/12 012.
 */

public class BudgetModel {
    private Map<Long, Budget> mBudgetMap;

    /**
     * Constructor - Initialize a BudgetModel
     */
    public BudgetModel() {

    }

    /**
     * Add a new budget
     *
     * @param budget A Budget
     * @return <b>true</b> if budget is successfully added<p>
     * <b>false</b> if budget is already in mBudgetMap
     * @see Map#put(Object, Object)
     */
    boolean AddBudget(Budget budget) {
        if (mBudgetMap.containsKey(budget.GetId())) {
            return false;
        } else {
            mBudgetMap.put(budget.GetId(), budget);
            return true;
        }
    }

    /**
     * Update an existing budget
     *
     * @param budgetId ID of the budget you want to update
     * @param dueDate  due date
     * @param period   budget period
     * @param amount   budget amount
     * @param catIds   category Ids
     * @return <b>true</b> if budget is successfully updated<p>
     * <b>false</b> if budget is not in mBudgetMap
     */
    boolean UpdateBudget(long budgetId, long dueDate, int period, double amount, Set<Long> catIds) {
        if (mBudgetMap.containsKey(budgetId)) {
            Budget curr = mBudgetMap.get(budgetId);
            curr.UpdateDueDate(dueDate);
            curr.UpdatePeriod(period);
            curr.UpdateAmount(amount);
            curr.UpdateCatIds(catIds);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete a budget
     *
     * @param budgetId A Budget's ID
     * @return <b>true</b> if budget is successfully deleted<p>
     * <b>false</b> if budget is not in mBudgetMap
     * @see Map#remove(Object)
     */
    boolean DeleteBudget(long budgetId) {
        if (mBudgetMap.containsKey(budgetId)) {
            mBudgetMap.remove(budgetId);
            return true;
        } else {
            return false;
        }
    }
}
