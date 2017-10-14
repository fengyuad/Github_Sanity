package Model;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import Model.Transaction;

/**
 * Created by zhongchu on 10/12/17.
 */

public class TransactionModel {
    private Map<Long, Transaction> Transactions = new HashMap<>();;
    private static TransactionModel instance = null;
    private TransactionModel(){}
    public static TransactionModel GetInstance(){
        if(instance == null){
            instance = new TransactionModel();
        }
        return instance;
    }
    public void addTransaction(double amount, long categoryId, String notes){
        Long transactionId = System.currentTimeMillis()/1000;
        Transaction toAdd = new Transaction(amount, categoryId, transactionId, notes);
        Transactions.put(transactionId, toAdd);
    }
    public void UpdateTransaction(long transactionId, double amount, long categoryId){
        Transactions.get(transactionId).SetmAmount(amount);
        Transactions.get(transactionId).SetmCategoryId(categoryId);
    }
    public void DeleteTransaction(long transactionId){
        Transactions.remove(transactionId);
    }
}
