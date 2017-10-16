package Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Yifan on 10/13 013.
 */

public class StorageModel {
    private static StorageModel mInstance = null;
    private static Context mContext;
    private FileOutputStream fos;
    private FileInputStream fis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public StorageModel() {
    }

    public static StorageModel GetInstance() {
        if (mInstance == null) {
            mInstance = new StorageModel();
        }
        return mInstance;
    }

    public static void SetContext(Context context) {
        mContext = context;
    }

    public boolean DeleteFiles() {
        File file = mContext.getFileStreamPath("SanityBudgetModel.dat");
        if (!file.delete())
            return false;
        file = mContext.getFileStreamPath("SanityCategoryModel.dat");
        if (!file.delete())
            return false;
        file = mContext.getFileStreamPath("SanityTransactionModel.dat");
        if (!file.delete())
            return false;
        return true;
    }

    public boolean FilesExist() {
        File file = mContext.getFileStreamPath("SanityBudgetModel.dat");
        if (!file.exists())
            return false;
        file = mContext.getFileStreamPath("SanityCategoryModel.dat");
        if (!file.exists())
            return false;
        file = mContext.getFileStreamPath("SanityTransactionModel.dat");
        if (!file.exists())
            return false;
        return true;
    }

    public boolean SaveAll() {
        if (!SaveObject(BudgetModel.GetInstance()))
            return false;
        if (!SaveObject(CategoryModel.GetInstance()))
            return false;
        if (!SaveObject(TransactionModel.GetInstance()))
            return false;
        return true;
    }

    public void ReadAll() {
        BudgetModel.UpdateInstance((BudgetModel) ReadObject("BudgetModel"));
        BudgetModel.UpdateInstance((BudgetModel) ReadObject("CategoryModel"));
        BudgetModel.UpdateInstance((BudgetModel) ReadObject("TransactionModel"));
    }

    public <E extends Model> boolean SaveObject(E model) {
        try {
            fos = mContext.openFileOutput("Sanity" + model.getClass().getSimpleName() + ".dat", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            oos.close();
            fos.close();
            Log.d("DEBUG", "Object Saved");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <E extends Model> E ReadObject(String className) {
        try {
            E currModel;
            fis = mContext.openFileInput("Sanity" + className + ".dat");
            ois = new ObjectInputStream(fis);
            currModel = (E) ois.readObject();
            ois.close();
            fis.close();
            Log.d("DEBUG", "Object Read");
            return currModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
