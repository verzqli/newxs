package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.young.newsgathering.entity.Article;
import com.young.newsgathering.entity.Material;
import com.young.newsgathering.entity.User;
import com.young.newsgathering.view.PreviewImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 写稿界面
 */
public class WriteArticleActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 1001;
    private String outPath = Environment.getExternalStorageDirectory() + "/pic";
    private LinearLayout mediaLayout;
    private LinearLayout picLayout;
    private LinearLayout videoLayout;
    private ImageView addBtn;
    private PreviewImageView articleMaterialImage;
    private EditText titleEdit;
    private EditText contentEdit;
    private String url;
    private Article article;
    //判断当前选择的是视频还是图片素材
    private boolean isImage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write_article;
    }

    @Override
    protected void initView() {
        setToolBar("新建稿件", "保存");
        article = new Article();
        mediaLayout = findViewById(R.id.media_layout);
        picLayout = findViewById(R.id.pic_layout);
        videoLayout = findViewById(R.id.video_layout);
        addBtn = findViewById(R.id.add_btn);
        titleEdit = findViewById(R.id.title_edit);
        contentEdit = findViewById(R.id.content_edit);
        articleMaterialImage = findViewById(R.id.article_material_image);

    }

    @Override
    protected void initEvent() {
        addBtn.setOnClickListener(v -> {
            if (mediaLayout.getVisibility() == View.VISIBLE) {
                mediaLayout.setVisibility(View.GONE);
            } else {
                mediaLayout.setVisibility(View.VISIBLE);
            }
        });
        picLayout.setOnClickListener(v -> {
            mediaLayout.setVisibility(View.GONE);
            openMedia(MimeType.ofImage());
            isImage = true;
        });
        videoLayout.setOnClickListener(v -> {
            mediaLayout.setVisibility(View.GONE);
            openMedia(MimeType.ofVideo());
            isImage = false;
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
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void menuClick() {
        String title = titleEdit.getText().toString().trim();
        String content = contentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.showShort("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("请输入内容");
            return;
        }
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort("请选择素材");
            return;
        }
        article.setTitle(title);
        article.setContent(content);
        if (isImage) {
            compressImage(url);
        } else {
            File file = new File(url);
            if (file.length() > 15728640) {
                ToastUtils.showShort("请选择大小15MB以下的视频");
                return;
            }
            uploadFile(new File(url));
        }

    }

    private void compressImage(String url) {
        Luban.with(this)
                .load(url)
                .ignoreBy(100)
                .setTargetDir(outPath)
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        showLoadDialog();
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件

                        uploadFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        hideLoadDialog();
                    }
                }).launch();
    }

    private void uploadFile(File file) {
        showLoadDialog();
        BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

                if (e == null) {
                    url = bmobFile.getFileUrl();
                    addArticle();
                } else {
                    hideLoadDialog();
                    ToastUtils.showShort("上传失败，请重试");
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                setDialogContent("正在上传:" + value + "%");
            }
        });
    }


    /**
     * 添加稿件
     */
    private void addArticle() {
        showLoadDialog();
        article.setMaterialUrl(url);
        article.setMaterialType(isImage ? "0" : "1");
        article.setEditor(UserUtil.getInstance().getUser().getName());
        article.setEditorId(UserUtil.getInstance().getUser().getObjectId());
        article.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hideLoadDialog();
                if (e == null) {
                    ToastUtils.showShort("添加成功");
                    finish();
                } else {
                    ToastUtils.showShort("添加失败,请重试");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //图片路径 同样视频地址也是这个 根据requestCode
            url = Matisse.obtainPathResult(data).get(0);
            articleMaterialImage.setUrl(url,isImage);
        }
    }
}
