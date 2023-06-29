package com.java.lichenghao.newsrefactored.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.java.lichenghao.newsrefactored.R;
import com.java.lichenghao.newsrefactored.data.News;
import com.java.lichenghao.newsrefactored.service.NewsManager;
import com.java.lichenghao.newsrefactored.service.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public class RecordListFragment extends Fragment {
    public static final int PAGE_SIZE = 10;
    public static final String LOG_TAG = RecordListFragment.class.getSimpleName();


    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private int mode = 0; // 0 for history, 1 for favorite;


    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt("mode");
        }
        Log.d("record list", "mode" + mode);
    }

    public RecordListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        var view = inflater.inflate(R.layout.fragment_record_list, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.news_list);
        var llm = new LinearLayoutManager(context);
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        recyclerView.setLayoutManager(llm);
        listAdapter = new NewsListAdapter(this, context, NewsManager.getInstance().get_record(mode));
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        return view;
    }



}
