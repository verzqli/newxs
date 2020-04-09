package com.young.newsgathering.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.young.newsgathering.MainActivity;
import com.young.newsgathering.R;
import com.young.newsgathering.ResetPwdActivity;
import com.young.newsgathering.ToastUtils;
import com.young.newsgathering.UserUtil;
import com.young.newsgathering.entity.User;
import com.young.newsgathering.util.BlurTransformation;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    private static final int REQUEST_CODE_CHOOSE = 1002;
    private ImageView avatarImage;
    private ImageView avatarBlurImage;
    private String outPath = Environment.getExternalStorageDirectory() + "/pic";

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        //获取本地保存的用户信息
        User user = UserUtil.getInstance().getUser();
        ((TextView) view.findViewById(R.id.name_text)).setText(user.getName());
        ((TextView) view.findViewById(R.id.job_text)).setText(user.getJob());
        ((TextView) view.findViewById(R.id.phone_text)).setText(user.getPhone());

        avatarImage = view.findViewById(R.id.avatar_image);
        avatarBlurImage = view.findViewById(R.id.avatar_blur_bg);
        //加载用户头像
        Glide.with(avatarImage.getContext())
                .load(UserUtil.getInstance().getUser().getAvatar())
                .placeholder(R.drawable.icon_avatar)
                .into(avatarImage);
        Glide.with(avatarBlurImage.getContext())
                .load(UserUtil.getInstance().getUser().getAvatar())
                .transform(new BlurTransformation())
                .placeholder(R.drawable.icon_avatar)
                .into(avatarBlurImage);
        //点击头像进入相册
        avatarImage.setOnClickListener(v -> open());
        //跳转设置密码
        view.findViewById(R.id.reset_layout).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ResetPwdActivity.class));
        });
        //退出登录
        view.findViewById(R.id.login_out_btn).setOnClickListener(v -> {
            //删除本地保存的用户信息
            UserUtil.getInstance().logout();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
    }


    private void initEvent() {

    }

    private void open() {
        //相册框架
        Matisse.from(this)
                .choose(MimeType.ofImage())//只选择图片
                .showSingleMediaType(true)//只显示单一的类型，这里因为上面选择了图片，所以只显示图片
                .capture(false)//不显示拍照功能
                .maxSelectable(1)//最多显示一张
                .theme(R.style.Matisse_Zhihu)//相册UI主题
                .imageEngine(new GlideEngine())//用glide加载图片
                .forResult(REQUEST_CODE_CHOOSE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            //图片路径 同样视频地址也是这个 根据requestCode
            String url = Matisse.obtainPathResult(data).get(0);
            //选择图片后保存在本地后再上传到服务器（这里上传的是本地链接）
            UserUtil.getInstance().updateAvatar(url);
            //这个是展示圆形头像
            Glide.with(avatarImage.getContext())
                    .load(UserUtil.getInstance().getUser().getAvatar())
                    .placeholder(R.drawable.icon_avatar)
                    .into(avatarImage);
            //这个是展示圆形头像后面的模糊背景
            Glide.with(avatarBlurImage.getContext())
                    .load(UserUtil.getInstance().getUser().getAvatar())
                    .transform(new BlurTransformation())//这是我以前写的模糊功能，把图片转换成毛玻璃小锅显示出来
                    .placeholder(R.drawable.icon_avatar)
                    .into(avatarBlurImage);
            //压缩图片
            compressImage(url);
        }
    }

    private void compressImage(String url) {
        Luban.with(getActivity())
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
                        //图片压缩好后把图片上传
                        BmobFile bmobFile = new BmobFile(file);
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //图片上传服务器成功后把图片的网络链接上传到数据库
                                    updateAvatar(bmobFile.getFileUrl());
                                } else {
                                    ToastUtils.showShort("图片上传失败，请重试");
                                }
                            }

                            @Override
                            public void onProgress(Integer value) {
                                // 返回的上传进度（百分比）
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    private void updateAvatar(String fileUrl) {
        //更新头像
        User user = UserUtil.getInstance().getUser();
        user.setAvatar(fileUrl);
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("更新成功");
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("更新失败，请重试");
                }
            }
        });
    }
}
