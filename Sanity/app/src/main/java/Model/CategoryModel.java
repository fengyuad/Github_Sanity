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
    private Map<String, Category> mIDToCategory;
    private List<String> nameCategoryUsed;
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
        nameCategoryUsed = new ArrayList<String>();

        // read data from database and store in mIDToCategory
        mDatabase = FirebaseDatabase.getInstance().getReference(mUserID + "/category");
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
     * Public method //////////
     */
    public void AddCategory(Category category) {
        mIDToCategory.put(category.getmID(), category);
    }

    /**
     *
     * @param id
     */
    public void DeleteCategory(Long id) {
        mIDToCategory.remove(id);
    }

    /**
     *
     * @param name
     * @return true if name already used
     */
    public boolean CheckNameUsed(String name) {
        return nameCategoryUsed.contains(name);
    }

    /**
     * Update the current amount
     * @param id
     * @param amount
     */
    public void UpdateCategory(Long id, double amount) {
        mIDToCategory.get(id).AddCurrentAmount(amount);
    }

    public boolean WriteNewCategory(Category cat){
        String key = mDatabase.push().getKey();
        cat.setmID(key);
        mIDToCategory.put(key, cat);
        mDatabase.setValue(mIDToCategory);

        return true;
    }

}
