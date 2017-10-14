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

public class CategoryModel extends Model {
    /**
     *  member variable
     */
    private static CategoryModel mInstance = null;
    private Map<Long, Category> mIDToCategory;
    private List<String> nameCategoryUsed;
    private DatabaseReference mCategoryRef;

    /**
     * Get the instance of this model
     *
     * @return <b>CategoryModel</b> a BudgetModel instance
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
    public CategoryModel() {
        // initialize the data struture
        mIDToCategory = new HashMap<>();
        nameCategoryUsed = new ArrayList<String>();

        // read data from database
        mCategoryRef = FirebaseDatabase.getInstance().getReference("categories");
        mCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> catInfo = (HashMap<String, Object>) ds.getValue();
                    Log.d("id+name+amount", ds.getKey() + " " + (String) catInfo.get("name") + " " + catInfo.get("amount"));
                    String name = (String) catInfo.get("name");
                    String amount = (String)catInfo.get("amount");
                    Double currentAmount = ((Long) catInfo.get("current-amount")).doubleValue();
                    Long key = Long.parseLong(ds.getKey());
                    mIDToCategory.put(key, new Category(name, Double.parseDouble(amount), key, currentAmount));
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
        mIDToCategory.put(category.GetID(), category);
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


}
