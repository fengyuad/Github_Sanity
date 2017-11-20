package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import Model.Budget;
import Model.BudgetModel;
import Model.Category;
import Model.CategoryModel;
import Model.Transaction;
import Model.TransactionModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PieChart pieChart;
    ListView Budget_ListView;
    View myFragmentView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TextView transDateText;
    private int transYear, transMonth, transDay;
    private Map<PieEntry, Long> pieMap = new HashMap<>();
    private FloatingActionButton scan;
    String mCurrentPhotoPath;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Vector<Transaction> autoTrans = TransactionModel.GetInstance().getmAutoOnly();
        Calendar cal = Calendar.getInstance();
        int currDay = cal.get(Calendar.DAY_OF_MONTH);
        int currMonth = cal.get(Calendar.MONTH);
        int currYear = cal.get(Calendar.YEAR);
        for(Transaction t: autoTrans)
        {
            if(t.getmYear() < currYear || (t.getmYear() == currYear && t.getmMonth() < currMonth) ||
                    (t.getmYear() == currYear && t.getmMonth() == currMonth && t.getmDay() <= currDay))
            {
                TransactionModel.GetInstance().addTransaction(
                        new Transaction(t.getmAmount(),
                                t.getmCategoryId(),
                                "Auto transaction",
                                t.getmYear(),
                                t.getmMonth(),
                                t.getmDay(),
                                false));
                TransactionModel.GetInstance().updateMonth(t);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.activity_overview, container, false);
        // Budget_ListView = myFragmentView.findViewById(R.id.Budget_listview);
        final Calendar c = Calendar.getInstance();
        transDay = c.get(Calendar.DAY_OF_MONTH);
        transMonth = c.get(Calendar.MONTH);
        transYear = c.get(Calendar.YEAR);
        pieChart = (PieChart) myFragmentView.findViewById(R.id.overview_pie);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);
        //pieChart.getDescription().setText("Budgets OverView");

        ArrayList<PieEntry> yvalues = new ArrayList<>();
        Map<Long, Budget> budgetMap = BudgetModel.GetInstance().GetBudgetMap();
        for (Budget budget : budgetMap.values()) {
            PieEntry e = new PieEntry((float) budget.GetAmountLimit(), budget.getmName());
            yvalues.add(e);
            pieMap.put(e, budget.getmBudgetId());
        }
/*
        ArrayList<PieEntry> yvalues= new ArrayList<>();
        yvalues.add(new PieEntry(100f,"PartyA"));
        yvalues.add(new PieEntry(100f,"USA"));
        yvalues.add(new PieEntry(100f,"China"));
        yvalues.add(new PieEntry(100f,"Japan"));
        yvalues.add(new PieEntry(23f,"Russia"));
*/

        PieDataSet dataSet = new PieDataSet(yvalues, "Budgets");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);
        pieChart.animateY(1000);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Log.i("VAL SELECTED",
//                        "Value: " + BudgetModel.GetInstance().getBudgetById( Long.valueOf(((PieEntry) e).getLabel()) ).getmName()+ ", index: " + h.getX()
//                                + ", DataSet index: " + h.getDataSetIndex());
                long bgtID = pieMap.get(e);
                //Log.d("Budget ID", Long.toString(bgtID));
                Intent i = new Intent(getContext(), BudgetViewActivity.class);
                i.putExtra("bgtID", bgtID);
                startActivity(i);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        FloatingActionButton fab2=(FloatingActionButton) myFragmentView.findViewById(R.id.fab_multi_trans);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),AddMultiTrasactionActivity.class);
                startActivity(i);
            }
        });
        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    EditText transAmount;
    EditText transNote;
    ProgressBar progressBar;
    protected void showInputDialog() {

        // get input_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext()).setView(promptView);

        transAmount = (EditText) promptView.findViewById(R.id.trans_amt);
        transNote = (EditText) promptView.findViewById((R.id.trans_note));
        Button transDateButton = (Button) promptView.findViewById(R.id.trans_date_button);
        transDateText = (TextView) promptView.findViewById(R.id.trans_date_text);
        transDateText.setText(transYear + "-" + (transMonth + 1) + "-" + transDay);
        transDateButton.setOnClickListener(this);
        final Spinner bgtSpinner = (Spinner) promptView.findViewById(R.id.bgt_spinner);
        final Spinner catSpinner = (Spinner) promptView.findViewById(R.id.cat_spinner);
        scan=(FloatingActionButton)promptView.findViewById(R.id.fab_scan);
        progressBar=(ProgressBar)promptView.findViewById(R.id.progressbar_recept_scanning);
        progressBar.setVisibility(View.GONE);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        Map<Long, Budget> bgtMap = BudgetModel.GetInstance().GetBudgetMap();
        Map<Long, Category> catMap = CategoryModel.GetInstance().mIDToCategory;
        final Map<String, Long> bgtNameIdMap = new HashMap<>();
        final Map<String, Long> catNameIdMap = new HashMap<>();
        final ArrayList<String> bgts = new ArrayList<>();
        final ArrayList<String> cats = new ArrayList<>();

        for (Map.Entry<Long, Budget> entry : bgtMap.entrySet()) {
            Long bgtId = entry.getKey();
            Budget bgt = entry.getValue();
            bgtNameIdMap.put(bgt.getmName(), bgtId);
            bgts.add(bgt.getmName());
        }

        for (Map.Entry<Long, Category> entry : catMap.entrySet()) {
            Long catId = entry.getKey();
            Category cat = entry.getValue();
            catNameIdMap.put(cat.getmName(), catId);
        }

        ArrayAdapter<String> bgtAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, bgts);

        final ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, cats);

        bgtSpinner.setAdapter(bgtAdapter);
        catSpinner.setAdapter(catAdapter);

        bgtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                cats.clear();
                Long selectedBgtID = bgtNameIdMap.get(bgtSpinner.getSelectedItem());
                List<Category> cList = BudgetModel.GetInstance().getCategoriesUnderBudget(selectedBgtID);
                for (Category c : cList) {
                    if(c == null) continue;
                    cats.add(c.getmName());
                    catAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        final Switch autoSwitch = promptView.findViewById(R.id.auto_switch);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (transAmount.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Amount cannot be empty! Please Try Again", Toast.LENGTH_SHORT).show();
                        } else {
                            TransactionModel.GetInstance().addTransaction(
                                    new Transaction(Double.parseDouble(transAmount.getText().toString()),
                                            catNameIdMap.get(catSpinner.getSelectedItem()).longValue(),
                                            transNote.getText().toString(),
                                            transYear,
                                            transMonth,
                                            transDay,
                                            autoSwitch.isChecked()));
                            String trans = Double.parseDouble(transAmount.getText().toString()) + " " +
                                    catNameIdMap.get(catSpinner.getSelectedItem()).longValue() + " " +
                                    transNote.getText().toString() + " " +
                                    transYear + " " +
                                    transMonth + " " +
                                    transDay;
                            Toast.makeText(getContext(), "Add Transaction: " + trans, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                transDateText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                transYear = year;
                transMonth = month;
                transDay = dayOfMonth;
            }
        }, transYear, transMonth, transDay);
        datePickerDialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File photoFile=null;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

       }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Log.d("On Activity Result:**", "onActivityResult: ");

            new RestAsync().execute();
        }
    }

    public class RestAsync extends AsyncTask<String,String,String>
    {
        BufferedReader httpResponseReader;
        HttpsURLConnection urlConnection;
        double amount=0.0;
        String date="Today";
        Double taxAmount=0.0;
        String address="3135 Mclintock Avenue, Los Angeles,CA, 90007";
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings){

            try{
                SSLContext sslContext = SSLContexts.createSystemDefault();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslContext,
                        SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
                CloseableHttpClient  httpclient= HttpClientBuilder.create()
                        .setSSLSocketFactory(sslsf)
                        .build();
                HttpPost httpPost = new HttpPost("https://api.taggun.io/api/receipt/v1/simple/encoded");
                String encodedFile=encodeFileToBase64Binary(photoFile);
                Log.d("My File to 64 encoded", encodedFile);
                String jsonString = new JSONObject()
                        .put("image",encodedFile)
                        .put("filename", "example.jpg")
                        .put("contentType", "image/jpeg")
                        .put("refresh",true)
                        .put("incognito",false)
                        .put("ipAddress","219.88.232.1")
                        .put("language","en").toString();
                StringEntity entity = new StringEntity(jsonString);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("apikey","c0080e90c34611e7a0ebfdc7a5da208a");

                CloseableHttpResponse response = httpclient.execute(httpPost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    Log.d("I fucking did it", "doInBackground: ");
                    InputStream ips=response.getEntity().getContent();
                    BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String s;
                    while(true )
                    {
                        s = buf.readLine();
                        if(s==null || s.length()==0)
                            break;
                        sb.append(s);

                    }
                    buf.close();
                    ips.close();
                   String result= sb.toString();
                    Log.d("The content is: ", result);
                    JSONObject js=new JSONObject(result);
                    try {
                        amount=js.getJSONObject("totalAmount").getDouble("data");

                    }catch (Exception e)
                    {

                    }
                    try {
                        date=js.getJSONObject("date").getString("data");
                    }catch (Exception e)
                    {

                    }
                    try {
                        taxAmount=js.getJSONObject("taxAmount").getDouble("data");
                    }catch (Exception e)
                    {

                    }
                    try{
                        address=js.getJSONObject("merchantAddress").getString("data");
                    }
                    catch (Exception e)
                    {

                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Bad Receipt, please try again",Toast.LENGTH_SHORT).show();
                }
                httpclient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                progressBar.setVisibility(View.GONE);
                transAmount.setText(amount+"");
                transNote.setText("date: "+date+'\n'+"TaxAmount:"+taxAmount+"Address: "+address);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private String encodeFileToBase64Binary(File file)
            throws IOException {

        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);
        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }


}
