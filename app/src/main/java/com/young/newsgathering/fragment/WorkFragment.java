package com.young.newsgathering.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.young.newsgathering.ArticleListActivity;
import com.young.newsgathering.ArticleReviewActivity;
import com.young.newsgathering.ProgressActivity;
import com.young.newsgathering.R;
import com.young.newsgathering.UserUtil;
import com.young.newsgathering.entity.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkFragment extends Fragment {
    private LinearLayout writeLayout;
    private LinearLayout editLayout;
    private LinearLayout progressLayout;
    private LinearLayout materialLayout;
    private LinearLayout chartLayout;

    public WorkFragment() {
        // Required empty public constructor
    }


    public static WorkFragment newInstance() {
        return new WorkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        writeLayout = view.findViewById(R.id.write_layout);
        editLayout = view.findViewById(R.id.edit_layout);
        progressLayout = view.findViewById(R.id.progress_layout);
        materialLayout = view.findViewById(R.id.material_layout);
        chartLayout = view.findViewById(R.id.chart_layout);

    }

    private void initEvent() {
        writeLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), ArticleListActivity.class)));
        //总编才有审稿功能
        if (UserUtil.getInstance().isAdmin()) {
            editLayout.setVisibility(View.VISIBLE);
            editLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), ArticleReviewActivity.class)));
        }
        progressLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), ProgressActivity.class)));
    }
}
