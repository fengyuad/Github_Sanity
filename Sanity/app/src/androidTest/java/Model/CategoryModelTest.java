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

}