package com.young.newsgathering.entity;

import com.young.newsgathering.UserUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.bmob.v3.BmobObject;

public class Article extends BmobObject {
    //稿件标题
    private String title;
    //稿件内容
    private String content;
    //稿件作者
    private String editor;
    //稿件状态 草稿（员工写完未传稿）,审核中,签发，退回
    private String status;
    //稿件作废原因
    private String reason;
    //稿件作者id
    private String editorId;
    //审稿人，给稿件写退回原因和签发的人的名字
    private String reviewer;
    //审稿时间，给稿件退回和签发的时间
    private String reviewTime;
    //稿件素材链接
    private String materialUrl;
    //稿件素材类型，0为图片，1为视频
    private String materialType;

    public String getMaterialType() {
        return materialType;
    }

    public boolean isImageMaterial() {
        return "0".equals(materialType);
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(long reviewTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reviewTime);
        SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.reviewTime = fmat.format(calendar.getTime());
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public Article() {
        if (UserUtil.getInstance().isAdmin()) {
            status = "签发";
        } else {
            status = "草稿";
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
