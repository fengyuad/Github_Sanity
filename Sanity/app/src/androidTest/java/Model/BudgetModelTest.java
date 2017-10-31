package Model;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yifan on 10/30 030.
 */
public class BudgetModelTest {
    BudgetModel bm;

    @Before
    public void setUp() throws Exception {
        Variable.GetInstance().setmUserID("1qQKDhks1GhFt1nzzTz6a1kPZqF3");
        bm = BudgetModel.GetInstance();
    }

    @Test
    public void getBudgetMap() throws Exception {
        bm.GetBudgetMap().isEmpty();
    }

    @Test
    public void deleteAllBudgets() throws Exception {
        bm.DeleteAllBudgets();
        assertEquals(true, bm.GetBudgetMap().size() == 0);
    }

    @Test
    public void addBudget() throws Exception {
        bm.DeleteAllBudgets();
        bm.AddBudget(new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>()));
        assertEquals(true, bm.GetBudgetMap().size() == 1);
    }

    @Test
    public void deleteBudget() throws Exception {
        bm.DeleteAllBudgets();
        Budget budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
        bm.AddBudget(budget);
        bm.DeleteBudget(budget.getmBudgetId());
        assertEquals(true, bm.GetBudgetMap().isEmpty());
    }

    @Test
    public void getBudgetById() throws Exception {
        bm.DeleteAllBudgets();
        Budget budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
        bm.AddBudget(budget);
        assertEquals(true, bm.getBudgetById(budget.getmBudgetId()) == budget);
    }

    @Test
    public void getCategoriesUnderBudget() throws Exception {
        bm.DeleteAllBudgets();
        Category category = new Category("Test Category", 100.0, -1L);
        category.setmID(1508976000L);
        Budget budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
        budget.AddCatId(category.getmID());
        bm.AddBudget(budget);
        assertEquals(true, bm.getCategoriesUnderBudget(budget.getmBudgetId()).size() == 1);
    }

    @Test
    public void addACategory() throws Exception {
        bm.DeleteAllBudgets();
        Category category = new Category("Test Category", 100.0, -1L);
        category.setmID(1508976000L);
        Budget budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
        bm.AddBudget(budget);
        assertEquals(true, bm.getCategoriesUnderBudget(budget.getmBudgetId()).size() == 0);
        bm.AddACategory(budget.getmBudgetId(), 1508976000L);
        assertEquals(true, bm.getCategoriesUnderBudget(budget.getmBudgetId()).size() == 1);
    }

    @Test
    public void removeACategory() throws Exception {
        bm.DeleteAllBudgets();
        Category category = new Category("Test Category", 100.0, -1L);
        category.setmID(1508976000L);
        Budget budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
        bm.AddBudget(budget);
        bm.AddACategory(budget.getmBudgetId(), 1508976000L);
        assertEquals(true, bm.getCategoriesUnderBudget(budget.getmBudgetId()).size() == 1);
        bm.RemoveACategory(budget.getmBudgetId(), 1508976000L);
        assertEquals(true, bm.getCategoriesUnderBudget(budget.getmBudgetId()).size() == 0);
    }

    @Test
    public void updateBudget() throws Exception {
        bm.DeleteAllBudgets();
        Budget budget = new Budget("Test Budget", 1509494400L, 10, new ArrayList<Long>());
        Category category = new Category("Test Category", 100.0, -1L);
        category.setmID(1508976000L);
        bm.AddBudget(budget);
        bm.AddACategory(budget.getmBudgetId(), 1508976000L);
        bm.UpdateBudget(budget.getmBudgetId(), "New Name");
        assertEquals(true, bm.getBudgetById(budget.getmBudgetId()).getmName().equals("New Name"));
        bm.UpdateBudget(budget.getmBudgetId(), 1509580800L, 5);
        assertEquals(true, bm.getBudgetById(budget.getmBudgetId()).getmDueTime() == 1509580800L);
        assertEquals(true, bm.getBudgetById(budget.getmBudgetId()).getmPeriod() == 5);
        bm.UpdateBudget(budget.getmBudgetId(), new ArrayList<Long>());
        assertEquals(true, bm.getCategoriesUnderBudget(budget.getmBudgetId()).size() == 0);
    }
}