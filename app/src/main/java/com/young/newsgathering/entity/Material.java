package com.young.newsgathering.entity;

import cn.bmob.v3.BmobObject;

/**
 * @author verzqli
 * @date 2020/3/25
 * @Desc 图片和视频素材
 */
public class Material extends BmobObject {
    private String url;
    //缩略图，主要给视频勇0
    private String thumb;
    //数据类型，0为图片，1为视频
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
