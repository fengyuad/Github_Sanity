package Model;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by zhongchu on 10/26/17.
 */
public class TransactionModelTest {
    private TransactionModel tModel;
    private Transaction t = new Transaction(100.0, 150080, "this is a test transaction", 2017, 10, 26);
    @Before
    public void setUp() throws Exception {
        tModel = TransactionModel.GetInstance();
    }

    @Test
    public void getmTransactions() throws Exception {
        assertEquals(TransactionModel.GetInstance(), tModel);
    }

}