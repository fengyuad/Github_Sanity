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
                    cats.add(c.getmName());
                    catAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


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
                                            transDay));
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
               // encodedFile="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oC2+AEdp8JvB/wAPbbxBLbax4GeC403WYbdVaO6gDpva3dnVkdHZHjLkMp65wRlXP7O+p6691rnjLxe+s+J7y70eRr77CkEMVno94t4lrBbJJ8gkfcXdpHO5gccYIB5LL+0d4u8WfFL4Sab4OvVt/D+u6ZDd63GsUUga51C0uZoYC7ozo0RtXJCMuc85GKg/Z1+KPij4h23hzUNc+KerajrF3avc3Wkv4cit7JmRGLIL5bGNMDAIKTfMRgZ6V6Z4U/ZU0rwjq1nqena4SLLxHea4iG15FtcW0ttDZBvN4WASswkx83I2jORufC74O/Ff4baNofhBviHZal4b0aL7P9l/sLyZ5IQDhftH2x9pyc7th+lAHzB8Lfj/AON9btPh7eWnxQHjTxN4hv7ODVPDP9m2imC2mYi5lEtrDE8fkR/vNzsV4wQa+t/iV4l8bal8RvDvwk8B6unhyfUrC91a91I20d3NHbWrxQpHBHLmPdJJL8zOrbVXgEmqOk/AA6H4N+G2hadr3laz8Np43t9SFr/x8QMrR3Vu0Pm/KlxG204kJUhW5xiuy+Inwy1DxZrWjeMfCevv4Y8T6ElxBBeC3S7hktrrb5sE8Dsm9SyIykOpVhkHmgDybWfGHxv8N6Vovw91i7tI/FHiPxHJpGn66YI2STTI7drpr5rRH2LOFRohE21SwDY2nFebfFb4rfFf4S6d4q8Eax4wS61CztNF1Wx1/wCw20U0Nne6mljdxzwbGt2KDLIwjHyscjIBHtv/AAz2ZPC/2efxTdv4vXWj4iXXhDGGTUvL8nK2xzGLfyf3Rgzgrn5t3NZeqfs5an4nsNYvPGXjB9X8T6xJpKtqH2FIYILPSbxbxLaC1ST5RI4YuzSsdzA9sEA8n0T4k/F7xA/ie3+FfjX/AIWNYeHLOz1aHUTp1tbrdXKTP9p0ctFCsbma3XeskSrJG5UFsE59++DvxE1j4w6nqXxD0maW28BmGKz0q2lhVJLq4T57q7csvmAI5+zIobaSkjEElSPfq84+Evw//wCFW/D7S/Av2/8AtP8Asw3B+0eV5O/z55Jvub3xjft+8c4z7UAej0UUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAV8/ftY/8msfGP8A7E3xB/6bp6+ga+fv2sf+TWPjH/2JviD/ANN09AH/1/17/ZO/5NY+Dn/Ym+H/AP03QV9A18/fsnf8msfBz/sTfD//AKboK+gaACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAr5+/ax/5NY+Mf/Ym+IP8A03T19A18/ftY/wDJrHxj/wCxN8Qf+m6egD//0P17/ZO/5NY+Dn/Ym+H/AP03QV9A18/fsnf8msfBz/sTfD//AKboK+gaACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAr5+/ax/5NY+Mf/Ym+IP8A03T19A18/ftY/wDJrHxj/wCxN8Qf+m6egD//0fwDooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigD/9k=";
                String jsonString = new JSONObject()
                       // .put("image","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")
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
                transNote.setText("date: "+date+'\n'+"TaxAmount:"+taxAmount);

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
