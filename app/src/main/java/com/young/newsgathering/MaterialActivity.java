package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.Set;

public class MaterialActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_material;
    }

    @Override
    protected void initView() {
            setToolBar("素材库");
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.material_image_layout).setOnClickListener(v->{
            openMedia(MimeType.ofImage());
        });
        findViewById(R.id.material_video_layout).setOnClickListener(v->{
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
                .forResult(1);
    }
}
