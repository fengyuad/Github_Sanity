package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yifan on 10/12 012.
 */

public class BudgetModel extends Model implements Serializable {

    private static BudgetModel mInstance = null;
    private Map<Long, Budget> mBudgetMap;

    /**
     * Constructor - Initialize a BudgetModel
     */
    private BudgetModel() {
        mBudgetMap = new HashMap<>();
    }

    /**
     * Get the instance of this model
     *
     * @return <b>BudgetModel</b> a BudgetModel instance
     */
    public static BudgetModel GetInstance() {
        if (mInstance == null) {
            mInstance = new BudgetModel();
        }
        return mInstance;
    }

    /**
     * Add a new budget
     *
     * @param budget A Budget
     * @return <b>true</b> if budget is successfully added<p>
     * <b>false</b> if budget is already in mBudgetMap
     * @see Map#put(Object, Object)
     */
    public boolean AddBudget(Budget budget) {
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
    public boolean UpdateBudget(long budgetId, long dueDate, int period, double amount, List<Long> catIds) {
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
    public boolean DeleteBudget(long budgetId) {
        if (mBudgetMap.containsKey(budgetId)) {
            mBudgetMap.remove(budgetId);
            return true;
        } else {
            return false;
        }
    }
}
