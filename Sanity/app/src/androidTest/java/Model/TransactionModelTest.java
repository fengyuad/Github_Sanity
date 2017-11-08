package Model;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.util.concurrent.ThreadFactory;

import android.util.Log;

import static org.junit.Assert.*;

/**
 * Created by zhongchu on 10/26/17.
 */
public class TransactionModelTest {
    private TransactionModel tModel;
    private Transaction t1 = new Transaction(100.0, 150080, "this is a test transaction", 2017, 10, 26);
    private Transaction t2 = new Transaction(100.0, 150080, "this is a test transaction", 2017, 10, 27);
    private Transaction t3 = new Transaction(100.0, 150080, "this is a test transaction", 2017, 10, 28);
    private Transaction t4 = new Transaction(100.0, 150080, "this is a test transaction", 2017, 10, 29);
    @Before
    public void setUp() throws Exception {
        tModel = TransactionModel.GetInstance();
    }

    @Test
    public void getmTransactions() throws Exception {
        assertEquals(TransactionModel.GetInstance(), tModel);
    }

    @Test
    public void addTransaction(){
        tModel.getmTransactions().put(System.currentTimeMillis(), t1);
        assertEquals(1, tModel.getmTransactions().size());
    }

    @Test
    public void SelectTransaction() throws Exception{
        tModel.getmTransactions().put(System.currentTimeMillis(), t2);
        Thread.sleep(1000);
        tModel.getmTransactions().put(System.currentTimeMillis(), t3);
        Thread.sleep(1000);
        tModel.getmTransactions().put(System.currentTimeMillis(), t4);
        Thread.sleep(1000);
        Map<Long, Transaction> test = tModel.SelectTransactions(2017, 10, 26, 2017, 11, 1);
        if(tModel.SelectTransactions(2017, 10, 26, 2017, 11, 1).size() == 3){
            assertEquals(3, test.size());
        }
        if(tModel.SelectTransactions(2017, 10, 26, 2017, 11, 1).size() == 4){
            assertEquals(4, test.size());
        }
    }

    @Test
    public void SingletonTest() throws Exception{
        TransactionModel tModel2 = TransactionModel.GetInstance();
        assertEquals(tModel.getmTransactions().size(), tModel2.getmTransactions().size());
    }

}