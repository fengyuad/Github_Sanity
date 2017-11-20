package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yifan on 10/12 012.
 */

public class Budget implements Serializable {
    private long mBudgetId = 0;
    private String mName = "";
    //private int mDueDate = 1;
    //private boolean mIsWeek = true;
    private long mDueTime = 0;
    private int mPeriod = 0;
    private double mAmount = 0.0;
    private double mTotalAmount = 0.0;
    private List<Long> mCatIds = new ArrayList<>();

    /**
     * Default Constructor
     */
    public Budget() {

    }

    /**
     * Constructor - Initialize a Budget
     *
     * @param dueTime due time
     * @param period  budget period
     * @param catIds  category Ids
     */
    public Budget(String name, long dueTime, int period, List<Long> catIds) {
        mBudgetId = System.currentTimeMillis();
        mName = name;
        mDueTime = dueTime;
        mPeriod = period;
        mCatIds = catIds;
        UpdateTotalAmount();
        UpdateAmountLimit();
    }


    //<editor-fold desc="Budget Update">
    /* =============== Budget Update =============== */

    /**
     * Update current balance of the budget
     * Should be triggered whenever a transaction/category change happends
     */
    public void UpdateTotalAmount() {
        double totalAmount = 0.0;
        for (long catId : mCatIds){
            Category cat = CategoryModel.GetInstance().GetCategoryById(catId);
            if(cat == null) continue;
            totalAmount += CategoryModel.GetInstance().GetCategoryById(catId).getmCurrentAmount();
        }
        mTotalAmount = totalAmount;
    }

    public void UpdateAmountLimit() {
        double tempAmount = 0.0;
        for (Long catId : mCatIds){
            if(CategoryModel.GetInstance().GetCategoryById(catId) == null) continue;
            tempAmount += CategoryModel.GetInstance().GetCategoryById(catId).getmAmount();
        }
        mAmount = tempAmount;
    }

    /**
     * Reset budget if it's time (should be triggered somewhere)
     */
    public void ResetBudget() {
        // If new period
        /*Calendar rightNow = Calendar.getInstance();
        if (mIsWeek)
        {
            if (mDueDate < rightNow.get(Calendar.DAY_OF_MONTH)) {
                mPrevAmount = mTotalAmount;
            }
            else
            {

            }
        }
        else{

        }*/
        if (mDueTime <= System.currentTimeMillis()) {
            mDueTime += 86400000 * mPeriod;
            // TODO 1018
            for (Long catId : mCatIds) {
                CategoryModel.GetInstance().ResetCategoryPeriodEnds(catId);
            }
            UpdateTotalAmount();
            UpdateAmountLimit();
            BudgetModel.GetInstance().CloudSet(this);
        }
    }

    public void PutToSaving(){
        if(getmName().equalsIgnoreCase("Saving")) return;
        if(mAmount > mTotalAmount){
            double leftAmount = mAmount - mTotalAmount;
            if(BudgetModel.GetInstance().GetBudgetByName("Saving") == null) return;
            double limit = BudgetModel.GetInstance().GetBudgetByName("Saving").getmAmount() + leftAmount;
            BudgetModel.GetInstance().GetBudgetByName("Saving").setmAmount(limit);
            BudgetModel.GetInstance().CloudSet(BudgetModel.GetInstance().GetBudgetByName("Saving"));
        }
    }


    public void Rollover(){
        if(getmName().equalsIgnoreCase("Saving")) return;

        for(Long l: mCatIds){
            Category cat = CategoryModel.GetInstance().GetCategoryById(l);
            if(cat == null) continue;;

            if(cat.getmAmount() > cat.getmCurrentAmount()){
                double left = cat.getmAmount() - cat.getmCurrentAmount();
                cat.setmAmount(cat.getmAmount() + left);
                CategoryModel.GetInstance().UpdateAmountAndUpdateDatabase(cat.getmID(), cat.getmAmount());
            }
        }
    }

    //</editor-fold>


    //<editor-fold desc="Category Related">
    /* =============== Category Related =============== */

    /**
     * add a category to this budget
     *
     * @param catId a category ID
     */
    public void AddCatId(long catId) {
        mCatIds.add(catId);
    }

    /**
     * remove a category from this budget
     *
     * @param catId a category ID
     */
    public void RemoveCatId(long catId) {
        mCatIds.remove(catId);
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    /* =============== Getters and Setters =============== */

    /**
     * Getter
     *
     * @return mAmount
     */
    public double GetAmountLimit() {
        UpdateAmountLimit();
        return mAmount;
    }

    /**
     * return current budget balance
     *
     * @return <b>double</b> budget balance
     */
    public double GetCurrAmount() {
        UpdateTotalAmount();
        return mTotalAmount;
    }

    /**
     * Getter
     *
     * @return mName
     */
    public String getmName() {
        return mName;
    }

    /**
     * Setter
     *
     * @param mName
     */
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
     * Getter
     *
     * @return mDueTime
     */
    public long getmDueTime() {
        return mDueTime;
    }

    /**
     * Setter
     *
     * @param mDueTime
     */
    public void setmDueTime(long mDueTime) {
        this.mDueTime = mDueTime;
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
    //</editor-fold>


    public double getmAmount() {
        return mAmount;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public double getmTotalAmount() {
        return mTotalAmount;
    }

    public void setmTotalAmount(double mTotalAmount) {
        this.mTotalAmount = mTotalAmount;
    }
}

