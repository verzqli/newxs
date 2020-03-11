package com.young.newsgathering;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initEvent();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initEvent();

    public void setToolBar(String title) {
        setToolBar(title, -1);
    }

    public void setToolBar(String title, int moreBtnId) {
        if (moreBtnId != -1) {
            ImageView imageView = findViewById(R.id.toolbar_menu_btn);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(moreBtnId);
            imageView.setOnClickListener(v -> menuClick());

        }
        findViewById(R.id.toolbar_back_btn).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.toolbar_title_text)).setText(title);
    }

    public void setToolBar(String title, String menuText) {
        if (!TextUtils.isEmpty(menuText)) {
            TextView menuBtn = findViewById(R.id.toolbar_menu_text);
            menuBtn.setText(menuText);
            menuBtn.setVisibility(View.VISIBLE);
            menuBtn.setOnClickListener(v -> menuClick());

        }
        findViewById(R.id.toolbar_back_btn).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.toolbar_title_text)).setText(title);
    }

    public void menuClick() {
    }

    public void baseStartActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
