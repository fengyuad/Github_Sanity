package Model;

import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
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

    /**
     * Called when Category is Added
     *
     * @param budgetId budget ID
     * @param catId    category ID
     */
    public void CategoryAdded(long budgetId, long catId) {
        mBudgetMap.get(budgetId).AddCatId(catId);
        CalcTotalAmount(budgetId);
    }

    /**
     * Calculate total amount and update local&DB
     *
     * @param budgetId budget ID
     */
    public void CalcTotalAmount(long budgetId) {
        mBudgetMap.get(budgetId).UpdateTotalAmount();
        CloudSet(mBudgetMap.get(budgetId));
        LocalUpdate();
    }

    /**
     * Called when Category is Removed
     *
     * @param budgetId budget ID
     * @param catId    category ID
     */
    public void CategoryRemoved(long budgetId, long catId) {
        mBudgetMap.get(budgetId).RemoveCatId(catId);
        CalcTotalAmount(budgetId);
    }

    /**
     *
     * @return list of the string displayed in notification
     */
    public List<String> getNotification(){
        double threshold = Variable.GetInstance().getmThreshold();
        int frequency = Variable.GetInstance().getmFrequency();
        List<String> ret = new ArrayList<>();

        for(Long l: mBudgetMap.keySet()){
            Budget budget = mBudgetMap.get(l);
            if(budget.getmAmount() >= budget.getmTotalAmount() * threshold){
                StringBuilder sb = new StringBuilder();
                sb.append(budget.getmName() + " has reached "); // name of the category
                sb.append(threshold * 100 + "% of limit.\n"); // threshold of the category
                sb.append("Amount left: " + (budget.getmAmount() - budget.getmTotalAmount()) + "; "); // amount left
                sb.append("Time remaining: " ); // time remaining
                ret.add(sb.toString());
            }
        }
        return ret;

    }


    /**
     * Firebase SetValue Budget
     *
     * @param budget budget
     */
    private void CloudSet(Budget budget) {
        // Firebase Set
        mDatabase.child(Long.toString(budget.getmBudgetId())).setValue(budget);
    }

    /**
     * Firebase RemoveValue Budget
     *
     * @param budget budget
     */
    private void CloudRemove(Budget budget) {
        // Firebase Remove
        mDatabase.child(Long.toString(budget.getmBudgetId())).removeValue();
    }

    /**
     * Local Storage
     */
    private void LocalUpdate() {
        // Local Update
        StorageModel.GetInstance().SaveObject(this);
    }
}