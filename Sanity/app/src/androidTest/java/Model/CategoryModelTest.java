package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EricWang on 10/28/17.
 */
public class CategoryModelTest {
    private CategoryModel mCM;

    @Before
    public void setUp() throws Exception {
        mCM = CategoryModel.GetInstance();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAllCategories() throws Exception {
        assertEquals(0, mCM.GetAllCategories().size(), 0);
    }

    @Test
    public void getAllNoUsedCategory() throws Exception {
        assertEquals(0, mCM.getAllNoUsedCategory().size(), 0);
    }

    @Test
    public void addGetDeleteCategory() throws Exception {
        Category newCat = new Category("test cat 1", 1L);
        mCM.AddCategory(newCat);
        assertEquals(1, mCM.GetAllCategories().size(), 0);
        assertEquals(newCat, mCM.GetCategoryById(newCat.getmID()));
        mCM.DeleteCategory(newCat.getmID());
        assertEquals(0, mCM.GetAllCategories().size(), 0);
    }

    @Test
    public void checkNameUsed() throws Exception {
        Category newCat = new Category("test", 1L);
        mCM.AddCategory(newCat);
        assertEquals(true, mCM.CheckNameUsed("test"));
    }

    @Test
    public void changeCategoryName() throws Exception {
        Category newCat = new Category("old", 1L);
        mCM.ChangeCategoryName(newCat.getmID(), "new");
        assertEquals("new", mCM.GetCategoryById(newCat.getmID()));
    }

    @Test
    public void addRemoveTransactionToCategory() throws Exception {
        Category newCat = new Category("test", 1L);
        mCM.AddCategory(newCat);
        mCM.AddTransactionToCategory(newCat.getmID(), 100L, 30.0);
        assertEquals(30.0, mCM.GetCategoryById(newCat.getmID()).getmCurrentAmount(), 0);
        assertEquals(1, mCM.GetCategoryById(newCat.getmID()).getmTransactionIDs().size());
        mCM.RemoveTransactionInCategory(newCat.getmID(), 100L, 30.0);
        assertEquals(0, mCM.GetCategoryById(newCat.getmID()).getmCurrentAmount(), 0);
        assertEquals(0, mCM.GetCategoryById(newCat.getmID()).getmTransactionIDs().size());
    }

}