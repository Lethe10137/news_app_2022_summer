package com.java.lichenghao.newsrefactored.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.lichenghao.newsrefactored.MyApplication;
import com.java.lichenghao.newsrefactored.R;
import com.java.lichenghao.newsrefactored.data.News;
import com.java.lichenghao.newsrefactored.service.NewsManager;
import com.java.lichenghao.newsrefactored.service.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    public static final int PAGE_SIZE = 10;
    public static final String LOG_TAG = NewsListFragment.class.getSimpleName();

    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private EndlessRecyclerViewScrollListener listScrollListener;

    private Context context;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout listContainer;
    private ConstraintLayout mainArea;
    private float mPosX, mPosY, mCurPosX, mCurPosY;
    private int page  = 1;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NewsList", "onCreateView");

        if(MyApplication.NewsList != null){
            return MyApplication.NewsList;
        }

        var view = inflater.inflate(R.layout.fragment_news_list, container, false);
        context = view.getContext();

//        mainArea = view.findViewById(R.id.constraintlayout_area);
//        mainArea.setLongClickable(true);
//        setTouchListener(mainArea);

        listContainer = view.findViewById(R.id.news_list_container);
        listContainer.setOnRefreshListener(this::reloadNews);

        recyclerView = view.findViewById(R.id.news_list);

        var llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        listScrollListener = new EndlessRecyclerViewScrollListener(llm, (page, totalItemsCount, view1) -> loadNextPage());
        recyclerView.addOnScrollListener(listScrollListener);

        listAdapter = new NewsListAdapter(this, context, newsList);
        recyclerView.setAdapter(listAdapter);

        loadingBar = view.findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);


        reloadNews();

        MyApplication.NewsList = view;
        return view;
    }



    private void loadNextPage() {//这个函数来自2022年科协暑培的代码
        page += 1;
        Log.d("NewsListFragment","loadNextPage()" + page);
        loadingBar.setVisibility(View.VISIBLE);
        NewsManager.getInstance().newsList(PAGE_SIZE * (page-1), PAGE_SIZE, new NewsFetchCallback(false));
    }

    public void reloadNews() {//这个函数来自2022年科协暑培的代码
        page = 1;
        if(listScrollListener == null)return;
        Log.d("NewsListFragment","reloadNews()");
        listScrollListener.resetState();
        NewsManager.getInstance().newsList(0, PAGE_SIZE, new NewsFetchCallback(true));
    }

    public class NewsFetchCallback implements TaskRunner.Callback<List<News>> {//这个函数来自2022年科协暑培的代码
        private final boolean reload;

        public NewsFetchCallback(boolean reload) {
            this.reload = reload;
        }//这个函数来自2022年科协暑培的代码

        @Override
        public void complete(TaskRunner.Result<List<News>> res) {//这个函数来自2022年科协暑培的代码
            loadingBar.setVisibility(View.GONE);
            if (reload) {
                listContainer.setRefreshing(false);
            }
            if (res.isOk()) {
                if (reload) {
                    newsList.clear();
                }
                newsList.addAll(res.getResult());
                listAdapter.notifyDataSetChanged();
            } else {
                Log.e(LOG_TAG, "Post fetch failed due to exception", res.getError());
            }
        }
    }

}
