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
    private NewFragment fragment;

    @Override
    protected void initEvent() {
        list = new ArrayList<>();
        fragment = NewFragment.newInstance();
        list.add(WorkFragment.newInstance());
        list.add(fragment);
        list.add(PersonalFragment.newInstance());
        //首页展示的四个页面，用最普通的viewpager+fragment来完成，这里用自定义viewpager禁止其左右滑动
        //让他只靠下面四个tab点击跳转
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

    @Override
    public void onBackPressed() {
        //这里是传递返回键监听给新闻界面，因为新闻界面是fragment，不好监听返回事件，所以这里把这个
        //返回点击事件传递给它，方便他处理。
        if (viewPager.getCurrentItem() == 1) {
            if (!fragment.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
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
