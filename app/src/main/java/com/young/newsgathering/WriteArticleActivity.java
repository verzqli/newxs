package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.young.newsgathering.entity.Article;
import com.young.newsgathering.entity.User;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;
import java.util.Set;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 这个页面 写稿和修改共用，用isRevise判断是哪个情况
 */
public class WriteArticleActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 1001;
    private LinearLayout mediaLayout;
    private LinearLayout picLayout;
    private LinearLayout videoLayout;
    private ImageView addBtn;
    private EditText titleEdit;
    private EditText contentEdit;
    private String url;
    private Article article;
    //判断跳转到这个页面是对稿件进行修改还是写稿,默认是写稿，为true时是修改
    private boolean isRevise = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_article;
    }

    @Override
    protected void initView() {
        setToolBar("新建稿件", "保存");
        article = Utils.article;
        Utils.article = null;
        mediaLayout = findViewById(R.id.media_layout);
        picLayout = findViewById(R.id.pic_layout);
        videoLayout = findViewById(R.id.video_layout);
        addBtn = findViewById(R.id.add_btn);
        titleEdit = findViewById(R.id.title_edit);
        contentEdit = findViewById(R.id.content_edit);

        if (article != null) {
            isRevise = true;
            titleEdit.setText(article.getTitle());
            contentEdit.setText(article.getContent());
        } else {
            article = new Article();
        }
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
        article.setTitle(title);
        article.setContent(content);
        if (isRevise) {
            reviseArticle();
        } else {
            addArticle();
        }

    }

    /**
     * 修改稿件
     */
    private void reviseArticle() {

        article.update(article.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("修改成功");
                    finish();
                } else {
                    ToastUtils.showShort("修改失败,请重试");
                }
            }
        });
    }

    /**
     * 添加稿件
     */
    private void addArticle() {
        showLoadDialog();
        article.setEditor(UserUtil.getInstance().getUser().getName());
        article.setEditorId(UserUtil.getInstance().getUser().getObjectId());
        article.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hideLoadDialog();
                if (e == null) {
                    ToastUtils.showShort("添加成功");
                    finish();
                } else {
                    ToastUtils.showShort("添加失败,请重试");
                }
            }
        });
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
