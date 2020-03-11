package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ArticleListActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_list;
    }

    @Override
    protected void initView() {
        setToolBar("发稿",R.drawable.icon_toolbar_menu);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void menuClick() {
        baseStartActivity(WriteArticleActivity.class);
    }
}
