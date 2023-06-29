package com.java.lichenghao.newsrefactored.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.java.lichenghao.newsrefactored.MyApplication;
import com.java.lichenghao.newsrefactored.R;
import com.java.lichenghao.newsrefactored.data.User;
import com.java.lichenghao.newsrefactored.data.UserPreference;

import java.util.ArrayList;
import java.util.List;

public class SelectPaddleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private List<UserPreference> selected = new ArrayList<>();
    private List<UserPreference> unselected = new ArrayList<>();

    private SelectPaddleAdapter selected_adapter;
    private SelectPaddleAdapter unselected_adapter;

    private GridView selected_grid_view;
    private GridView unselected_grid_view;

    private SelectPaddleAdapter.Interface listener;
    private Button confirmButton;





    public SelectPaddleFragment() {
        // Required empty public constructor

    }



    public List<UserPreference> getSelected(){
        return selected;
    }

    public List<UserPreference> getUnselected(){
        return unselected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
        listener = (new SelectPaddleAdapter.Interface() {
            @Override
            public void onChangeSelect(int position, boolean currentStatus) {
                if(currentStatus){
                    unselected.add(selected.remove(position));
                }else{
                    selected.add(unselected.remove(position));
                }
                selected_adapter.notifyDataSetChanged();
                unselected_adapter.notifyDataSetChanged();
                mListener.reportCurrent(selected, unselected);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        upload();
        mListener.selectPaddleConfirmed();
    }

    public void upload(){
        MyApplication.myUser.selected = this.selected;
        MyApplication.myUser.unselected = this.unselected;
    }

    private onSelectPaddleListener mListener;

    public interface onSelectPaddleListener {
        void reportCurrent(List<UserPreference> selected, List<UserPreference> unselected);
        void selectPaddleConfirmed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectPaddleFragment.onSelectPaddleListener) {
            mListener = (SelectPaddleFragment.onSelectPaddleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       if (MyApplication.myUser.selected != null && MyApplication.myUser.unselected != null) {
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
    }else{
        selected = MyApplication.myUser.selected;
        unselected = MyApplication.myUser.unselected;
    }

        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_select_paddle, container, false);
        selected_grid_view = view.findViewById(R.id.selected);
        unselected_grid_view = view.findViewById(R.id.unselected);

        selected_adapter = new SelectPaddleAdapter(listener ,selected, this.getContext(),true);
        unselected_adapter = new SelectPaddleAdapter(listener,unselected, this.getContext(),false);
        selected_grid_view.setAdapter(selected_adapter);
        unselected_grid_view.setAdapter(unselected_adapter);
        confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {upload();mListener.selectPaddleConfirmed();});
        return view;
    }
}