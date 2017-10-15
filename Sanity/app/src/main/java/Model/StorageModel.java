package Model;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Yifan on 10/13 013.
 */

public class StorageModel {
    FileOutputStream fos;
    FileInputStream fis;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Context mContext;

    public StorageModel(Context context) {
        mContext = context;
    }

    public void SaveAll() {
        //SaveObject();
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
