package com.example.noteapp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class FragmentDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //Create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));



        return super.onCreateDialog(savedInstanceState);
    }
}
