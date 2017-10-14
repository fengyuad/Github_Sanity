package Model;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Yifan on 10/13 013.
 */

public class StorageModel extends Activity {
    private File outFile;
    private ObjectOutputStream oos;

    StorageModel() {
        outFile = new File(getCacheDir(), "Sanity.dat");
    }

    public <E extends BudgetModel> boolean SaceObject(E model) {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(outFile));
            oos.writeObject(model);
            oos.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }
}
