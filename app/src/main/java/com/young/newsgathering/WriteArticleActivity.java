package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;
import java.util.Set;

public class WriteArticleActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 1001;
    private LinearLayout mediaLayout;
    private LinearLayout picLayout;
    private LinearLayout videoLayout;
    private ImageView addBtn;
    private EditText titleEdit;
    private EditText contentEdit;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_article;
    }

    @Override
    protected void initView() {
        setToolBar("新建稿件", "保存");
        mediaLayout = findViewById(R.id.media_layout);
        picLayout = findViewById(R.id.pic_layout);
        videoLayout = findViewById(R.id.video_layout);
        addBtn = findViewById(R.id.add_btn);
        titleEdit = findViewById(R.id.title_edit);
        contentEdit = findViewById(R.id.content_edit);
    }

    @Override
    protected void initEvent() {
        addBtn.setOnClickListener(v -> {
            if (mediaLayout.getVisibility() == View.VISIBLE) {
                mediaLayout.setVisibility(View.GONE);
            } else {
                mediaLayout.setVisibility(View.VISIBLE);
            }
        });
        picLayout.setOnClickListener(v -> {
            mediaLayout.setVisibility(View.GONE);
            openMedia(MimeType.ofImage());
        });
        videoLayout.setOnClickListener(v -> {
            mediaLayout.setVisibility(View.GONE);
            openMedia(MimeType.ofVideo());
        });
    }

    private void openMedia(Set<MimeType> type) {
        Matisse.from(this)
                .choose(type)
                .showSingleMediaType(true)
                .capture(false)
                .maxSelectable(1)
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);

    }

    @Override
    public void menuClick() {
        String title = titleEdit.getText().toString().trim();
        String content = contentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.showShort("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("请输入内容");
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //图片路径 同样视频地址也是这个 根据requestCode
            url = Matisse.obtainPathResult(data).get(0);
            ToastUtils.showShort(url);
        }
    }
}
