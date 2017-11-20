package Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Vector;

import Controller.OnGetDataListener;

/**
 * Created by zhongchu on 10/12/17.
 */

public class TransactionModel extends Model implements java.io.Serializable {
    private static TransactionModel instance = null;
    private Map<Long, Transaction> mTransactions;
    private Map<Long, Transaction> mAutoOnly;
    private String mUserID;

    private TransactionModel() {
        super();
        mTransactions = new HashMap<>();
        mAutoOnly = new HashMap<>();
        InitDataBase();
    }

    public void InitDataBase() {
        mUserID = Variable.GetInstance().getmUserID();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUserID).child("transaction");
        //mDatabase = FirebaseDatabase.getInstance().getReference().child(mUserID).child("transaction");
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

    public Vector<Transaction> getmAutoOnly() {
        Vector<Transaction> toReturn = new Vector<>();
        for(Long l : mAutoOnly.keySet()){
            toReturn.add(mAutoOnly.get(l));
        }
        return toReturn;
    }

    public Transaction addTransaction(Transaction trans) {
        if(trans.getmisAuto() == true){
            WriteNewTransaction(trans);
            mAutoOnly.put(trans.getmTransactionId(), trans);
        }
        else{
            WriteNewTransaction(trans);
            CategoryModel CModel = CategoryModel.GetInstance();
            CModel.AddTransactionIDToCategoryAndUpdateDatabase(trans.getmCategoryId(), trans.getmTransactionId(), trans.getmAmount());
            Variable.GetInstance().setmUpdateTime(System.currentTimeMillis());
            mTransactions.put(trans.getmTransactionId(), trans);
        }
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
                mAutoOnly.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Transaction trans = ds.getValue(Transaction.class);
                    if(trans.getmisAuto() == true){
                        mAutoOnly.put(trans.getmTransactionId(), trans);
                    }
                    else{
                        mTransactions.put(trans.getmTransactionId(), trans);
                    }
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

    void updateMonth(Transaction trans){
        if(trans.getmMonth() == 12) {
            trans.setmMonth(1);
            trans.setmYear(trans.getmYear() + 1);
        }
        else{
            trans.setmMonth(trans.getmMonth() + 1);
        }
        for(Transaction t : mAutoOnly.values()) {
            if(trans.equals(t)) {
                if(t.getmMonth() == 12) {
                    t.setmMonth(1);
                    t.setmYear(t.getmYear() + 1);
                }
                else{
                    t.setmMonth(t.getmMonth() + 1);
                }
            }
        }
        WriteNewTransaction(trans);
    }

    public Map<Long, Transaction> SelectTransactions(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
        Map<Long, Transaction> toReturn = new HashMap<>();
        Log.d("MyTestRiqi", "" + "" + fromYear + " " + fromMonth + " " + fromDay);
        Log.d("MyTestRiqi", "" + "" + toYear + " " + toMonth + " " + toDay);
        Log.d("MapSize", "" + mTransactions.size());
        for (Long key : mTransactions.keySet()) {
            Log.d("Mapitem", "" + mTransactions.get(key).getmYear() + " " + mTransactions.get(key).getmMonth() + " " + mTransactions.get(key).getmDay());
            if ((mTransactions.get(key).getmYear() > fromYear && mTransactions.get(key).getmYear() < toYear)) {
                toReturn.put(key, mTransactions.get(key));
            } else if (mTransactions.get(key).getmYear() == fromYear || mTransactions.get(key).getmYear() == toYear) {
                if (mTransactions.get(key).getmMonth() > fromMonth && mTransactions.get(key).getmMonth() < toMonth) {
                    toReturn.put(key, mTransactions.get(key));
                } else if (mTransactions.get(key).getmMonth() == fromMonth || mTransactions.get(key).getmMonth() == toMonth) {
                    if (mTransactions.get(key).getmMonth() == fromMonth && mTransactions.get(key).getmMonth() == toMonth){
                        if(mTransactions.get(key).getmDay() >= fromDay && mTransactions.get(key).getmDay() <= toDay){
                            toReturn.put(key, mTransactions.get(key));
                        }
                    }
                    else if (mTransactions.get(key).getmDay() >= fromDay && mTransactions.get(key).getmMonth() == fromMonth) {
                        toReturn.put(key, mTransactions.get(key));
                    }
                    else if (mTransactions.get(key).getmDay() <= toDay && mTransactions.get(key).getmMonth() == toMonth){
                        toReturn.put(key, mTransactions.get(key));
                    }
                }
            }
        }
        Log.d("returnsize", "" + toReturn.size());
        return toReturn;
    }

    public List<Double> analyzeMonthlySpend(){
        List<Double> list = new ArrayList<>();

        for(int i = 1; i <= 12; i++) {

            Map<Long, Transaction> map = SelectTransactions(2017, i, 1, 2017, i, 30);
            double monthSum = 0;
            for (Transaction t : map.values()) {
                monthSum += t.getmAmount();
            }
            list.add(monthSum);
        }

        // get rid of leading zeros
        int index = 0;
        for(; index < list.size(); index++){
            if(list.get(index) != 0) break;
        }
        list = list.subList(index, list.size());

        return list;
    }

    public List<Double> analyzeDaylySpend() throws ParseException {
        List<Double> list = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = formatter.parse("2017-01-01");
        Date endDate = new Date();
        System.out.println(formatter.format(endDate)); // test

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            // Do your job here with `date`.
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            double sum = 0;
            for(Transaction t: SelectTransactions(year, month, day, year, month, day).values()){
                sum += t.getmAmount();
            }
            list.add(sum);
        }

        // get rid of leading zeros
        int index = 0;
        for(; index < list.size(); index++){
            if(list.get(index) != 0) break;
        }
        list = list.subList(index, list.size());

        return list;
    }

    public List<Date> generateDayLabel() throws ParseException{
        List<Date> list = new ArrayList<>();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = formatter.parse("2017-01-01");
        Date endDate = new Date();
        System.out.println(formatter.format(endDate)); // test

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            // Do your job here with `date`.
            list.add(date);
        }

        List<Double> spendList = analyzeDaylySpend();
        list = list.subList(list.size() - spendList.size(), list.size());

        return list;
    }



    public List<Date> generateMonthLabel(){

        List<Date> list = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse("2017-01-01");
            list.add(date);
            date = formatter.parse("2017-02-01");
            list.add(date);
            date = formatter.parse("2017-03-01");
            list.add(date);
            date = formatter.parse("2017-04-01");
            list.add(date);
            date = formatter.parse("2017-05-01");
            list.add(date);
            date = formatter.parse("2017-06-01");
            list.add(date);
            date = formatter.parse("2017-07-01");
            list.add(date);
            date = formatter.parse("2017-08-01");
            list.add(date);
            date = formatter.parse("2017-09-01");
            list.add(date);
            date = formatter.parse("2017-10-01");
            list.add(date);
            date = formatter.parse("2017-11-01");
            list.add(date);
            date = formatter.parse("2017-12-01");
            list.add(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Double> spendList = analyzeMonthlySpend();
        list = list.subList(list.size() - spendList.size(), list.size());

        return list;

    }

    public List<Date> generateWeekLabel(){
        List<Date> list = new ArrayList<>();
        String startDate = "2017-01-01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = formatter.parse(startDate);
            Date end = new Date();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(start);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(end);

            for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 7), date = startCal.getTime()) {
                // Do your job here with `date`.
                list.add(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Double> spendList = analyzeWeeklySpend();
        list = list.subList(list.size() - spendList.size(), list.size());

        return list;
    }

    public List<Double> analyzeWeeklySpend(){
        List<Double> list = new ArrayList<>();

        int startWeek;
        int finishWeek;
        int diff;
        SimpleDateFormat sdf;
        Calendar cal;
        Calendar startCountingCal;
        Date startDate;
        Date finishDate = new Date();
        String startDateS = "01/01/2017";

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            startDate = sdf.parse(startDateS);
            cal = Calendar.getInstance();

            cal.setTime(startDate);
            startWeek = cal.get(Calendar.WEEK_OF_YEAR);

            cal.setTime(finishDate);
            finishWeek = cal.get(Calendar.WEEK_OF_YEAR);
            diff = finishWeek - startWeek;

            startCountingCal = Calendar.getInstance();
            startCountingCal.setTime(startDate);
            for (int i = 0; i <= diff; i++) {
                int startYear = startCountingCal.get(Calendar.YEAR);
                int startMonth = startCountingCal.get(Calendar.MONTH);
                int startDay = startCountingCal.get(Calendar.DAY_OF_MONTH);
                startCountingCal.add(Calendar.DATE, 7);
                int endYear = startCountingCal.get(Calendar.YEAR);
                int endMonth = startCountingCal.get(Calendar.MONTH);
                int endDay = startCountingCal.get(Calendar.DAY_OF_MONTH);

                double sum = 0;
                for(Transaction t: SelectTransactions(startYear, startMonth, startDay, endYear, endMonth, endDay).values()){
                    sum += t.getmAmount();
                }
                list.add(sum);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // get rid of leading zeros
        int index = 0;
        for(; index < list.size(); index++){
            if(list.get(index) != 0) break;
        }
        list = list.subList(index, list.size());


        return list;
    }

    public double totalSpending(){
        double ret = 0;
        for(Transaction t: mTransactions.values()){
            ret += t.getmAmount();
        }
        return ret;
    }
}
