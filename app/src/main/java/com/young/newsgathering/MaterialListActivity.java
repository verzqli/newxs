package com.young.newsgathering;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.young.newsgathering.entity.Article;
import com.young.newsgathering.entity.Material;
import com.young.newsgathering.entity.User;
import com.young.newsgathering.view.MaterialItemImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.ui.widget.MediaGrid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MaterialListActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 3001;
    private RecyclerView materialRecyclerView;
    private MaterialListAdapter adapter;
    private List<Material> list;
    //判断是否是图片素材库，默认是
    private boolean isImage = true;
    private String outPath = Environment.getExternalStorageDirectory() + "/pic";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_material_list;
    }

    @Override
    protected void initView() {
        setToolBar("", R.drawable.icon_toolbar_menu);
        isImage = getIntent().getBooleanExtra("isImage", true);
        ToastUtils.showShort(isImage ? "0" : "1");
        list = new ArrayList<>();
        adapter = new MaterialListAdapter(list);
        materialRecyclerView = findViewById(R.id.material_recyclerview);
        materialRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        materialRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Utils.article = (Article) adapter.getData().get(position);
            baseStartActivity(ArticleDetailActivity.class);
        });
    }

    @Override
    protected void initEvent() {
        requestmaterialData();
        ;
    }

    private void requestmaterialData() {
        BmobQuery<Material> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("type", isImage ? "0" : "1");
        bmobQuery.findObjects(new FindListener<Material>() {
            @Override
            public void done(List<Material> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        ToastUtils.showShort("暂无素材");
                    }
                    adapter.setNewData(list);
                } else {
                    ToastUtils.showShort("加载数据异常，请重试");
                }
            }
        });
    }

    @Override
    public void menuClick() {
        openMedia(isImage ? MimeType.ofImage() : MimeType.ofVideo());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            //图片路径 同样视频地址也是这个 根据requestCode
            String url = Matisse.obtainPathResult(data).get(0);
            //压缩图片
            if (isImage) {
                compressImage(url);
            } else {
                uploadFile(new File(url));
            }
        }
    }


    private void compressImage(String url) {
        Luban.with(this)
                .load(url)
                .ignoreBy(100)
                .setTargetDir(outPath)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件

                            uploadFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    private void uploadFile(File file) {
        BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    uploadMaterial(bmobFile.getFileUrl());
                } else {
                    ToastUtils.showShort("上传失败，请重试");
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    private void uploadMaterial(String fileUrl) {
        Material material = new Material();
        material.setUrl(fileUrl);
        material.setType(isImage ? "0" : "1");
        material.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("添加素材成功");
                    adapter.addData(material);
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("添加失败，请重试");
                }
            }
        });
    }

    public class MaterialListAdapter extends BaseQuickAdapter<Material, BaseViewHolder> {
        public MaterialListAdapter(List data) {
            super(R.layout.item_material_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Material item) {
            MaterialItemImage imageView = helper.getView(R.id.image);
            if (isImage){
                Glide.with(MaterialListActivity.this).load(item.getUrl()).into(imageView);
            }else{
                imageView.setImageBitmap(createVideoThumbnail(item.getUrl(),200,200));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
