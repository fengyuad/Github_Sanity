package Model;

import com.google.firebase.database.FirebaseDatabase;

import java.util.*;

/**
 * Created by EricWang on 11/19/17.
 */

public class SharedBudgetModel extends Model {
    private static SharedBudgetModel mInstance = null;
    private Map<Long, SharedBudget> mIDTOSharedBudget;


    private SharedBudgetModel(){
        super();
        mIDTOSharedBudget = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SharedBudget");

    }

    public static SharedBudgetModel GetInstance(){
        if(mInstance == null){
            mInstance = new SharedBudgetModel();
        }
        return mInstance;
    }

    public void AddSharedUserToBudget(long userID, long budgetID){
        mIDTOSharedBudget.get(budgetID).AddSharedUser(userID);
    }

    public SharedBudget GetSharedBudgetByID(long budgetID){
        return mIDTOSharedBudget.get(budgetID);
    }

    public SharedBudget CreateNewSharedBudget(String name, double amountLimit, long mDueTime, int period){
        SharedBudget sb = new SharedBudget();
        sb.setmName(name);
        sb.setmAmountLimit(amountLimit);
        sb.setmDueTime(mDueTime);
        sb.setmBudgetID(System.currentTimeMillis());
        sb.setmPeriod(period);
        mIDTOSharedBudget.put(sb.getmBudgetID(), sb);
        return sb;
    }

    public void RemoveSharedUserFromBudget(long userID, long budgetID){
        mIDTOSharedBudget.get(budgetID).RemoveSharedUser(userID);
    }

}

