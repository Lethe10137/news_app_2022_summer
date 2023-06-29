package com.java.lichenghao.newsrefactored.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.java.lichenghao.newsrefactored.MyApplication;
import com.java.lichenghao.newsrefactored.R;
import com.java.lichenghao.newsrefactored.data.News;
import com.java.lichenghao.newsrefactored.data.NewsResponse;
import com.java.lichenghao.newsrefactored.service.NewsManager;
import com.java.lichenghao.newsrefactored.service.PictureLoader;
import com.java.lichenghao.newsrefactored.service.TaskRunner;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{
    private List<News> newsList;
    private final Fragment fragment;
    private final LayoutInflater inflater;
    private Context mainActivity;


    public NewsListAdapter(Fragment fragment, Context context, List<News> newsList) {
        this.newsList = newsList;
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(context);
        this.mainActivity =  MyApplication.getContext();

    }
    
    @Override
    public int getItemViewType(int position){
        return newsList.get(position).getImages().length;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//这个函数来自2022年科协暑培的代码
        View itemView;
        if(viewType == 0) {
            itemView = inflater.inflate(R.layout.news_title_no_image, parent, false);
        }else if(viewType == 1){
            itemView = inflater.inflate(R.layout.news_title_one_image, parent, false);
        }else{
            itemView = inflater.inflate(R.layout.news_title_two_image, parent, false);
        }
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//这个函数来自2022年科协暑培的代码
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description;
        private ImageView picture, picture2;
        private int type;
        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
            //Log.d("ViewHolder",type + "");
            if(type == 0) {
                title = itemView.findViewById(R.id.news_no_pic_title);
                description = itemView.findViewById(R.id.news_no_pic_description);
            }else if(type ==1){
                title = itemView.findViewById(R.id.news_one_pic_title);
                description = itemView.findViewById(R.id.news_one_pic_description);
                picture = itemView.findViewById(R.id.news_one_pic_picture_0);
            }else{
                title = itemView.findViewById(R.id.news_two_pic_title);
                description = itemView.findViewById(R.id.news_two_pic_description);
                picture = itemView.findViewById(R.id.news_two_pic_picture_0);
                picture2 = itemView.findViewById(R.id.news_two_pic_picture_1);
            }
        }

        public void bindData(int position){
            var news = newsList.get(position);
            String title_ = news.getTitle();

            if(Utils.is_an_api_id_read(news.getIdFromAPI())){
                this.title.setText(Html.fromHtml("<font color=\"#999999\">" + title_ + "</font>"));
            }else{
                this.title.setText(title_);
            }
            String description = news.getSource() + news.getTime();
            this.description.setText(description);
            Log.d("Making",title_);

            if(type != 0) {
                PictureLoader.loadPictureWithPlaceHolder(mainActivity, news.getImages()[0], picture);
            }
            if(type >= 2){
                PictureLoader.loadPictureWithPlaceHolder(mainActivity, news.getImages()[1], picture2);
            }
            itemView.setOnClickListener(v -> {

                long id = NewsManager.getInstance().createNews(news);
                news.setId(id);
                notifyItemChanged(position);
                // download picture
                var bundle = new Bundle();
                bundle.putLong("newsId",id);
                Utils.replaceFragment(fragment, NewsDetailFragment.class, bundle);
            });
        }

    }

}
