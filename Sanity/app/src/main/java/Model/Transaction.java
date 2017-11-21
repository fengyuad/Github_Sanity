package Model;

/**
 * Created by zhongchu on 10/12/17.
 */

public class Transaction implements java.io.Serializable {

    private double mAmount;
    private long mCategoryId;
    private long mTransactionId;
    private String mNotes = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean misAuto;

    public Transaction(double amount, long categoryId, String notes, int year, int month, int day, boolean auto) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mAmount = amount;
        mCategoryId = categoryId;
        mTransactionId = System.currentTimeMillis();
        mNotes = notes;
        misAuto = auto;
    }

    public Transaction() {

    }
    public boolean getmisAuto() { return misAuto; }

    public void setmAuto(boolean mAuto) { this.misAuto = mAuto; }

    public double getmAmount() {
        return mAmount;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public long getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(long mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public long getmTransactionId() {
        return mTransactionId;
    }

    public void setmTransactionId(long mTransactionId) {
        this.mTransactionId = mTransactionId;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }
}
