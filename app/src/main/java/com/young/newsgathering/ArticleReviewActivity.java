package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.young.newsgathering.entity.Article;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 审稿列表
 */
public class ArticleReviewActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<Article> list;
    private ArticleListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_review;
    }

    @Override
    protected void initView() {
        setToolBar("审稿", R.drawable.icon_toolbar_menu);
        recyclerView = findViewById(R.id.review_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        list = new ArrayList<>();
        adapter = new ArticleListAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Utils.article = (Article) adapter.getData().get(position);
            baseStartActivity(ArticleDetailActivity.class);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobQuery<Article> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("status", "审核中");
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
