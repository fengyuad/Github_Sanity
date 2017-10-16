package Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.text.DateFormat;
import java.util.*;
import android.util.Log;
import java.sql.Date;

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
        //Long transactionId = System.currentTimeMillis() / 1000;
        CategoryModel CModel = CategoryModel.GetInstance();
        CModel.AddTransactionIDToCategoryAndUpdateDatabase(trans.getmCategoryId(), trans.getmTransactionId(), trans.getmAmount());
        WriteNewTransaction(trans);
        mTransactions.put(trans.getmTransactionId(), trans);
        return trans;
    }

    public Transaction UpdateTransaction(long transactionId, double amount, long categoryId) {
        mTransactions.get(transactionId).setmAmount(amount);
        mTransactions.get(transactionId).setmCategoryId(categoryId);
        return mTransactions.get(transactionId);
    }

    //public

    public Transaction DeleteTransaction(Long transactionId, Boolean callCat) {
        if(callCat) {
            CategoryModel CModel = CategoryModel.GetInstance();
            CModel.RemoveTransactionInCategoryAndUpdateDatabase
                    (mTransactions.get(transactionId).getmCategoryId(), transactionId, mTransactions.get(transactionId).getmAmount());
        }
        mDatabase.child(transactionId.toString()).removeValue();
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

    public Map<Long, Transaction> SelectTransactions(Timestamp from, Timestamp to){
        Map<Long, Transaction> toReturn = new HashMap<>();
        for(Long key : mTransactions.keySet()){
            if(key >= from.getTimestamp().getTime() && key <= to.getTimestamp().getTime()){
                toReturn.put(key, mTransactions.get(key));
            }
        }
        return toReturn;
    }
}
