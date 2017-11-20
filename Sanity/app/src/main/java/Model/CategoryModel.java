package Model;

import android.util.Log;

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
 * Created by EricWang on 10/12/17.
 */

public class CategoryModel extends Model implements Serializable {
    /**
     * member variable
     */
    private static CategoryModel mInstance = null;
    public Map<Long, Category> mIDToCategory;
    private List<String> mNameCategoryUsed;
    private String mUserID;


    /**
     * Constructor, read from database
     */
    private CategoryModel() {
        super();
        // initialize the data struture
        mIDToCategory = new HashMap<>();
        mNameCategoryUsed = new ArrayList<String>();
        InitDataBase();

    }

    /**
     * Get the instance of this model
     *
     * @return <b>CategoryModel</b> a CategoryModel instance
     */
    public static CategoryModel GetInstance() {
        if (mInstance == null) {
            mInstance = new CategoryModel();
        }
        return mInstance;
    }

    /**
     * When loading from local storage, a new instance should be updated
     *
     * @param cm a <b>CategoryModel</b> instance
     */
    public static void UpdateInstance(CategoryModel cm) {
        mInstance = cm;
    }

    public void InitDataBase() {

        mUserID = Variable.GetInstance().getmUserID();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUserID).child("category");
    }

    /**
     * ----------------- Public method on local -----------------
     */
    public List<Category> GetAllCategories() {
        List<Category> list = new ArrayList<>();
        for (Category cat : mIDToCategory.values()) {
            list.add(cat);
        }
        return list;
    }

    /**
     * add category to map and nameCategoryUsed
     *
     * @param category
     */
    public void AddCategory(Category category) {
        mIDToCategory.put(category.getmID(), category);
        mNameCategoryUsed.add(category.getmName());
    }

    /**
     * @param id
     */
    public void DeleteCategory(Long id) {
        mNameCategoryUsed.remove(mIDToCategory.get(id));
        mIDToCategory.remove(id);
    }

    /**
     * @param name
     * @return true if name already used
     */
    public boolean CheckNameUsed(String name) {
        return mNameCategoryUsed.contains(name);
    }

    /**
     * @param id
     * @param name
     * @return true if change successfully, false if contains duplicate name
     */
    public boolean ChangeCategoryName(Long id, String name) {
        Category cat = mIDToCategory.get(id);
        // same name as previous
        if (cat.getmName().equals(name)) return true;

        // same as name already used
        if (mNameCategoryUsed.contains(name)) return false;

        // change the name on local
        mNameCategoryUsed.remove(cat.getmName());
        cat.setmName(name);
        mNameCategoryUsed.add(name);
        return true;
    }

    /**
     * @param id
     * @return Category
     */
    public Category GetCategoryById(Long id) {
        return mIDToCategory.get(id);
    }

    /**
     * @param catID
     * @param tranID
     * @return
     */
    public void AddTransactionToCategory(Long catID, Long tranID, double amount) {
        mIDToCategory.get(catID).AddTransaction(tranID, amount);
    }

    /**
     * @param catID
     * @param tranID
     * @param amount
     */
    public void RemoveTransactionInCategory(Long catID, Long tranID, double amount) {
        Log.d("test", "size of category map: " + mIDToCategory.size());
        for (Long l : mIDToCategory.keySet()) {
            Log.d("test", "category map: " + l);
        }
        mIDToCategory.get(catID).RemoveTransaction(tranID, amount);
    }

    /**
     * @return list of the string displayed in notification
     */
    public List<String> getNotification() {
        double threshold = Variable.GetInstance().getmThreshold();
        List<String> ret = new ArrayList<>();

        for (Long l : mIDToCategory.keySet()) {
            Category cat = mIDToCategory.get(l);
            if (cat.getmCurrentAmount() > cat.getmAmount() * threshold && cat.getmCurrentAmount() <= cat.getmAmount()) {
                StringBuilder sb = new StringBuilder();
                sb.append(cat.getmName() + " has reached "); // name of the category
                sb.append(threshold * 100 + "% of limit.\n"); // threshold of the category
                sb.append("Amount left: " + (cat.getmAmount() - cat.getmCurrentAmount()) + "; "); // amount left
                if(BudgetModel.GetInstance().getBudgetById(cat.getmBudgetID()) == null) continue;
                Long dayLeft = (BudgetModel.GetInstance().getBudgetById(cat.getmBudgetID()).getmDueTime() - System.currentTimeMillis())/86400000;
                sb.append("Time remaining: " + dayLeft); // time remaining
                ret.add(sb.toString());
            }else if(cat.getmCurrentAmount() > cat.getmAmount()){
                StringBuilder sb = new StringBuilder();
                sb.append(cat.getmName() + " has reached limit.\n"); // name of the category
                sb.append("Amount left: " + (cat.getmAmount() - cat.getmCurrentAmount()) + "; "); // amount
                Long dayLeft = (BudgetModel.GetInstance().getBudgetById(cat.getmBudgetID()).getmDueTime() - System.currentTimeMillis())/86400000;
                sb.append("Time remaining: " + dayLeft); // time remaining
                ret.add(sb.toString());
            }
        }
        return ret;

    }

    public List<Category> getAllNoUsedCategory() {
        List<Category> list = new ArrayList<>();
        for (Category cat : mIDToCategory.values()) {
            if (cat.getmBudgetID() == 0) list.add(cat);
        }
        return list;
    }

    public Map<String, Double> allCatsToTransSum(){
        Map<String, Double> map = new HashMap<>();
        for(Category c: mIDToCategory.values()){

            double sum = 0;
            for(Long l: c.getmTransactionIDs()){
                Transaction tran = TransactionModel.GetInstance().getmTransactions().get(l);
                if(tran != null){
                    sum += tran.getmAmount();
                }
            }
            map.put(c.getmName(), sum);
        }
        return map;
    }

    /**
     *----------------- Database Related -----------------
     */

    public boolean UpdateBudgetIdAndUpdateDatabase(Long catId, Long budgetId)
    {
        if (mIDToCategory.containsKey(catId)) {
            // Get Budget
            Category curr = mIDToCategory.get(catId);
            // Update
            curr.setmBudgetID(budgetId);
            mDatabase.child(catId.toString()).child("mBudgetID").setValue(budgetId);
            FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
            Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
            StorageModel.GetInstance().SaveAll();
            return true;
        } else {
            return false;
        }
    }

    public boolean UpdateAmountAndUpdateDatabase(Long catId, double amount)
    {
        if (mIDToCategory.containsKey(catId)) {
            // Get Budget
            Category curr = mIDToCategory.get(catId);
            // Update
            curr.setmAmount(amount);
            mDatabase.child(catId.toString()).child("mAmount").setValue(amount);
            FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
            Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
            BudgetModel.GetInstance().CalcTotalAmount(curr.getmBudgetID());
            StorageModel.GetInstance().SaveAll();
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param cat
     * @return false if find duplicate category name
     */
    public boolean WriteCategoryAndUpdateDatabase(Category cat) {
        // check duplicate name
        if (CheckNameUsed(cat.getmName())) return false;

        Long key = System.currentTimeMillis();
        // Set Cat ID
        cat.setmID(key);
        // Add Cat to Map
        AddCategory(cat);
        // Add Cat info to Budget (return if budgetId=0)
        BudgetModel.GetInstance().CategoryAdded(cat.getmBudgetID(), cat.getmID());
        // Update Firebase
        mDatabase.child(key.toString()).setValue(cat);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        // Update Local File
        StorageModel.GetInstance().SaveAll();
        return true;
    }

    /**
     * read all category
     */
    public void ReadCategoryFromDatabase(final OnGetDataListener listener) {
        listener.onStart();
        // read data from database and store in mIDToCategory
        Log.d("test", "read from categroy db");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mIDToCategory.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category cat = ds.getValue(Category.class);
                    AddCategory(cat);
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
     * delete category on the database
     *
     * @param catID
     */
    public void DeleteCategoryAndUpdateDatabase(Long catID) {
        if(mIDToCategory.get(catID).getmBudgetID() == 0 || BudgetModel.GetInstance().getBudgetById(mIDToCategory.get(catID).getmBudgetID()) == null) {
            DeleteCategory(catID);
            mDatabase.child(catID.toString()).removeValue();
            FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
            Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
            StorageModel.GetInstance().SaveAll();
            return;
        }
        for (Long l : mIDToCategory.get(catID).getmTransactionIDs()) {
            // remove transaction by id, call TransactionModel method
            TransactionModel.GetInstance().DeleteTransaction(l, false);
        }

        // Update Budget Amount
        BudgetModel.GetInstance().CategoryRemoved(mIDToCategory.get(catID).getmBudgetID(), mIDToCategory.get(catID).getmID());
        DeleteCategory(catID);
        mDatabase.child(catID.toString()).removeValue();
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        StorageModel.GetInstance().SaveAll();
    }

    /**
     * @param id
     * @param name
     * @return true if successfully update the name, false if duplicate name
     */
    public boolean ChangeCategoryNameAndUpdateDatabase(Long id, String name) {
        // duplicate name
        if (!ChangeCategoryName(id, name)) return false;

        mDatabase.child(id.toString()).child("mName").setValue(name);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        StorageModel.GetInstance().SaveAll();
        return true;
    }

    /**
     * @param catID
     * @param tranID
     */
    public void AddTransactionIDToCategoryAndUpdateDatabase(Long catID, Long tranID, double amount) {
        AddTransactionToCategory(catID, tranID, amount);
        List<Long> list = mIDToCategory.get(catID).getmTransactionIDs();
        mDatabase.child(catID.toString()).child("mTransactionIDs").setValue(list);
        mDatabase.child(catID.toString()).child("mCurrentAmount").setValue(mIDToCategory.get(catID).getmCurrentAmount());
        BudgetModel.GetInstance().CalcTotalAmount(mIDToCategory.get(catID).getmBudgetID());
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        StorageModel.GetInstance().SaveAll();
    }

    /**
     * @param catID
     * @param tranID
     * @param amount
     */
    public void RemoveTransactionInCategoryAndUpdateDatabase(Long catID, Long tranID, double amount) {
        RemoveTransactionInCategory(catID, tranID, amount);
        List<Long> list = mIDToCategory.get(catID).getmTransactionIDs();
        mDatabase.child(catID.toString()).child("mTransactionIDs").setValue(list);
        mDatabase.child(catID.toString()).child("mCurrentAmount").setValue(mIDToCategory.get(catID).getmAmount());
        BudgetModel.GetInstance().CalcTotalAmount(mIDToCategory.get(catID).getmBudgetID());
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        StorageModel.GetInstance().SaveAll();
    }

    /**
     * @param catID
     */
    public void ResetCategoryAndUpdateDatabase(Long catID) {
        Category cat = mIDToCategory.get(catID).Reset();
        mDatabase.child(catID.toString()).setValue(cat);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        StorageModel.GetInstance().SaveAll();
    }

    public void ResetCategoryPeriodEnds(Long catID) {
        Category cat = mIDToCategory.get(catID);
        cat.setmCurrentAmount(0);
        mDatabase.child(catID.toString()).setValue(cat);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        StorageModel.GetInstance().SaveAll();
    }
}
