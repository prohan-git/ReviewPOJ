package com.example.tjun.reviewpoj.media;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjun.mp3recorder.utils.FileUtils;
import com.example.tjun.reviewpoj.R;
import com.example.tjun.reviewpoj.application.BaseFragment;
import com.example.tjun.reviewpoj.media.adapter.SoundsListAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoundsRecorderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoundsRecorderListFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.rv_recorder)
    RecyclerView rvRecorder;
    Unbinder unbinder;

    File[] files;

    private String mParam1;
    private String mParam2;
    private SoundsListAdapter recorderListAdapter;


    public SoundsRecorderListFragment() {

    }


    public static SoundsRecorderListFragment newInstance(String param1, String param2) {
        SoundsRecorderListFragment fragment = new SoundsRecorderListFragment();
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
        View view = inflater.inflate(R.layout.fragment_sounds_recorder_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        initData();
        loadList();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViews() {
        // 设置布局管理器
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvRecorder.setLayoutManager(mLayoutManager);
        recorderListAdapter = new SoundsListAdapter(rvRecorder);
        rvRecorder.setAdapter(recorderListAdapter);
    }

    /**
     * 获取录音列表
     */
    private void initData() {
        files = FileUtils.getFiles(FileUtils.getAppPath());
        for (int i = 0; i < files.length; i++) {
            Logger.d(files[i].getAbsolutePath() + "");
        }
    }

    private void loadList() {
        recorderListAdapter.setData(files);
    }


    public void refresh() {
        initData();
        loadList();
    }
}
