package com.takkat.counting.datMoaaena;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takkat.counting.R;

import androidx.annotation.Nullable;

public class DateAndTimeDialog extends DialogFragment {


    private View view ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.date_and_time_dialog, container, false);

        return view ;
    }
}
