package com.example.tomdong.sanity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionPickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionPickerFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button transFromButton, transToButton;
    TextView transFromText, transToText;
    ListView transHistory;
    private int fromYear, fromMonth, fromDay;
    private int toYear, toMonth, toDay;

    public TransactionPickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionPickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionPickerFragment newInstance(String param1, String param2) {
        TransactionPickerFragment fragment = new TransactionPickerFragment();
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

        final Calendar c = Calendar.getInstance();
        fromDay = c.get(Calendar.DAY_OF_MONTH);
        fromMonth = c.get(Calendar.MONTH);
        fromYear = c.get(Calendar.YEAR);

        toDay = c.get(Calendar.DAY_OF_MONTH);
        toMonth = c.get(Calendar.MONTH);
        toYear = c.get(Calendar.YEAR);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_transaction_picker, container, false);

        transFromButton = (Button) v.findViewById(R.id.trans_from_button);
        transToButton = (Button) v.findViewById(R.id.trans_to_button);
        transFromText = (TextView) v.findViewById(R.id.trans_from_text);
        transToText = (TextView) v.findViewById(R.id.trans_to_text);
        transHistory =(ListView) v.findViewById(R.id.trans_picker_list);
        transFromButton.setOnClickListener(this);
        transToButton.setOnClickListener(this);

        ArrayList<Transaction_card> list = new ArrayList<>();

        list.add(new Transaction_card("Parking","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("Eating","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("Studying","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("Working","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("Skiing","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("Gaming","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("Travelling","2017-10-9","400$","I love you"));
        list.add(new Transaction_card("pooping","2017-10-9","400$","I love you"));
        CustomTransactionCardAdapter adapter = new CustomTransactionCardAdapter(getContext(), R.layout.tran_item, list);
        transHistory.setAdapter(adapter);
        transFromText.setText(fromYear + "-" + (fromMonth + 1) + "-" + fromMonth);
        transToText.setText(toYear + "-" + (toMonth + 1) + "-" + toMonth);

        return v;
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

    @Override
    public void onClick(View v) {
        if(v == transFromButton) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    transFromText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }, fromYear, fromMonth, fromDay);
            datePickerDialog.show();
        }
        if(v == transToButton) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    transToText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }, toYear, toMonth, toDay);
            datePickerDialog.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
