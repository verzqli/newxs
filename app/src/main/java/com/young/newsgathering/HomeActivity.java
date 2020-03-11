package com.young.newsgathering;


import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.young.newsgathering.fragment.NewFragment;
import com.young.newsgathering.fragment.PersonalFragment;
import com.young.newsgathering.fragment.WorkFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private ViewPager viewPager;
    private LinearLayout workLayout;
    private LinearLayout newsLayout;
    private LinearLayout personalLayout;
    private List<Fragment> list;

    @Override
    protected void initEvent() {
        list = new ArrayList<>();
        list.add(WorkFragment.newInstance());
        list.add(NewFragment.newInstance());
        list.add(PersonalFragment.newInstance());
        viewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));
        workLayout.setOnClickListener(v -> viewPager.setCurrentItem(0, false));
        newsLayout.setOnClickListener(v -> viewPager.setCurrentItem(1, false));
        personalLayout.setOnClickListener(v -> viewPager.setCurrentItem(2, false));
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        workLayout = findViewById(R.id.work_layout);
        newsLayout = findViewById(R.id.news_layout);
        personalLayout = findViewById(R.id.personal_layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    public class HomeAdapter extends FragmentPagerAdapter {

        public HomeAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
