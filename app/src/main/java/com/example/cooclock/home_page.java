package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class home_page extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){
//        ((main_page)getActivity()).updateSubCategories();
        return inflater.inflate(R.layout.activity_main_page,container,false);
    }
}