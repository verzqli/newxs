package com.young.newsgathering.entity;

import cn.bmob.v3.BmobObject;

public class Article extends BmobObject {
    //稿件标题
    private String title;
    //稿件内容
    private String content;
    //稿件作者
    private String editor;
    //稿件状态 0：未审核 1：审核成功 2：作废
    private String status;
    //稿件作废原因
    private String reason;
    //稿件作者id
    private String editorId;

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public Article() {
        status = "0";
        reason = "";
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
