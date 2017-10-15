package Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;




/**
 * Created by EricWang on 10/12/17.
 */

public class CategoryModel extends Model implements java.io.Serializable {
    /**
     *  member variable
     */
    private static CategoryModel mInstance = null;
    public Map<Long, Category> mIDToCategory;
    private List<String> mNameCategoryUsed;
    private DatabaseReference mDatabase;

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
     * Constructor, read from database
     */
    private CategoryModel() {
        super();
        // initialize the data struture
        mIDToCategory = new HashMap<>();
        mNameCategoryUsed = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference(mUserID + "/category");
    }

    /**
     *----------------- Public method on local -----------------
     */

    /**
     * add category to map and nameCategoryUsed
     * @param category
     */
    private void AddCategory(Category category) {
        category.setmID(System.currentTimeMillis()/1000);
        mIDToCategory.put(category.getmID(), category);
        mNameCategoryUsed.add(category.getmName());
    }

    /**
     *
     * @param id
     */
    private void DeleteCategory(Long id) {
        mNameCategoryUsed.remove(mIDToCategory.get(id));
        mIDToCategory.remove(id);
    }

    /**
     * @param name
     * @return true if name already used
     */
    private boolean CheckNameUsed(String name) {
        return mNameCategoryUsed.contains(name);
    }

    /**
     * Update the current amount
     * @param id
     * @param amount
     * @return id's current amount
     */
    private double AddCategoryAmount(Long id, double amount) {
        return mIDToCategory.get(id).AddCurrentAmount(amount);
    }

    /**
     *
     * @param id
     * @param name
     * @return true if change successfully, false if contains duplicate name
     *
     */
    private boolean ChangeCategoryName(Long id, String name){
        Category cat = mIDToCategory.get(id);
        // same name as previous
        if(cat.getmName().equals(name)) return true;

        // same as name already used
        if(mNameCategoryUsed.contains(name)) return false;

        // change the name on local
        mNameCategoryUsed.remove(cat.getmName());
        cat.setmName(name);
        mNameCategoryUsed.add(name);
        return true;
    }

    /**
     *----------------- Database Related -----------------
     */

    /**
     *
     * @param cat
     * @return true if successfully write into the database
     * @return false if find duplicate category name
     */
    public boolean WriteCategoryAndUpdateDatabase(Category cat){
        // check duplicate name
        if(mNameCategoryUsed.contains(cat.getmName())) return false;
        AddCategory(cat);
        mDatabase.child(cat.getmID().toString()).setValue(cat);
        return true;
    }

    /**
     * read all category
     */
    public void ReadCategoryFromDatabase(){
        // read data from database and store in mIDToCategory
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Category cat = ds.getValue(Category.class);
                    mIDToCategory.put(cat.getmID(), cat);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * delete category on the database
     * @param catID
     */
    public void DeleteCategoryAndUpdateDatabase(Long catID){
        DeleteCategory(catID);
        mDatabase.child(catID.toString()).removeValue();
    }

    /**
     *
     * @param id
     * @param name
     * @return true if successfully update the name, false if duplicate name
     */
    public boolean ChangeCategoryNameAndUpdateDatabase(Long id, String name){
        // duplicate name
        if(!ChangeCategoryName(id, name)) return false;

        mDatabase.child(id.toString()).child("mName").setValue(name);
        return true;
    }

    /**
     *
     * @param id
     * @param amount
     * @return id's current amount
     */
    public double AddCategoryAmountAndUpdateDatabase(Long id, double amount){
        double currentAmount = AddCategoryAmount(id, amount);
        mDatabase.child(id.toString()).child("mCurrentAmount").setValue(currentAmount);
        return currentAmount;
    }

}
