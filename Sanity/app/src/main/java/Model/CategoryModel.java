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
