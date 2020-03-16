package com.young.newsgathering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.young.newsgathering.entity.Article;
import com.young.newsgathering.entity.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 员工和总编的稿件列表
 */
public class ArticleListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<Article> list;
    private ArticleListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_list;
    }

    @Override
    protected void initView() {
        setToolBar("发稿", R.drawable.icon_toolbar_menu);
        recyclerView = findViewById(R.id.recyclerview);

    }

    @Override
    protected void initEvent() {
        list = new ArrayList<>();
        adapter = new ArticleListAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Utils.article = (Article) adapter.getData().get(position);
            baseStartActivity(ArticleDetailActivity.class);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserUtil.getInstance().isAdmin()) {
            //总编查询条件，查询自己发的稿件
            BmobQuery<Article> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("editorId", UserUtil.getInstance().getUser().getObjectId());
            bmobQuery.findObjects(new FindListener<Article>() {
                @Override
                public void done(List<Article> list, BmobException e) {
                    if (e == null) {
                        if (list.size() == 0) {
                            ToastUtils.showShort("暂无稿件");
                        }
                        adapter.setNewData(list);
                    } else {
                        ToastUtils.showShort("加载数据异常，请重试");
                    }
                }
            });
        } else {
            //员工查询条件，只查询和当前用户相同id发的稿件，
            BmobQuery<Article> queryUser = new BmobQuery<>();
            queryUser.addWhereEqualTo("editorId", UserUtil.getInstance().getUser().getObjectId());
            //查询该员工状态为草稿（稿件写完未发稿）的稿件
            BmobQuery<Article> queryStatus = new BmobQuery<>();
            queryStatus.addWhereEqualTo("status", "草稿");
            //两个条件联合查询=员工查询自己发的且状态为草稿的稿件
            List<BmobQuery<Article>> queries = new ArrayList<>();
            queries.add(queryUser);
            queries.add(queryStatus);
            BmobQuery<Article> query = new BmobQuery<>();
            query.and(queries);
            query.findObjects(new FindListener<Article>() {
                @Override
                public void done(List<Article> list, BmobException e) {
                    if (e == null) {
                        if (list.size() == 0) {
                            ToastUtils.showShort("暂无稿件");
                        }
                        adapter.setNewData(list);
                    } else {
                        ToastUtils.showShort("加载数据异常，请重试");
                    }
                }
            });
        }

    }

    @Override
    public void menuClick() {
        //防止数据混淆，写稿页面知道当前是写稿，而不是修改
        Utils.article = null;
        baseStartActivity(WriteArticleActivity.class);
    }

    public class ArticleListAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
        public ArticleListAdapter(List data) {
            super(R.layout.item_article_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Article item) {
            helper.setText(R.id.article_name_text, item.getTitle());
            helper.setText(R.id.article_create_text, item.getCreatedAt());
            helper.setText(R.id.article_editor_text, "来源: " + item.getEditor());
        }
    }
}
