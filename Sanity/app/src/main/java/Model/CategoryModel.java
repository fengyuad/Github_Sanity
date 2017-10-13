package Model;
import java.util.Map;


import Model.*;

/**
 * Created by EricWang on 10/12/17.
 */

public class CategoryModel {
    // member variable
    private Map<String, Category> mNameToCategory;

    // public methods
    public void AddCategory(Category category)
    {
        mCategories.add(category);
    }

}
