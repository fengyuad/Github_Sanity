package Model;
import java.util.*;
import Model.*;

/**
 * Created by EricWang on 10/12/17.
 */

public class CategoryModel {
    // member variable
    private Map<Long, Category> mIDToCategory;
    private Set<String> nameCategoryUsed;

    // constructor
    public CategoryModel(){
        mIDToCategory = new HashMap<>();
        nameCategoryUsed = new HashSet<>();
    }

    // Public methods
    public void AddCategory(Category category)
    {
        mIDToCategory.put(category.GetID(), category);
    }

    public void DeleteCategory(Category category)
    {
        mIDToCategory.remove(category.GetID());
    }

    public boolean checkNameUsed(String name){
        return nameCategoryUsed.contains(name);
    }



}
