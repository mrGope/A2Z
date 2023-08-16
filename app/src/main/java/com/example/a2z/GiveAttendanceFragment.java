package com.example.a2z;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class GiveAttendanceFragment extends  Fragment {


    public GiveAttendanceFragment() {
        // Required empty public constructor
    }


    ImageView im;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_give_attendance, container, false);


    }
}