package com.java.lichenghao.newsrefactored.data;



import com.java.lichenghao.newsrefactored.service.NewsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import lombok.Data;


@Data
public  class User {

    private String name = "TestUser";
    //private  List<Long> read_history = new ArrayList<>();
    //private  List<Long> like_history = new ArrayList<>();
    public  List<UserPreference> selected = new ArrayList<>();
    public  List<UserPreference> unselected = new ArrayList<>();

    public User(){
        selected.add(UserPreference.教育);
        selected.add(UserPreference.娱乐);
        selected.add(UserPreference.科技);
        selected.add(UserPreference.体育);
        selected.add(UserPreference.健康);
        unselected.add(UserPreference.军事);
        unselected.add(UserPreference.文化);
        unselected.add(UserPreference.汽车);
        unselected.add(UserPreference.社会);
        unselected.add(UserPreference.财经);
    }




}
