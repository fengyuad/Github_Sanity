package com.example.tomdong.sanity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Model.TransactionModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    LineChart lineChart;
    View myTrendView;
    List<Double> data;
    Spinner mSpinner;
    String[] mMonths = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    public TrendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendFragment newInstance(String param1, String param2) {
        TrendFragment fragment = new TrendFragment();
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

        myTrendView = inflater.inflate(R.layout.fragment_trend, container, false);
        lineChart = myTrendView.findViewById(R.id.line_chart);
        mSpinner = myTrendView.findViewById(R.id.trend_spinner);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

//        LimitLine upper_limit = new LimitLine(65f, "Danger");
//        upper_limit.setLineWidth(4f);
//        upper_limit.enableDashedLine(10f, 10f, 0f);
//        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        upper_limit.setTextSize(15f);
//
//        LimitLine lower_limit = new LimitLine(35f, "Too Low");
//        lower_limit.setLineWidth(2f);
//        lower_limit.enableDashedLine(10f, 10f, 10f);
//        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        lower_limit.setTextSize(15f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        //leftAxis.addLimitLine(upper_limit);
        //leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(25f);
        leftAxis.enableGridDashedLine(10f,10f,0);
        leftAxis.setDrawLimitLinesBehindData(true);

        //lineChart.getAxisRight().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        data = TransactionModel.GetInstance().analyzeMonthlySpend();
        String[] month = new String[data.size()];
        for(int i = 0; i < data.size(); i++)
        {
            yValues.add(new Entry(i,Float.parseFloat(String.valueOf(data.get(i)))));
            month[i] = mMonths[i/12];
        }


        double maxVal = Collections.max(data);
        maxVal *= 1.05;
        leftAxis.setAxisMaximum(Float.parseFloat(String.valueOf(maxVal)));
        leftAxis.setAxisMinimum(0f);

        LineDataSet set1 = new LineDataSet(yValues, "Spending");
        set1.setFillAlpha(110);

        set1.setColor(Color.argb(200,255,99, 71));
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.GRAY);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter((new MyXAxisValueFormatter(month)));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        lineChart.setVisibleXRangeMaximum(4f);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Log.d("Trend Spinner", "Monthly");
                        break;
                    case 1:
                        Log.d("Trend Spinner", "Weekly");
                        break;
                    case 2:
                        Log.d("Trend Spinner", "Daily");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return myTrendView;
    }

    void monthly() {

    }

    void weekly() {

    }

    void daily() {

    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
