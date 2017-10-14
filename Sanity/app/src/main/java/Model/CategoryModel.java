package Model;
import android.util.Log;

import java.util.*;
import Model.*;
import com.google.firebase.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by EricWang on 10/12/17.
 */

public class CategoryModel {
    // member variable
    private Map<Long, Category> mIDToCategory;
    private Set<String> nameCategoryUsed;
    private DatabaseReference mCategoryRef;


    // constructor
    public CategoryModel(){
        mIDToCategory = new HashMap<>();
        nameCategoryUsed = new HashSet<>();
        mCategoryRef = FirebaseDatabase.getInstance().getReference("categories");




        // read from the database
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Map<String, Object> catInfo = (HashMap<String, Object>) ds.getValue();
                    Log.d("id+name+amount", ds.getKey() + " " + (String)catInfo.get("name") + " " + catInfo.get("amount"));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    // Public methods
    public void AddCategory(Category category)
    {
        mIDToCategory.put(category.GetID(), category);
    }

    public void DeleteCategory(Long id)
    {
        mIDToCategory.remove(id);
    }

    public boolean CheckNameUsed(String name){
        return nameCategoryUsed.contains(name);
    }

    public void UpdateCategory(Long id, double amount){
        mIDToCategory.get(id).AddCurrentAmount(amount);
    }




}
