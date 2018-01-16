package com.example.tjun.reviewpoj.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjun.reviewpoj.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestLayoutFragment extends Fragment {

    public TestLayoutFragment() {
    }

    public static TestLayoutFragment newInstance() {
        TestLayoutFragment fragment = new TestLayoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_layout, container, false);
        return view;
    }

}
