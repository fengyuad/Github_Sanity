package Model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

/**
 * Created by Yifan on 10/13 013.
 */

public class Model implements Serializable{
    protected String mUserID = "60WQP281jXVqW57q3JP6vQeMeq73";
    protected transient DatabaseReference mDatabase;
    protected double mThreshold = 0.75;
    protected int mFrequency = 1;

    /**
     * Getter and Setter
     */
    public double getmThreshold() {
        return mThreshold;
    }

    public void setmThreshold(double mThreshold) {
        this.mThreshold = mThreshold;
    }

    public int getmFrequency() {
        return mFrequency;
    }

    public void setmFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }
}
