package Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

import Controller.OnGetDataListener;

/**
 * Created by zhongchu on 10/12/17.
 */

public class TransactionModel extends Model implements java.io.Serializable {
    private static TransactionModel instance = null;
    private Map<Long, Transaction> mTransactions;
    private String mUserID;

    private TransactionModel() {
        super();
        mTransactions = new HashMap<>();
        InitDataBase();

    }

    public void InitDataBase() {
        mUserID = Variable.GetInstance().getmUserID();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUserID).child("transaction");
    }

    public static TransactionModel GetInstance() {
        if (instance == null) {
            instance = new TransactionModel();
        }
        return instance;
    }


    /**
     * When loading from local storage, a new instance should be updated
     *
     * @param tm a <b>TransactionModel</b> instance
     */
    public static void UpdateInstance(TransactionModel tm) {
        instance = tm;
    }

    public Map<Long, Transaction> getmTransactions() {
        return mTransactions;
    }

    public Transaction addTransaction(Transaction trans) {

        WriteNewTransaction(trans);
        CategoryModel CModel = CategoryModel.GetInstance();
        CModel.AddTransactionIDToCategoryAndUpdateDatabase(trans.getmCategoryId(), trans.getmTransactionId(), trans.getmAmount());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        mTransactions.put(trans.getmTransactionId(), trans);
        return trans;
    }

    //public

    public Transaction DeleteTransaction(Long transactionId, Boolean callCat) {
        if (callCat) {
            CategoryModel CModel = CategoryModel.GetInstance();
            CModel.RemoveTransactionInCategoryAndUpdateDatabase
                    (mTransactions.get(transactionId).getmCategoryId(), transactionId, mTransactions.get(transactionId).getmAmount());
            Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        }
        mDatabase.child(transactionId.toString()).removeValue();
        Transaction temp = mTransactions.get(transactionId);
        mTransactions.remove(transactionId);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        return temp;
    }

    public boolean WriteNewTransaction(Transaction trans) {
        Long key = trans.getmTransactionId();
        //trans.setmTransactionId(key);
        //addTransaction(trans);
        mDatabase.child(key.toString()).setValue(trans);
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
        return true;
    }

    public void ReadTransaction(final OnGetDataListener listener) {
        listener.onStart();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTransactions.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Transaction trans = ds.getValue(Transaction.class);
                    mTransactions.put(trans.getmTransactionId(), trans);
                }
                listener.onSuccess(dataSnapshot);
            }
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
        FirebaseDatabase.getInstance().getReference().child(mUserID).child("update").setValue(System.currentTimeMillis());
        Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
    }

    public Map<Long, Transaction> SelectTransactions(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
        Map<Long, Transaction> toReturn = new HashMap<>();
        Log.d("MyTestRiqi", "" + "" + fromYear + " " + fromMonth + " " + fromDay);
        Log.d("MyTestRiqi", "" + "" + toYear + " " + toMonth + " " + toDay);
        Log.d("MapSize", "" + mTransactions.size());
        for (Long key : mTransactions.keySet()) {
            Log.d("Mapitem", "" + mTransactions.size());
            if ((mTransactions.get(key).getmYear() > fromYear && mTransactions.get(key).getmYear() < toYear)) {
                toReturn.put(key, mTransactions.get(key));
            } else if (mTransactions.get(key).getmYear() == fromYear || mTransactions.get(key).getmYear() == toYear) {
                if (mTransactions.get(key).getmMonth() > fromMonth && mTransactions.get(key).getmMonth() < toMonth) {
                    toReturn.put(key, mTransactions.get(key));
                } else if (mTransactions.get(key).getmMonth() == fromMonth || mTransactions.get(key).getmMonth() == toMonth) {
                    if (mTransactions.get(key).getmDay() >= fromDay && mTransactions.get(key).getmDay() <= toDay) {
                        toReturn.put(key, mTransactions.get(key));
                    }
                }
            }
        }
        Log.d("returnsize", "" + toReturn.size());
        return toReturn;
    }
}
