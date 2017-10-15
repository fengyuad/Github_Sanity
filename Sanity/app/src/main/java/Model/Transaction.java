package Model;

/**
 * Created by zhongchu on 10/12/17.
 */

public class Transaction implements java.io.Serializable{
    public double getmAmount() {
        return mAmount;
    }

    public long getmCategoryId() {
        return mCategoryId;
    }

    public long getmTransactionId() {
        return mTransactionId;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public void setmCategoryId(long mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public void setmTransactionId(long mTransactionId) {
        this.mTransactionId = mTransactionId;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

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

    public Transaction(){

    }
}
