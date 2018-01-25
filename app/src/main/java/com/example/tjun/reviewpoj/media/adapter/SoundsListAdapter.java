package com.example.tjun.reviewpoj.media.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjun.reviewpoj.R;
import com.example.tjun.reviewpoj.application.AbsRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/****
 * </pre> 
 *  Project_Name:    ReviewPOJ
 *  Copyright: 
 *  Version:         1.0.0.1
 *  Created:         Tijun on 2018/1/25 0025 15:15.
 *  E-mail:          prohankj@outlook.com
 *  Desc: 
 * </pre>            
 ****/
public class SoundsListAdapter extends AbsRecyclerViewAdapter {


    ArrayList<File> arrayList = new ArrayList<File>();

    public SoundsListAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        File file = arrayList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.name.setText(file.getName());
        super.onBindViewHolder(holder, position);
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_sounds_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setData(File[] data) {
        arrayList.clear();
        arrayList.addAll(Arrays.asList(data));
        notifyDataSetChanged();
    }

    private class ItemViewHolder extends ClickableViewHolder {
        TextView name;
        TextView data;

        public ItemViewHolder(View inflate) {
            super(inflate);
            name = $(R.id.tv_name);
            data = $(R.id.tv_time);
        }
    }
}
