package com.young.newsgathering;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.young.newsgathering.entity.Material;
import com.young.newsgathering.view.MaterialItemImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
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
    /**
     * 素材库界面有两个进来的方式
     * 一个是素材功能，一个是写稿功能
     * 这个boolean判断当前是否是从写稿功能进来的，如果是，那么点击item就不是预览，而是返回数据
     */
    private boolean isGetResult = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_material_list;
    }

    @Override
    protected void initView() {
        isImage = getIntent().getBooleanExtra("isImage", true);
        isGetResult = getIntent().getBooleanExtra("isGetResult", false);
        setToolBar(isImage ? "图片素材" : "视频素材", R.drawable.icon_toolbar_menu);

        list = new ArrayList<>();
        adapter = new MaterialListAdapter(list);
        materialRecyclerView = findViewById(R.id.material_recyclerview);
        materialRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        materialRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            String url = ((Material) adapter.getItem(position)).getUrl();
            if (isGetResult) {
                Intent intent = new Intent();
                intent.putExtra("url", url);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                if (isImage) {
                    Intent intent = new Intent(this, PreviewImageActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, PreviewVideoActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }

        });
    }

    @Override
    protected void initEvent() {
        requestmaterialData();
    }

    private void requestmaterialData() {
        showLoadDialog();
        //请求素材数据，根据传进来的isImage判断是拿适配素材还是图片素材
        BmobQuery<Material> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("type", isImage ? "0" : "1");
        bmobQuery.findObjects(new FindListener<Material>() {
            @Override
            public void done(List<Material> list, BmobException e) {
                hideLoadDialog();
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

    /**
     * 这里是返回你进去相册点击后返回的照片或视频链接，
     * 然后把它上传到服务器拿到网络链接后再传到数据库表里
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                File file = new File(url);
                if (file.length() > 15728640) {
                    ToastUtils.showShort("请选择大小15MB以下的视频");
                    return;
                }
                showLoadDialog();
                uploadFile(new File(url));
            }
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
                        Log.i("测试测试", "压缩失败" + e.getMessage());
                        hideLoadDialog();
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

    private void uploadMaterial(String fileUrl) {
        Material material = new Material();
        material.setUrl(fileUrl);
        material.setType(isImage ? "0" : "1");
        material.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hideLoadDialog();
                if (e == null) {
                    ToastUtils.showShort("添加素材成功");
                    adapter.addData(material);
                } else {
                    Log.i("测试测试", "添加失败" + e.getMessage());
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
            Glide.with(getContext()).load(item.getUrl()).into(imageView);
//            imageView.setUrl(item.getUrl(), isImage);
        }
    }
}
