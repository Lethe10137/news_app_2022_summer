package com.java.lichenghao.newsrefactored;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.tabs.TabLayout;
import com.java.lichenghao.newsrefactored.data.UserPreference;
import com.java.lichenghao.newsrefactored.databinding.ActivityMainBinding;
import com.java.lichenghao.newsrefactored.service.FetchFromAPIManager;
import com.java.lichenghao.newsrefactored.ui.NewsListFragment;
import com.java.lichenghao.newsrefactored.ui.SearchFragment;
import com.java.lichenghao.newsrefactored.ui.SelectPaddleFragment;
import com.java.lichenghao.newsrefactored.ui.TabListFragment;
import com.java.lichenghao.newsrefactored.ui.UserPageFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TabListFragment.onTabBarListener,
        SelectPaddleFragment.onSelectPaddleListener, SearchFragment.OnSearchInputFinished{

    private ActivityMainBinding binding;
    public BottomNavigationView navView;
    public SearchView searchView;
    public FragmentContainerView mainArea;
    public FragmentContainerView tabs;
    private SelectPaddleFragment selectPaddleFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private TabListFragment tabListFragment;
    private NewsListFragment newsListFragment;





    private float mPosX, mPosY, mCurPosX, mCurPosY;
    Boolean newsListVisible = true;


    @Override
    public void selectPaddleConfirmed(){
        drawerLayout.closeDrawer(Gravity.LEFT);

        tabListFragment.update_list();

    }

    @Override
    public void menuBarClicked() {
        Log.d("Mainactivity", "menu clicked");
        drawerLayout.openDrawer(Gravity.LEFT);

    }

    @Override
    public void tabSelected(String tag) {
        Log.d("Mainactivity", "menu tab"+ tag);
        List<String> a  = new ArrayList<>();
        a.add(tag);
        FetchFromAPIManager.getInstance().setSubjects(a);
        newsListFragment.reloadNews();
    }

    @Override
    public void reportCurrent(List<UserPreference> selected, List<UserPreference> unselected) {
        MyApplication.myUser.selected = selected;
        MyApplication.myUser.unselected = unselected;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {//这个函数来自2022年科协暑培的代码
        super.onCreate(savedInstanceState);



        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navView = binding.navView;

       // searchView = binding.searchView;
        tabs = binding.fragmentContainerup;
       //searchView.setVisibility(View.GONE);
        //selectPaddle = binding.selectPaddle;
        mainArea = binding.fragmentContainer;
        navView.setOnItemSelectedListener(this::onNavItemSelected);
        MyApplication.setNavView(navView);

        mainArea.setLongClickable(true);
//        mainArea.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//
//                    case MotionEvent.ACTION_DOWN:
//                        mPosX = event.getX();
//                        mPosY = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        mCurPosX = event.getX();
//                        mCurPosY = event.getY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > 50)) {
//                            Log.e("TAG", "wang向右");
//                           // initView(String.format("%0" + tvUsernum.getText().toString().length() + "d", (Integer.parseInt(tvUsernum.getText().toString()) - 1)));
//                        } else if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > 50)) {
//                            Log.e("TAG", "wang向左");
//                            //initView(String.format("%0" + tvUsernum.getText().toString().length() + "d", (Integer.parseInt(tvUsernum.getText().toString()) + 1)));
//                        }
//                        break;
//
//                       /* if (mCurPosY - mPosY > 0
//                                && (Math.abs(mCurPosY - mPosY) > 25)) {
//                            Log.e("TAG", "wang向下");
//                        } else if (mCurPosY - mPosY < 0
//                                && (Math.abs(mCurPosY - mPosY) > 25)) {
//                            Log.e("TAG", "wang向上");
//                        }
//                        break;*/
//                }
//                return false;
//            }
//        });

        fragmentManager = getSupportFragmentManager();
        selectPaddleFragment = (SelectPaddleFragment) fragmentManager.findFragmentById(R.id.select_paddle);

        tabListFragment = (TabListFragment) fragmentManager.findFragmentByTag("upper_fragment_in_container");
        newsListFragment = (NewsListFragment) fragmentManager.findFragmentByTag("fragment_in_container");

        MyApplication.setTopFragmentContainer(tabs);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.LEFT);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void replaceFragment(Class<? extends Fragment> fragmentClass) {//这个函数来自2022年科协暑培的代码
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null) //代表支持不同的返回栈
                .commit();
    }

    private boolean onNavItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.posts) {

            if(! MyApplication.newsPage) replaceFragment(NewsListFragment.class);
            MyApplication.newsPage = true;
            MyApplication.searchPage = false;
            MyApplication.userPage = false;

            tabs.setVisibility(View.VISIBLE);

            if(MyApplication.newsPageisSearchingPage){
                newsListFragment.reloadNews();
                FetchFromAPIManager.reset_navi();
            }
            MyApplication.newsPageisSearchingPage = false;
          //  selectPaddle.setVisibility(View.GONE);
            return true;
        } else if (item.getItemId() == R.id.user) {
            if(! MyApplication.userPage) replaceFragment(UserPageFragment.class);
            MyApplication.newsPage = false;
            MyApplication.searchPage = false;
            MyApplication.userPage = true;

          //  searchView.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
          //  selectPaddle.setVisibility(View.GONE);
            return true;
        } else if (item.getItemId() == R.id.search){
            if( ! MyApplication.searchPage) replaceFragment(SearchFragment.class);
            MyApplication.newsPage = false;
            MyApplication.searchPage = true;
            MyApplication.userPage = false;

         //   searchView.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
          //  selectPaddle.setVisibility(View.VISIBLE);
           return true;
        }
        return false;
    }


    @Override
    public void finished() {
        if(! MyApplication.newsPage) replaceFragment(NewsListFragment.class);
        Log.d("finished searching Input", "newsPageisSearchingPage = true");
        MyApplication.newsPage = true;
        MyApplication.searchPage = false;
        MyApplication.userPage = false;
        MyApplication.newsPageisSearchingPage = true;
        newsListFragment.reloadNews();
    }
}