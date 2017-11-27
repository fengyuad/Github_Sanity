package com.example.tomdong.sanity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import Model.Budget;

/**
 * Created by Yifan on 11/19 019.
 */

public class BudgetResetDialogFragment extends DialogFragment {

    Budget mBudget;

    public void setBudget(Budget budget) {
        this.mBudget = budget;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Budget")
                .setPositiveButton("Rollover", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
//                        ((MenuActivity) getActivity()).rightClick();
//                        ((MenuActivity) getActivity()).mDialogResult = true;
                    }
                })
                .setNegativeButton("Save Budget", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
//                        ((MenuActivity) getActivity()).leftClick();
//                        ((MenuActivity) getActivity()).mDialogResult = false;
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //123
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
