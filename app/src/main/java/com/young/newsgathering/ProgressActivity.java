package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.young.newsgathering.entity.Article;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ProgressActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<Article> list;
    private ProgressListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progress;
    }

    @Override
    protected void initView() {
        setToolBar("稿件进度");
        recyclerView = findViewById(R.id.progress_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        list = new ArrayList<>();
        adapter = new ProgressListAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobQuery<Article> bmobQuery1 = new BmobQuery<>();
        BmobQuery<Article> bmobQuery2 = new BmobQuery<>();
        if (UserUtil.getInstance().isAdmin()) {
            BmobQuery<Article> bmobQuery = new BmobQuery<>();
            //总编查询所有不为草稿的稿件（审核中，退回，签发）
            bmobQuery.addWhereNotEqualTo("status", "草稿");
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
            //员工只查询自己不为草稿的稿件
            BmobQuery<Article> queryUser = new BmobQuery<>();
            //查询自己稿件
            queryUser.addWhereEqualTo("editorId", UserUtil.getInstance().getUser().getObjectId());
            BmobQuery<Article> queryStatus = new BmobQuery<>();
            //查询不为草稿的稿件
            queryStatus.addWhereNotEqualTo("status", "草稿");
            List<BmobQuery<Article>> queries = new ArrayList<>();
            //联合查询 = 员工查询自己不为草稿的稿件
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

    public class ProgressListAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
        public ProgressListAdapter(List data) {
            super(R.layout.item_progress_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Article item) {
            helper.setText(R.id.article_name_text, item.getTitle());
            helper.setText(R.id.article_create_text, item.getCreatedAt());
            helper.setText(R.id.article_editor_text, "来源: " + item.getEditor());
            TextView content = helper.getView(R.id.article_content_text);
            content.setText(item.getContent());
            TextView status = helper.getView(R.id.article_status_text);
            boolean isReturn = false;
            //判断稿件状态，给设置对应的颜色和状态值
            if ("审核中".equals(item.getStatus())) {
                status.setText("审核中");
                status.setTextColor(Color.YELLOW);
                content.setBackgroundResource(R.drawable.shape_yellow);
            } else if ("签发".equals(item.getStatus())) {
                status.setText("签发");
                status.setTextColor(Color.GREEN);
                content.setBackgroundResource(R.drawable.shape_dash_green);
            } else {
                status.setText("退回");
                status.setTextColor(Color.RED);
                content.setBackgroundResource(R.drawable.shape_red_dash);
                isReturn = true;
            }
            //如果该稿件被退回，就要显示退回等一系列资料
            if (isReturn) {
                helper.setVisible(R.id.return_data_layout, true);
                helper.setText(R.id.return_status_text, "退回原因(1)");
                helper.setText(R.id.review_name_text, "退回总编 : " + item.getReviewer());
                helper.setText(R.id.review_time_text, "退回时间 : " + item.getReviewTime());
                helper.setText(R.id.review_reason_text, "退回原因 : " + item.getReason());
            } else {
                //签发和审核中显示退回原因(0)
                helper.setText(R.id.return_status_text, "退回原因(0)");
            }
        }
    }
}
