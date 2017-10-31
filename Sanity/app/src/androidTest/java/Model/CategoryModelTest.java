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
    public void addCategory() throws Exception {
        Category newCat = new Category("test cat 1", 1L);
        mCM.AddCategory(newCat);
        assertEquals(1, mCM.GetAllCategories().size(), 0);
    }

    @Test
    public void getCategory() throws Exception {
        Category newCat = new Category("test cat 1", 1L);
        mCM.AddCategory(newCat);
        assertEquals(newCat, mCM.GetCategoryById(newCat.getmID()));
        mCM.DeleteCategory(newCat.getmID());
        assertEquals(0, mCM.GetAllCategories().size(), 0);
    }

    @Test
    public void deleteCategory() throws Exception {
        Category newCat = new Category("test cat 1", 1L);
        mCM.AddCategory(newCat);
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
        mCM.AddCategory(newCat);
        mCM.ChangeCategoryName(newCat.getmID(), "new");
        assertEquals("new", mCM.GetCategoryById(newCat.getmID()).getmName());
    }

    @Test
    public void addTransactionToCategory() throws Exception {
        Category newCat = new Category("test", 1L);
        mCM.AddCategory(newCat);
        mCM.AddTransactionToCategory(newCat.getmID(), 100L, 30.0);
        assertEquals(30.0, mCM.GetCategoryById(newCat.getmID()).getmCurrentAmount(), 0);
        assertEquals(1, mCM.GetCategoryById(newCat.getmID()).getmTransactionIDs().size());
        mCM.RemoveTransactionInCategory(newCat.getmID(), 100L, 30.0);
        assertEquals(0, mCM.GetCategoryById(newCat.getmID()).getmCurrentAmount(), 0);
        assertEquals(0, mCM.GetCategoryById(newCat.getmID()).getmTransactionIDs().size());
    }

    @Test
    public void singletonTest() throws Exception {
        CategoryModel cm2 = CategoryModel.GetInstance();
        Category newCat = new Category("test", 1L);
        Category newCat2 = new Category("test2", 2L);
        cm2.AddCategory(newCat);
        mCM.AddCategory(newCat2);
        assertEquals(cm2, mCM);
    }

}