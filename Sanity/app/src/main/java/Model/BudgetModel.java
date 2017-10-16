package Model;

import com.google.firebase.database.FirebaseDatabase;

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
        InitDataBase();
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

    public static void UpdateInstance(BudgetModel bm) {
        mInstance = bm;
    }

    public void InitDataBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUserID).child("budget");
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
        if (mBudgetMap.containsKey(budget.getmBudgetId())) {
            return false;
        } else {
            // Add
            mBudgetMap.put(budget.getmBudgetId(), budget);
            CloudSet(budget);
            LocalUpdate();
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
    public boolean UpdateBudget(String name, long budgetId, long dueDate, int period, double amount, List<Long> catIds) {
        if (mBudgetMap.containsKey(budgetId)) {
            // Get Budget
            Budget curr = mBudgetMap.get(budgetId);
            // Update
            curr.setmName(name);
            curr.setmDueDate(dueDate);
            curr.setmPeriod(period);
            curr.setmAmount(amount);
            curr.setmCatIds(catIds);
            CloudSet(curr);
            LocalUpdate();
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
            // Delete
            mBudgetMap.remove(budgetId);
            for (long catId : mBudgetMap.get(budgetId).getmCatIds()) {
                CategoryModel.GetInstance().DeleteCategoryAndUpdateDatabase(catId);
            }
            CloudRemove(mBudgetMap.get(budgetId));
            LocalUpdate();
            return true;
        } else {
            return false;
        }
    }

    public void AddCategory(long budgetId, long catId) {
        mBudgetMap.get(budgetId).AddCatId(catId);
        CalcTotalAmount(budgetId);
        CloudSet(mBudgetMap.get(budgetId));
        LocalUpdate();
    }

    public void CalcTotalAmount(long budgetId) {
        mBudgetMap.get(budgetId).UpdateTotalAmount();
    }

    public void CategoryRemoved(long budgetId, long catId) {
        mBudgetMap.get(budgetId).RemoveCatId(catId);
        CalcTotalAmount(budgetId);
        CloudSet(mBudgetMap.get(budgetId));
        LocalUpdate();
    }

    private void CloudSet(Budget budget) {
        // Firebase Set
        mDatabase.child(Long.toString(budget.getmBudgetId())).setValue(budget);
    }

    private void CloudRemove(Budget budget) {
        // Firebase Remove
        mDatabase.child(Long.toString(budget.getmBudgetId())).removeValue();
    }

    private void LocalUpdate() {
        // Local Update
        StorageModel.GetInstance().SaveObject(this);
    }
}