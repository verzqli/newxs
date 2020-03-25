package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        findViewById(R.id.material_image_layout).setOnClickListener(v -> {
            openMedia(true);
        });
        findViewById(R.id.material_video_layout).setOnClickListener(v -> {
            openMedia(false);
        });
    }

    private void openMedia(boolean isImage) {
        Intent intent = new Intent(this, MaterialListActivity.class);
        intent.putExtra("isImage", isImage);
        startActivity(intent);
    }
}
