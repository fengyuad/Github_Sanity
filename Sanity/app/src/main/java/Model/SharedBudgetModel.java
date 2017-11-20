package Model;

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

    }

    public static SharedBudgetModel GetInstance(){
        if(mInstance == null){
            mInstance = new SharedBudgetModel();
        }
        return mInstance;
    }

    
}
