package com.java.lichenghao.newsrefactored.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.lichenghao.newsrefactored.MyApplication;
import com.java.lichenghao.newsrefactored.R;
import com.java.lichenghao.newsrefactored.data.News;
import com.java.lichenghao.newsrefactored.service.NewsManager;
import com.java.lichenghao.newsrefactored.service.PictureLoader;
import com.java.lichenghao.newsrefactored.service.TaskRunner;

import java.util.List;

public class NewsDetailFragment extends Fragment {
    private Context context;
    private News newsToShow;
    private TextView news_title, news_description, news_content;
    private ImageView news_image, news_image2, news_image3, news_image4;
    private Long news_id = -1L;
    private FloatingActionButton button1, button2;
    private FragmentContainerView containerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        var view = inflater.inflate(R.layout.reading_news_one_pic, container, false);
        if(getArguments() != null){
            news_id = getArguments().getLong("newsId",-1);
        }
        if(news_id >= 0){
            newsToShow =  NewsManager.getInstance().getNews(news_id);
        }else{
            newsToShow = Utils.initNews("Error","","","","","","","");
        }


        context = view.getContext();

        news_title =  view.findViewById(R.id.detail_title);
        news_description = view.findViewById(R.id.detail_description);
        news_content = view.findViewById(R.id.detail_content);
        news_image = view.findViewById(R.id.detail_image1);
        news_image2 = view.findViewById(R.id.detail_image2);
        news_image3 = view.findViewById(R.id.detail_image3);
        news_image4 = view.findViewById(R.id.detail_image4);
        containerView = view.findViewById(R.id.fragment_to_contain_video);

        button1 = view.findViewById(R.id.favoriteFloatingActionButton);
        button2 = view.findViewById(R.id.favoriteFloatingActionButton2);
        Log.d("NewsDetailFragment","open_a_news" + newsToShow.getId());
        news_title.setText(newsToShow.getTitle());
        news_description.setText(newsToShow.getSource() + "    " +newsToShow.getTime());
        news_content.setText(newsToShow.getContent());

        button1.setOnClickListener(v->{handle_favorite_click(1);});
        button2.setOnClickListener(v->{handle_favorite_click(2);});

        if(!newsToShow.isFavorites()){
            button2.setVisibility(View.GONE);
        }else{
            button1.setVisibility(View.GONE);
        }

        if(newsToShow.getImages().length >=1 ) {
            PictureLoader.loadPictureWithoutPlaceHolder(context,newsToShow.getImages()[0],news_image);
        }else{
            news_image.setVisibility(View.GONE);
        }
        if(newsToShow.getImages().length >= 2 ) {
            PictureLoader.loadPictureWithoutPlaceHolder(context,newsToShow.getImages()[1],news_image2);
        }else{
            news_image2.setVisibility(View.GONE);
        }
        if(newsToShow.getImages().length >= 3 ) {
            PictureLoader.loadPictureWithoutPlaceHolder(context,newsToShow.getImages()[2],news_image3);
        }else{
            news_image3.setVisibility(View.GONE);
        }
        if(newsToShow.getImages().length >= 4 ) {
            PictureLoader.loadPictureWithoutPlaceHolder(context,newsToShow.getImages()[3],news_image4);
        }else{
            news_image4.setVisibility(View.GONE);
        }
        if(newsToShow.getVideo().length != 0){
            String path = newsToShow.getVideo()[0];
            VideoFragment to_fill = VideoFragment.newInstance(path);
            getParentFragmentManager().beginTransaction().add(R.id.fragment_to_contain_video, to_fill).commit();
        }else{
            containerView.setVisibility(View.GONE);
        }

        MyApplication.getNavView().setVisibility(View.GONE);
        MyApplication.getTopFragmentContainer().setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getNavView().setVisibility(View.VISIBLE);
        if(MyApplication.newsPage && !MyApplication.newsPageisSearchingPage) MyApplication.getTopFragmentContainer().setVisibility(View.VISIBLE);
    }

    private void handle_favorite_click(int i){
        Toast.makeText(context,""+ i,Toast.LENGTH_SHORT);
        if(i == 1){
            NewsManager.getInstance().favorite_trigerred(news_id,true);
            button2.setVisibility(View.VISIBLE);
            button1.setVisibility(View.GONE);
        }else{
            //newsToShow.setFavorites(false);
            NewsManager.getInstance().favorite_trigerred(news_id,false);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.GONE);
        }
    }




}
