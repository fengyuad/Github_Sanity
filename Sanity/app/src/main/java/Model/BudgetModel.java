package Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controller.OnGetDataListener;

/**
 * Created by Yifan on 10/12 012.
 */

public class BudgetModel extends Model implements Serializable {

    private static BudgetModel mInstance = null;
    private Map<Long, Budget> mBudgetMap;
    private String mUserID;

    /**
     * Constructor - Initialize a BudgetModel
     */
    private BudgetModel() {
        mBudgetMap = new HashMap<>();
        InitDataBase();
    }


    //<editor-fold desc="Singleton & Init">
    /* =============== Singleton & Init =============== */

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
     * When loading from local storage, a new instance should be updated
     *
     * @param bm a <b>BudgetModel</b> instance
     */
    public static void UpdateInstance(BudgetModel bm) {
        mInstance = bm;
    }

    /**
     * Initialize Firebase database connection
     */
    public void InitDataBase() {
        mUserID = Variable.GetInstance().getmUserID();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUserID).child("budget");
    }
    //</editor-fold>


    //<editor-fold desc="Notification">
    /* =============== Notification =============== */

    /**
     * Get notification
     *
     * @return list of the string displayed in notification
     */
    public List<String> getNotification() {
        double threshold = Variable.GetInstance().getmThreshold();
        int frequency = Variable.GetInstance().getmFrequency();
        List<String> ret = new ArrayList<>();
        for (Budget budget : mBudgetMap.values()) {
            Long timeRemain = (budget.getmDueTime() - budget.getmBudgetId()) / 86400000;
            if (budget.GetCurrAmount() > budget.GetAmountLimit() * threshold && budget.GetCurrAmount() <= budget.GetAmountLimit()) {
                StringBuilder sb = new StringBuilder();
                sb.append(budget.getmName() + " has reached "); // name of the category
                sb.append(threshold * 100 + "% of limit.\n"); // threshold of the category
                sb.append("Amount left: " + (budget.GetAmountLimit() - budget.GetCurrAmount()) + "; "); // amount left
                sb.append("Time remaining: " + (budget.getmDueTime() - budget.getmBudgetId()) / 86400000 + " Day(s)"); // time remaining
                ret.add(sb.toString());
            } else if (budget.GetCurrAmount() > budget.GetAmountLimit()) {
                StringBuilder sb = new StringBuilder();
                sb.append(budget.getmName() + " has exceeded limit. \n"); // name of the category
                sb.append("Amount left: " + (budget.GetAmountLimit() - budget.GetCurrAmount()) + "; "); // amount left
                sb.append("Time remaining: " + (budget.getmDueTime() - budget.getmBudgetId()) / 86400000 + " Day(s)"); // time remaining
                ret.add(sb.toString());
            }
        }
        return ret;
    }
    //</editor-fold>


    //<editor-fold desc="Budget Related">
    /* =============== Budget Related =============== */

    /**
     * Return budget map
     *
     * @return <b>Map</b>
     */
    public Map<Long, Budget> GetBudgetMap() {
        return mBudgetMap;
    }

    /**
     * Get a Budget
     *
     * @param budgetId budget ID
     * @return a <b>Budget</b>
     */
    public Budget getBudgetById(Long budgetId) {
        return mBudgetMap.get(budgetId);
    }

    /**
     * Get a list of Categories belongs to this budget
     *
     * @param budgetId budget ID
     * @return a <b>List</b> of Categories
     */
    public List<Category> getCategoriesUnderBudget(Long budgetId) {
        List<Category> cats = new ArrayList<>();
        CategoryModel catModel = CategoryModel.GetInstance();

        for (Long l : getBudgetById(budgetId).getmCatIds()) {
            cats.add(catModel.GetCategoryById(l));
        }
        return cats;
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
     * Remove a Category from a Budget
     *
     * @param budgetId budget ID
     * @param catId    category ID
     * @return success or not
     */
    public boolean AddACategory(long budgetId, long catId) {
        if (mBudgetMap.containsKey(budgetId)) {
            // Get Budget
            Budget curr = mBudgetMap.get(budgetId);
            // Update
            curr.AddCatId(catId);
            // Let Category know
            CategoryModel.GetInstance().UpdateBudgetIdAndUpdateDatabase(catId, budgetId);
            // Update Firebase
            CloudSet(curr);
            StorageModel.GetInstance().SaveAll();
            return true;
        } else {
            return false;
        }
    }

    /**
     * remove a Category from a Budget
     *
     * @param budgetId budget ID
     * @param catId    category ID
     * @return success or not
     */
    public boolean RemoveACategory(long budgetId, long catId) {
        if (mBudgetMap.containsKey(budgetId)) {
            // Get Budget
            Budget curr = mBudgetMap.get(budgetId);
            // Update
            curr.RemoveCatId(catId);
            CloudSet(curr);
            LocalUpdate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update an existing budget
     *
     * @param budgetId ID of the budget you want to update
     * @param dueTime  due date
     * @param period   budget period
     * @param catIds   category Ids
     * @return <b>true</b> if budget is successfully updated<p>
     * <b>false</b> if budget is not in mBudgetMap
     */
    public boolean UpdateBudget(long budgetId, String name) {
        if (mBudgetMap.containsKey(budgetId)) {
            // Get Budget
            Budget curr = mBudgetMap.get(budgetId);
            // Update
            curr.setmName(name);
            CloudSet(curr);
            LocalUpdate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update an existing budget
     *
     * @param budgetId ID of the budget you want to update
     * @param dueTime  due date
     * @param period   budget period
     * @param catIds   category Ids
     * @return <b>true</b> if budget is successfully updated<p>
     * <b>false</b> if budget is not in mBudgetMap
     */
    public boolean UpdateBudget(long budgetId, long dueTime, int period) {
        if (mBudgetMap.containsKey(budgetId)) {
            // Get Budget
            Budget curr = mBudgetMap.get(budgetId);
            // Update
            curr.setmDueTime(dueTime);
            curr.setmPeriod(period);
            CloudSet(curr);
            LocalUpdate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update an existing budget
     *
     * @param budgetId ID of the budget you want to update
     * @param dueTime  due date
     * @param period   budget period
     * @param catIds   category Ids
     * @return <b>true</b> if budget is successfully updated<p>
     * <b>false</b> if budget is not in mBudgetMap
     */
    public boolean UpdateBudget(long budgetId, List<Long> catIds) {
        if (mBudgetMap.containsKey(budgetId)) {
            // Get Budget
            Budget curr = mBudgetMap.get(budgetId);
            // Update
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
            for (long catId : mBudgetMap.get(budgetId).getmCatIds()) {
                CategoryModel.GetInstance().ResetCategoryAndUpdateDatabase(catId);
            }
            CloudRemove(mBudgetMap.get(budgetId));
            mBudgetMap.remove(budgetId);
            LocalUpdate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete all budgets from map
     */
    public void DeleteAllBudgets() {
        mBudgetMap.clear();
    }

    /**
     * Try to reset budgets that need to be reset
     * Call every time the user launch this app
     */
    public void ResetAllBudgets() {
        for (Budget b : mBudgetMap.values())
            b.ResetBudget();
        LocalUpdate();
    }
    //</editor-fold>


    //<editor-fold desc="Category Related">
    /* =============== Category Related =============== */

    /**
     * Called when Category is Added
     *
     * @param budgetId budget ID
     * @param catId    category ID
     */
    public void CategoryAdded(long budgetId, long catId) {
        if (budgetId == 0) return;
        mBudgetMap.get(budgetId).AddCatId(catId);
        CalcTotalAmount(budgetId);
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
     * Calculate total amount and update local&DB
     *
     * @param budgetId budget ID
     */
    public void CalcTotalAmount(long budgetId) {
        mBudgetMap.get(budgetId).UpdateTotalAmount();
        mBudgetMap.get(budgetId).UpdateAmountLimit();
        CloudSet(mBudgetMap.get(budgetId));
        LocalUpdate();
    }
    //</editor-fold>


    //<editor-fold desc="Data Storage">
    /* =============== Data Storage =============== */

    /**
     * Firebase SetValue Budget
     *
     * @param budget budget
     */
    public void CloudSet(Budget budget) {
        // Firebase Set
        mDatabase.child(Long.toString(budget.getmBudgetId())).setValue(budget);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
    }

    /**
     * Firebase GetValue Budget
     */
    public void CloudGet(final OnGetDataListener listener) {
        listener.onStart();
        // read data from database and store in mIDToCategory
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBudgetMap.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Budget budget = ds.getValue(Budget.class);
                    mBudgetMap.put(budget.getmBudgetId(), budget);
                }
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
    }

    /**
     * Firebase RemoveValue Budget
     *
     * @param budget budget
     */
    private void CloudRemove(Budget budget) {
        // Firebase Remove
        mDatabase.child(Long.toString(budget.getmBudgetId())).removeValue();
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
    }

    /**
     * Local Storage
     */
    private void LocalUpdate() {
        // Local Update
        StorageModel.GetInstance().SaveObject(this);
    }
    //</editor-fold>
}