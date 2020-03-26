package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片预览页面
 */
public class PreviewImageActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview_image;
    }

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra("url");
        ImageView imageView = findViewById(R.id.preview_image);
        Glide.with(this).load(url).into(imageView);
    }

    @Override
    protected void initEvent() {

    }
}
