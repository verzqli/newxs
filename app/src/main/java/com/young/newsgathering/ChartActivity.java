package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.young.newsgathering.entity.Article;
import com.young.newsgathering.entity.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * 统计页面
 */
public class ChartActivity extends BaseActivity {
    private PieChart chart;
    private int draftCount;//草稿数量
    private int reviewingCount;//审核中数量
    private int failCount;//退回数量
    private int successCount;//签发数量

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobQuery<Article> bmobQuery = new BmobQuery<>();
        //总编获取全部不为草稿的稿件，员工获取自己所有的稿件
        if (UserUtil.getInstance().isAdmin()) {
            bmobQuery.addWhereNotEqualTo("status", "草稿");
        } else {
            bmobQuery.addWhereEqualTo("editorId", UserUtil.getInstance().getUser().getObjectId());
        }
        bmobQuery.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        ToastUtils.showShort("暂无相关稿件");
                    }
                    dealData(list);
                } else {
                    ToastUtils.showShort("加载数据异常，请重试");
                }
            }


        });
    }


    private void setViewData() {
        ((TextView) findViewById(R.id.user_name_text)).setText("当前用户 : " + UserUtil.getInstance().getUser().getName());
        ((TextView) findViewById(R.id.total_article_text)).setText("稿件总数 : " + (draftCount + reviewingCount + successCount + failCount) + " 件");
        ((TextView) findViewById(R.id.success_article_text)).setText("签发稿件 : " + successCount + " 件");
        ((TextView) findViewById(R.id.fail_article_text)).setText("退回稿件 : " + failCount + " 件");
        ((TextView) findViewById(R.id.review_article_text)).setText("待审稿件 : " + reviewingCount + " 件");
        if (!UserUtil.getInstance().isAdmin()) {
            ((TextView) findViewById(R.id.draft_article_text)).setText("未传稿件 : " + draftCount + " 件");
        }
    }

    @Override
    protected void initView() {
        setToolBar("统计");
        chart = findViewById(R.id.chart_view);
        if (!UserUtil.getInstance().isAdmin()) {
            findViewById(R.id.draft_article_text).setVisibility(View.VISIBLE);
        }
        chart.setUsePercentValues(true);
        //不显示描述label
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文字
        chart.setCenterText("稿件状态数量");
        chart.setCenterTextSize(15);
        chart.setCenterTextColor(R.color.themeColor);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(51f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // 触摸旋转
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (!UserUtil.getInstance().isAdmin()) {
            entries.add(new PieEntry(draftCount, "草稿"));
        }
        entries.add(new PieEntry(reviewingCount, "审核中"));
        entries.add(new PieEntry(successCount, "签发"));
        entries.add(new PieEntry(failCount, "退回"));
        setData(entries);

        chart.animateY(1400, Easing.EaseInOutQuad);
        //设置最下方的颜色块描述文字
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTextColor(Color.GRAY);
        l.setYOffset(10f);
    }

    @Override
    protected void initEvent() {

    }

    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");

        //数据和数据饼块颜色
        ArrayList<Integer> colors = new ArrayList<>();
        if (!UserUtil.getInstance().isAdmin()) {
            colors.add(rgb("#3498db"));//蓝色:草稿
        }
        colors.add(rgb("#f1c40f"));//审核中:黄色
        colors.add(rgb("#2ecc71"));//签发:绿色
        colors.add(rgb("#e74c3c"));//退回:红色
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.highlightValues(null);
        //刷新
        chart.invalidate();
    }

    private void dealData(List<Article> list) {
        /**
         *  遍历所有的稿件，获得相应状态稿件的数量
         *  总编：将稿件按审核中，签发，退回分类拿到数量
         *  员工：将稿件按草稿，审核中，签发，退回分类拿到数量
         */
        if (UserUtil.getInstance().isAdmin()) {
            for (Article article : list) {
                String status = article.getStatus();
                if ("审核中".equals(status)) {
                    reviewingCount++;
                    //这里我不知道是否存在多个总编情况，所以总编只获取自己审批的稿件
                } else if ("退回".equals(status) && article.getReviewer().equals(UserUtil.getInstance().getUser().getName())) {
                    failCount++;
                } else if ("签发".equals(status) && article.getReviewer().equals(UserUtil.getInstance().getUser().getName())) {
                    successCount++;
                }
            }
        } else {
            for (Article article : list) {
                String status = article.getStatus();
                if ("草稿".equals(status)) {
                    draftCount++;
                } else if ("审核中".equals(status)) {
                    reviewingCount++;
                } else if ("退回".equals(status)) {
                    failCount++;
                } else if ("签发".equals(status)) {
                    successCount++;
                }
            }
        }
        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (!UserUtil.getInstance().isAdmin()) {
            entries.add(new PieEntry(draftCount, "草稿"));
        }
        entries.add(new PieEntry(reviewingCount, "审核中"));
        entries.add(new PieEntry(successCount, "签发"));
        entries.add(new PieEntry(failCount, "退回"));
        setData(entries);
        setViewData();
    }


    public class MyValueFormatter extends ValueFormatter {

        public DecimalFormat mFormat;
        private PieChart pieChart;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        // Can be used to remove percent signs if the chart isn't in percent mode
        public MyValueFormatter(PieChart pieChart) {
            this();
            this.pieChart = pieChart;
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + " %";
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
                // Converted to percent
                return getFormattedValue(value);
            } else {
                // raw value, skip percent sign
                return String.valueOf(pieEntry.getValue()).replace(".0", "") + "(" + mFormat.format(value) + "%" + ")";
            }
        }

    }


}
