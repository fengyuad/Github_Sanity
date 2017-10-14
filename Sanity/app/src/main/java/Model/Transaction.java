package Model;

/**
 * Created by zhongchu on 10/12/17.
 */

public class Transaction {
    private double mAmount;
    private long mCategoryId;
    private long mTransactionId;
    private String mNotes;

    public Transaction(double amount, long categoryId, long transID, String notes){
        mAmount = amount;
        mCategoryId = categoryId;
        mTransactionId = transID;
        mNotes = notes;
    }

    public double GetAmount(){
        return mAmount;
    }

    public void SetmAmount(double amount){
        mAmount = amount;
    }

    public long GetCategoryId(){
        return mCategoryId;
    }

    public void SetmCategoryId(long categoryId){
        mCategoryId = categoryId;
    }

    public long GetTransactionId(){
        return mTransactionId;
    }

    public void SetmTransactionId(long transID){
        mTransactionId = transID;
    }

    public String GetNotes() { return mNotes; }

    public void SetmNotes(String notes) { mNotes = notes; }
}
