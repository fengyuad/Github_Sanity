package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EricWang on 10/28/17.
 */
public class CategoryTest {
    private Category mCategory;

    @Before
    public void setUp() throws Exception {
        mCategory = new Category("Test Category", 100.0, -1L);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getmName() throws Exception {
        assertEquals("Test Category", mCategory.getmName());
    }

    @Test
    public void setmName() throws Exception {
        mCategory.setmName("new test name");
        assertEquals("new test name", mCategory.getmName());
    }

    @Test
    public void setmID() throws Exception {
        mCategory.setmID(1001L);
        assertEquals(Long.valueOf(1001L), mCategory.getmID());
    }

    @Test
    public void getmBudgetID() throws Exception {
        assertEquals(Long.valueOf(-1L), mCategory.getmBudgetID());
    }

    @Test
    public void setmBudgetID() throws Exception {
        mCategory.setmBudgetID(1001L);
        assertEquals(Long.valueOf(1001L), mCategory.getmBudgetID());
    }

    @Test
    public void getmAmount() throws Exception {
        assertEquals(100.0, mCategory.getmAmount(), 0);
    }

    @Test
    public void setmAmount() throws Exception {
        mCategory.setmAmount(200.0);
        assertEquals(200.0, mCategory.getmAmount(), 0);
    }

    @Test
    public void getmCurrentAmount() throws Exception {
        assertEquals(0.0, mCategory.getmCurrentAmount(), 0);
    }

    @Test
    public void setmCurrentAmount() throws Exception {
        mCategory.setmCurrentAmount(100.0);
        assertEquals(100.0, mCategory.getmCurrentAmount(), 0);
    }

    @Test
    public void reset() throws Exception {
        mCategory.Reset();
        assertEquals("", mCategory.getmName());
        assertEquals(0.0, mCategory.getmCurrentAmount(), 0);
        assertEquals(0.0, mCategory.getmAmount(), 0);
        assertEquals(0, mCategory.getmBudgetID(), 0);
        assertEquals(0.0, mCategory.getmCurrentAmount(), 0);
        assertEquals(0, mCategory.getmTransactionIDs().size(), 0);
    }

}