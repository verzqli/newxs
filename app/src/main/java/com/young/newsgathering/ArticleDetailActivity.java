package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ArticleDetailActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initView() {
        setToolBar("稿件详情");
    }

    @Override
    protected void initEvent() {

    }
}
