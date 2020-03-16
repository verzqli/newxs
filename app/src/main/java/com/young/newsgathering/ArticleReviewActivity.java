package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.young.newsgathering.entity.Article;

import java.util.List;

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
        recyclerView = findViewById(R.id.recyclerview);
    }

    @Override
    protected void initEvent() {

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
