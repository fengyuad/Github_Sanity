package Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yifan on 10/26 026.
 */
public class BudgetTest {
    private Budget budget;

    @Before
    public void setUp() throws Exception {
        budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
    }

    @Test
    public void updateTotalAmount() throws Exception {
        //TODO: 联动
        //budget.UpdateTotalAmount();
    }

    @Test
    public void updateAmountLimit() throws Exception {
        //TODO: 联动
        //budget.UpdateAmountLimit();
    }

    @Test
    public void resetBudget() throws Exception {
        //TODO: 联动
        //budget.ResetBudget();
    }

    @Test
    public void getmCatIds() throws Exception {
        assertEquals(true, budget.getmCatIds().isEmpty());
    }

    @Test
    public void setmCatIds() throws Exception {
        List<Long> mCatIds = new ArrayList<>();
        mCatIds.add(1508976000L);
        mCatIds.add(1509062400L);
        budget.setmCatIds(mCatIds);
        assertEquals(true, budget.getmCatIds().contains(1508976000L));
        assertEquals(true, budget.getmCatIds().contains(1509062400L));
    }

    @Test
    public void addCatId() throws Exception {
        budget.AddCatId(1508976000L);
        budget.AddCatId(1509062400L);
        assertEquals(true, budget.getmCatIds().contains(1508976000L));
        assertEquals(true, budget.getmCatIds().contains(1509062400L));
    }

    @Test
    public void removeCatId() throws Exception {
        budget.AddCatId(1508976000L);
        budget.RemoveCatId(1508976000L);
        assertEquals(false, budget.getmCatIds().contains(1508976000L));
    }

    @Test
    public void getAmountLimit() throws Exception {
        //TODO: 联动
        //double amount = budget.GetAmountLimit();
    }

    @Test
    public void getCurrAmount() throws Exception {
        //TODO: 联动
    }

    @Test
    public void getmName() throws Exception {
        assertEquals(true, budget.getmName().equals("Test Budget"));
    }

    @Test
    public void setmName() throws Exception {
        budget.setmName("New Budget Name");
        assertEquals(true, budget.getmName().equals("New Budget Name"));
    }

    @Test
    public void getmDueTime() throws Exception {
        assertEquals(true, budget.getmDueTime() == 1509494400L);
    }

    @Test
    public void setmDueTime() throws Exception {
        budget.setmDueTime(1509580800L);
        assertEquals(true, budget.getmDueTime() == 1509580800L);
    }

    @Test
    public void getmPeriod() throws Exception {
        assertEquals(true, budget.getmPeriod() == 10);
    }

    @Test
    public void setmPeriod() throws Exception {
        budget.setmPeriod(5);
        assertEquals(true, budget.getmPeriod() == 5);
    }
}