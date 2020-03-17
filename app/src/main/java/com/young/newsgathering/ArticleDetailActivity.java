package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.young.newsgathering.entity.Article;
import com.young.newsgathering.view.ReviewDialog;

import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ArticleDetailActivity extends BaseActivity {
    //总编显示的地步操作按键:签发和作废
    private LinearLayout articleEditLayout;
    //员工显示的地步操作按键:作废 修改 和 发稿
    private LinearLayout userEditLayout;
    private Article article;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initView() {
        setToolBar("稿件详情");
        article = Utils.article;
        Utils.article = null;
        articleEditLayout = findViewById(R.id.article_admin_edit_layout);
        userEditLayout = findViewById(R.id.article_user_edit_layout);
        ((TextView) findViewById(R.id.article_name_text)).setText(article.getTitle());
        ((TextView) findViewById(R.id.article_create_text)).setText(article.getCreatedAt());
        ((TextView) findViewById(R.id.article_length_text)).setText("字数: " + article.getContent().length());
        ((TextView) findViewById(R.id.article_content_text)).setText(article.getContent());
    }

    @Override
    protected void initEvent() {
        if (UserUtil.getInstance().isAdmin()) {
            //如果当前查看的是总编，并且看的还是一个签发的稿件，说明这个稿件是他自己写的，也就不存在作废和签发
            if (!"签发".equals(article.getStatus())) {
                articleEditLayout.setVisibility(View.VISIBLE);
                //作废点击
                articleEditLayout.getChildAt(0).setOnClickListener(v -> {
                    showReturnDialog();
                });
                //签发点击
                articleEditLayout.getChildAt(1).setOnClickListener(v -> {
                    successArticle();
                });
            }

        } else {
            userEditLayout.setVisibility(View.VISIBLE);
            //作废点击
            userEditLayout.getChildAt(0).setOnClickListener(v -> {
                deleteArticle();
            });
            //修改点击
            userEditLayout.getChildAt(1).setOnClickListener(v -> {
                editArticle();
            });
            //发稿点击
            userEditLayout.getChildAt(2).setOnClickListener(v -> {
                sendArticle();
            });
        }
    }

    /**
     * 底部弹出填写退回原因的弹窗
     */
    private void showReturnDialog() {
        ReviewDialog.newInstance()
                .setConfirmClickListener(this::returnArticle)
                .show(getSupportFragmentManager());
    }

    //修改稿件，跳转到写稿界面（员工）
    private void editArticle() {
        Utils.article = article;
        baseStartActivity(WriteArticleActivity.class);
        finish();

    }

    //发稿点击（员工）
    private void sendArticle() {
        article.setStatus("审核中");
        updateData(article, "发稿");
    }

    //作废稿件（员工）
    private void deleteArticle() {
        showLoadDialog();
        article.delete(article.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                hideLoadDialog();
                if (e == null) {
                    ToastUtils.showShort("作废成功");
                    finish();
                } else {
                    ToastUtils.showShort("作废失败，请重试");
                }
            }
        });
    }

    //签发稿件（总编）
    private void successArticle() {
        article.setStatus("签发");
        article.setReviewer(UserUtil.getInstance().getUser().getName());
        article.setReviewTime(new Date().getTime());
        updateData(article, "签发");
    }

    //退回稿件（总编）
    private void returnArticle(String reason) {
        showLoadDialog();
        article.setStatus("退回");
        article.setReason(reason);
        article.setReviewer(UserUtil.getInstance().getUser().getName());
        article.setReviewTime(new Date().getTime());
        article.update(article.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                hideLoadDialog();
                if (e == null) {
                    ToastUtils.showShort("退回成功");
                    finish();
                } else {
                    ToastUtils.showShort("退回失败，请重试");
                }
            }
        });
    }

    private void updateData(Article article, String msg) {
        showLoadDialog();
        article.update(article.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                hideLoadDialog();
                if (e == null) {
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtils.showShort(msg + "成功");
                    }
                    finish();
                } else {
                    ToastUtils.showShort(msg + "失败，请重试");
                }
            }
        });
    }
}
