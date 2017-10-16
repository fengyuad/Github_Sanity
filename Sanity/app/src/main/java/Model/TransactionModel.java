package Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongchu on 10/12/17.
 */

public class TransactionModel extends Model implements java.io.Serializable {
    private static TransactionModel instance = null;
    private Map<Long, Transaction> mTransactions;
    private DatabaseReference mDatabase;

    public Map<Long, Transaction> getmTransactions() {
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

        WriteNewTransaction(trans);
        CategoryModel CModel = CategoryModel.GetInstance();

        CModel.AddTransactionIDToCategoryAndUpdateDatabase(trans.getmCategoryId(), trans.getmTransactionId(), trans.getmAmount());


        mTransactions.put(trans.getmTransactionId(), trans);
        return trans;
    }

    //public

    public Transaction DeleteTransaction(Long transactionId, Boolean callCat) {
        if (callCat) {
            CategoryModel CModel = CategoryModel.GetInstance();
            CModel.RemoveTransactionInCategoryAndUpdateDatabase
                    (mTransactions.get(transactionId).getmCategoryId(), transactionId, mTransactions.get(transactionId).getmAmount());
        }
        mDatabase.child(transactionId.toString()).removeValue();
        Transaction temp = mTransactions.get(transactionId);
        mTransactions.remove(transactionId);
        return temp;
    }

    public boolean WriteNewTransaction(Transaction trans) {
        Long key = trans.getmTransactionId();
        //trans.setmTransactionId(key);
        //addTransaction(trans);
        mDatabase.child(key.toString()).setValue(trans);
        return true;
    }

    public void ReadTransaction() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Transaction trans = ds.getValue(Transaction.class);
                    mTransactions.put(trans.getmTransactionId(), trans);
                }
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Map<Long, Transaction> SelectTransactions(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
        Map<Long, Transaction> toReturn = new HashMap<>();
        for (Long key : mTransactions.keySet()) {
            if ((mTransactions.get(key).getmYear() > fromYear && mTransactions.get(key).getmYear() < toYear)) {
                toReturn.put(key, mTransactions.get(key));
            } else if (mTransactions.get(key).getmYear() == fromYear || mTransactions.get(key).getmYear() == toYear) {
                if (mTransactions.get(key).getmMonth() > fromMonth && mTransactions.get(key).getmMonth() < toMonth) {
                    toReturn.put(key, mTransactions.get(key));
                } else if (mTransactions.get(key).getmYear() == fromMonth || mTransactions.get(key).getmYear() == toMonth) {
                    if (mTransactions.get(key).getmDay() >= fromDay && mTransactions.get(key).getmDay() <= toDay) {
                        toReturn.put(key, mTransactions.get(key));
                    }
                }
            }
        }
        return toReturn;
    }
}
