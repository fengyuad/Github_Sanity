package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected void showInputDialog() {

        // get input_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext()).setView(promptView);

        final EditText transAmount = (EditText) promptView.findViewById(R.id.trans_amt);
        final EditText transNote = (EditText) promptView.findViewById((R.id.trans_note));
        Button transDateButton = (Button) promptView.findViewById(R.id.trans_date_button);
        transDateText = (TextView) promptView.findViewById(R.id.trans_date_text);
        transDateText.setText(transYear + "-" + (transMonth + 1) + "-" + transDay);
        transDateButton.setOnClickListener(this);
        final Spinner bgtSpinner = (Spinner) promptView.findViewById(R.id.bgt_spinner);
        final Spinner catSpinner = (Spinner) promptView.findViewById(R.id.cat_spinner);

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

}
