package Model;

import com.example.tomdong.sanity.CustomTransactionCardAdapter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhongchu on 10/26/17.
 */
public class TransactionTest {
    private Transaction mTransaction;

    @Before
    public void setUp() throws Exception {
        mTransaction = new Transaction(100.0, 150080, "this is a test transaction", 2017, 10, 26);
    }

    @Test
    public void getmAmount() throws Exception {
        assertEquals(100.0, mTransaction.getmAmount(), 0);
    }

    @Test
    public void setmAmount() throws Exception {
        mTransaction.setmAmount(60.0);
        assertEquals(60.0, mTransaction.getmAmount(), 0);
    }

    @Test
    public void getmCategoryId() throws Exception {
        assertEquals(150080, mTransaction.getmCategoryId());
    }

    @Test
    public void setmCategoryId() throws Exception {
        mTransaction.setmCategoryId(150001);
        assertEquals(150001, mTransaction.getmCategoryId());
    }

    @Test
    public void getmNotes() throws Exception {
        assertEquals("this is a test transaction", mTransaction.getmNotes());
    }

    @Test
    public void setmNotes() throws Exception {
        mTransaction.setmNotes("this is a modified note");
        assertEquals("this is a modified note", mTransaction.getmNotes());
    }

    @Test
    public void getmYear() throws Exception {
        assertEquals(2017, mTransaction.getmYear());
    }

    @Test
    public void setmYear() throws Exception {
        mTransaction.setmYear(2046);
        assertEquals(2046, mTransaction.getmYear());
    }

    @Test
    public void getmMonth() throws Exception {
        assertEquals(10, mTransaction.getmMonth());
    }

    @Test
    public void setmMonth() throws Exception {
        mTransaction.setmMonth(1);
        assertEquals(1, mTransaction.getmMonth());
    }

    @Test
    public void getmDay() throws Exception {
        assertEquals(26, mTransaction.getmDay());
    }

    @Test
    public void setmDay() throws Exception {
        mTransaction.setmDay(31);
        assertEquals(31, mTransaction.getmDay());
    }

}