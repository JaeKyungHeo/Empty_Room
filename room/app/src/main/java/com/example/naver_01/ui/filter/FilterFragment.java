package com.example.naver_01.ui.filter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.naver_01.R;
import com.example.naver_01.ui.filter.FilterViewModel;
import com.example.naver_01.ui.home.HomeViewModel;

public class FilterFragment extends Fragment {

    private FilterViewModel filterViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        filterViewModel =
                new ViewModelProvider(this).get(FilterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filter, container, false);
        return root;
    }
}