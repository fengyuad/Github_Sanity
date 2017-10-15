package Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;
import android.util.Log;

/**
 * Created by zhongchu on 10/12/17.
 */

public class TransactionModel extends Model implements java.io.Serializable{
    private static TransactionModel instance = null;
    private Map<Long, Transaction> mTransactions;
    private DatabaseReference mDatabase;

    public Map<Long, Transaction> getmTransactions(){
        return mTransactions;
    }
    private TransactionModel() {
        super();
        mTransactions = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(mUserID + "/transaction");
    }

    public static TransactionModel GetInstance() {
        if (instance == null) {
            instance = new TransactionModel();
        }
        return instance;
    }

    public Transaction addTransaction(Transaction trans) {
        Long transactionId = System.currentTimeMillis() / 1000;
        mTransactions.put(transactionId, trans);
        return trans;
    }

    public Transaction UpdateTransaction(long transactionId, double amount, long categoryId) {
        mTransactions.get(transactionId).setmAmount(amount);
        mTransactions.get(transactionId).setmCategoryId(categoryId);
        return mTransactions.get(transactionId);
    }

    public Transaction DeleteTransaction(long transactionId) {
        Transaction temp = mTransactions.get(transactionId);
        mTransactions.remove(transactionId);
        return temp;
    }

    public boolean WriteNewTransaction(Transaction trans){
        Long key = System.currentTimeMillis()/1000;
        trans.setmTransactionId(key);
        addTransaction(trans);
        mDatabase.child(key.toString()).setValue(trans);
        return true;
    }

    public void ReadTransaction(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Transaction trans = ds.getValue(Transaction.class);
                    mTransactions.put(trans.getmTransactionId(), trans);
                }
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
